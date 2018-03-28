package com.example.dqt02.uploadimg.Utils;

import android.graphics.Bitmap;
import android.os.Handler;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;

/**
*
*@作者 lenovo
*@创建时间 2018/3/23 14:40
*@类名 ImageThread
*@描述
*
*/


public class ImageThread implements Runnable {

    public static final  int UPLOAD = 1; // 上传图片
    public static final  int SEARCH = 2; // 搜索图片
    public static final  int UPLOAD_FG = 3; // 上传图片指纹
    public static final  Double DECODE =  0.8; // 搜索图片
    public int actionType;
    private String filePath;
    public Handler handler;
    UploadImageCallBack uploadImageCallBack;
    Searcher searcher;

    public Bitmap bitmap;
    public String imageUrl;

    /**
     * 上传或者搜索图片
     */
    public void uploadOrSearch(int flag,String filePath){
        actionType = flag;
        this.filePath = filePath;
    }
    public void setUploadImgCallBack(UploadImageCallBack uploadImageCallBack){
        this.uploadImageCallBack = uploadImageCallBack;
    }

    @Override
    public void run() {
        searcher = new Searcher();
        String res = null;
        if(actionType == UPLOAD){
            File file = new File(filePath);
            try {
                res = Uploader.upload("http://www.aiforu.com/api/Image/Image/uploader.shtml", file);   //以文件形式传到服务器
            } catch (NetworkException e) {
                e.printStackTrace();
            }
        }else if(actionType == SEARCH){
            try {
                res = searcher.searchImage(bitmap,imageUrl,DECODE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            try {
                res = searcher.addImage(bitmap,imageUrl,DECODE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(uploadImageCallBack != null){
            try {
                uploadImageCallBack.uploadImageCallBack(res,actionType);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public interface  UploadImageCallBack{
        void uploadImageCallBack(String res,int type) throws IOException, JSONException;
    }
}
