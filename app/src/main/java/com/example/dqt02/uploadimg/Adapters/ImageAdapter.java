package com.example.dqt02.uploadimg.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dqt02.uploadimg.Utils.ImgUtils;
import com.example.dqt02.uploadimg.Models.ImageModel;
import com.example.dqt02.uploadimg.R;

import java.util.List;

/**
 * Created by lenovo on 2018/3/23.
 */

public class ImageAdapter extends BaseAdapter {
    Context context;
    List<ImageModel> datas;
    LayoutInflater inflater;
    ImgUtils imgUtils;
    public ImageAdapter(Context context, List<ImageModel> datas) {
        this.context = context;
        this.datas = datas;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imgUtils = new ImgUtils(context);
    }

    @Override
    public int getCount() {
        return datas == null ? 0: datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ImageView imageView;
        TextView txt;
        MyViewHolder myViewHolder = null;
        if(view == null){
           view = inflater.inflate(R.layout.img_item,null);
           imageView = view.findViewById(R.id.iv);
           txt = view.findViewById(R.id.txt);
           view.setTag(new MyViewHolder(imageView,txt));
        }
        myViewHolder = (MyViewHolder) view.getTag();
        imageView = myViewHolder.imageView;
        txt = myViewHolder.textView;
        ImageModel imageModel = datas.get(position);
        String imgUrl = imageModel.url.replace("%3A",":").replace("%2F","/");
        imgUtils.loadImg(imageView,imgUrl);
        txt.setText("相似度为："+Double.valueOf(imageModel.similarity)*100+"%");
        return view;
    }
    class MyViewHolder{
        public ImageView imageView;
        public TextView textView;
        public MyViewHolder(ImageView imageView, TextView textView){
                this.imageView = imageView;
                this.textView = textView;
        }
    }
}
