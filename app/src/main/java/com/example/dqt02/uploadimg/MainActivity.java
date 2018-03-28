package com.example.dqt02.uploadimg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.dqt02.uploadimg.Utils.ImgUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button addButton;
    Button searchButton;
    ImgUtils imgUtils;
    ImageView ivShowPicture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgUtils=new ImgUtils(this);
        String refImageUrl="http://www.aiforu.com/api/Public/images/2018032110097484.jpg ";

        addButton = findViewById(R.id.btn_add);
        searchButton = findViewById(R.id.btn_search);
        ivShowPicture=findViewById(R.id.ivShowPicture);

//        imgUtils.loadImg(ivShowPicture,refImageUrl);

        addButton.setOnClickListener(this);
        searchButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,ImageActivity.class);//启动ImageActivity
        if(v == addButton){
            intent.putExtra("ACTION","add");
        }else if(v == searchButton){
            intent.putExtra("ACTION","search");
        }
        startActivity(intent);
    }
}
