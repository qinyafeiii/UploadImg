package com.example.dqt02.uploadimg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dqt02.uploadimg.Adapters.ImageAdapter;
import com.example.dqt02.uploadimg.Models.ImageModel;
import com.example.dqt02.uploadimg.Utils.ImageThread;
import com.example.dqt02.uploadimg.Utils.PullData;
import org.json.JSONException;
import org.json.JSONObject;
public class ImageActivity extends Activity implements View.OnClickListener, ImageThread.UploadImageCallBack {

    private Button btnOpenCamera;
    private Button btnSavePhoto;
    private ImageView ivShowPicture;
    private List<ImageView> views = new ArrayList<>();
    private final static int REQUEST_CAMERA = 0;     //打开摄像头请求
    private final static int REQUEST_OPEN_IMAGE = 1;  //打开相册请求
    private String mFilePath;          //获取媒体文件路径
    private String action = null;
    private Bitmap bitmap = null;   //获取缩略图为空
    String refImageUrl = null;    //定义参考图片地址为空
    String refSimilarity = null;
    private ProgressDialog dialog;
    String filePath;

//    ListView lv;
    GridView lv;
    ImageAdapter adapter;
    List<ImageModel> datas = new ArrayList<>();

    ImageThread imageThread;

    TextView txt_1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        initView();
    }
    @SuppressLint("WrongViewCast")
    private void initView() {
        // 初始化控件
        txt_1 = findViewById(R.id.txt_1);
        btnOpenCamera = (Button) findViewById(R.id.btnOpenCamera);
        btnSavePhoto = (Button) findViewById(R.id.btnSavePhoto);
        ivShowPicture = (ImageView) findViewById(R.id.ivShowPicture);

        lv = findViewById(R.id.lv);
        mFilePath = Environment.getExternalStorageDirectory().getPath();// 获取SD卡路径
        mFilePath = mFilePath + "/" + "temp.png";// 指定路径
        // 控件绑定点击事件
        btnOpenCamera.setOnClickListener(this);
        btnSavePhoto.setOnClickListener(this);
        action = getIntent().getStringExtra("ACTION");

        if (action.equals("search")) {
            txt_1.setVisibility(View.VISIBLE);
            lv.setVisibility(View.VISIBLE);
            adapter = new ImageAdapter(this, datas);
            lv.setAdapter(adapter);
        }
        initDialog();
    }

    private void initDialog() {
        dialog = ProgressDialog.show(this, "提示", "正在处理图片", false);
        dialog.dismiss();
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btnOpenCamera:
                // 拍照并显示图片
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
                startActivityForResult(intent, REQUEST_CAMERA);
                break;
            case R.id.btnSavePhoto:
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_OPEN_IMAGE);  //把图片数据传给请求
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == REQUEST_CAMERA) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                bitmap = extras.getParcelable("data");      //获得拍的照片
            }
        } else if (requestCode == REQUEST_OPEN_IMAGE) {
            //用户从图库选择图片后会返回所选图片的Uri
            Uri uri = data.getData();
            InputStream is;
            try {
                is = getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(is);
                is.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bitmap = Bitmap.createScaledBitmap(bitmap, 640, 640, false);
        }
        ivShowPicture.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 640, 480, false));
        File outputDir = this.getCacheDir(); // 在内存缓存创建一个临时文件
        File outputFile = null;
        try {
            outputFile = File.createTempFile("prefix", ".jpg", outputDir);   //在指定目录中创建新的空文件夹
        } catch (IOException e) {
            e.printStackTrace();
        }
        filePath = outputFile.getAbsolutePath();   //得到文件绝对路径
        saveBitmapTofile(bitmap, filePath);          //保存压缩图片到文件
        uploadImg();                   //上传图片的文件路径
    }
    private boolean saveBitmapTofile(Bitmap bmp, String filename) {
        if (bmp == null || filename == null)
            return false;
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;   //压缩为JPEG格式
        int quality = 100;           //压缩率
        OutputStream stream = null;   //写入压缩数据的输出流为空
        try {
            stream = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bmp.compress(format, quality, stream);
    }
    /**
     * 上传图片
     */
    private void uploadImg() {
        dialog.show();
        if (imageThread == null) {
            imageThread = new ImageThread();
        }
        if (action.equals("search")) {
            File file = new File(filePath);
            if (file.exists()) {
                imageThread.bitmap = BitmapFactory.decodeFile(filePath);
            } else {
                return;
            }
            imageThread.uploadOrSearch(ImageThread.SEARCH, filePath);
        } else {
            imageThread.uploadOrSearch(ImageThread.UPLOAD, filePath);

        }
        imageThread.setUploadImgCallBack(this);
        new Thread(imageThread).start();
    }

    /**
     * 解析json数组并显示图片
     *
     * @param jsonObject
     */
    private void pullListData(JSONObject jsonObject) throws JSONException {
        datas.clear();
        datas.addAll(PullData.pullListData(jsonObject));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void uploadImageCallBack(String res, int type) throws IOException, JSONException {
        Message message = new Message();
        message.what = type;
        message.obj = res;
        handler.sendMessage(message);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int type = msg.what;
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(msg.obj + "");
                if (type == ImageThread.UPLOAD) {
                    if (jsonObject.optString("is_success").equals("T")) {
                        dialog.dismiss();
                        if (jsonObject.optString("is_success").equals("T")) {
                            Toast.makeText(ImageActivity.this, R.string.add_success, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ImageActivity.this, R.string.add_failed, Toast.LENGTH_LONG).show();
                        }
//                        imageUrl = jsonObject.optString("url");
//                        imageId = jsonObject.optString("id");
//                        imageThread.bitmap = BitmapFactory.decodeFile(filePath);
//                        imageThread.imageUrl = imageUrl;
//                        imageThread.actionType = ImageThread.UPLOAD_FG;
//                        new Thread(imageThread).start();
                    }
                } else if (type == ImageThread.SEARCH) {
                    dialog.dismiss();
                    if (jsonObject.optString("is_success").equals("T")) {
                        refImageUrl = jsonObject.optString("ref_url");
                        refSimilarity = jsonObject.optString("similarity");
                        bitmap = null;
                        pullListData(jsonObject);
                    } else {
                        Toast.makeText(ImageActivity.this, R.string.search_failed, Toast.LENGTH_LONG).show();
                    }
                } else if (type == ImageThread.UPLOAD_FG) {
                    dialog.dismiss();
                    if (jsonObject.optString("is_success").equals("T")) {
                        Toast.makeText(ImageActivity.this, R.string.add_success, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ImageActivity.this, R.string.add_failed, Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}