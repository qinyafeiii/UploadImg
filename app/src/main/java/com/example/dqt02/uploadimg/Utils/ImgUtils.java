package com.example.dqt02.uploadimg.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
/**
 * Created by lenovo on 2018/3/22.
 */
public class ImgUtils {
    Context context;
    DisplayImageOptions options;
    ImageLoader imageLoader;
    public ImgUtils(Context context)
    {
        this.context = context;
        initImageLoader();
        imageLoader = ImageLoader.getInstance();
    }
    /**
     * 加载图片
     * @param view 需要加载图片的ImageView
     * @param imgUrl 图片的下载地址
     */
    public void loadImg(ImageView view,String imgUrl){
        imageLoader.displayImage(imgUrl, view, options);
    }
    /**
     * 初始化ImageLoader的相关参数
     */
    public void initImageLoader(){
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).imageDownloader(
                new BaseImageDownloader(context, 60 * 1000, 60 * 1000)) // connectTimeout超时时间
                .build();
        ImageLoader.getInstance().init(config);
    }
}
