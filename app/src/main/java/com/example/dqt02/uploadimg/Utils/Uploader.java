package com.example.dqt02.uploadimg.Utils; /**
 * Created by dqt02 on 2018/3/6.
 */

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Uploader {
    public static String upload(String actionUrl, File file) throws NetworkException {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName()
                        , RequestBody.create(null, file))
                .addFormDataPart("partner_id", "testoer")
                .build();

        Request request = new Request.Builder()
                .url(actionUrl)
                .post(requestBody)
                .build();

       //
        // OkHttpClient client = new OkHttpClient();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
            String jsonString = response.body().string();

            if(!response.isSuccessful()){
                throw new NetworkException("upload error code "+response);
            }else{
                return jsonString;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}