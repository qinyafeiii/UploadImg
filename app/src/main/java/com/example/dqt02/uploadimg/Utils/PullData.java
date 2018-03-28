package com.example.dqt02.uploadimg.Utils;


import com.example.dqt02.uploadimg.Models.ImageModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/3/23.
 */

public class PullData {
    public static List<ImageModel> pullListData(JSONObject jsonObject) throws JSONException {
        List<ImageModel>  datas = new ArrayList<>();
        JSONArray jsonArray =  jsonObject.optJSONArray("tops");
        ImageModel imageModel = null;
        if(jsonArray != null && jsonArray.length() >0){
            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);
                if(item.get("image") != null){
                    imageModel = (ImageModel)getInstanceByJson(item.getJSONObject("image"));
                    datas.add(imageModel);
                }
                if(item.get("similarity") != null){
                    imageModel.similarity = item.optString("similarity");
                }
            }
        }
        for(int i=0; i<datas.size(); i++){//表示n次排序过程。
            for(int j=i+1; j<datas.size(); j++){
                if(!firstIsBig(datas.get(i).similarity,datas.get(j).similarity)){//前面的数字大于后面的数字就交换
                    imageModel = datas.get(j);
                    datas.remove(j);
                    datas.add(j,datas.get(i));
                    datas.remove(i);
                    datas.add(i,imageModel);
                }
            }
        }
        return datas;
    }


    /**
     * 比较两个相似度的大小，true 的时候 a 大或者等于 b
     * @param a
     * @param b
     * @return
     */
    public static boolean firstIsBig(String a, String b) {
        //精确表示
        BigDecimal dataA = new BigDecimal(a);
        BigDecimal dataB = new BigDecimal(b);
        int i = dataA.compareTo(dataB);
        return i > -1? true : false;
    }


    public  static ImageModel getInstanceByJson(JSONObject image) throws JSONException {
        ImageModel imageModel2 = new ImageModel();
        if(image.get("id") != null){
            imageModel2.id = image.optString("id");
        }
        if(image.get("g") != null){
            imageModel2.g = image.optString("g");
        }
        if(image.get("partner_id") != null){
            imageModel2.partner_id = image.optString("partner_id");
        }
        if(image.get("time") != null){
            imageModel2.partner_id = image.optString("time");
        }
        if(image.get("b") != null){
            imageModel2.b = image.optString("b");
        }
        if(image.get("r") != null){
            imageModel2.r = image.optString("r").toString();
        }
        if(image.get("hashcode") != null){
            imageModel2.hashcode = image.optString("hashcode");
        }
        if(image.get("r9") != null){
            imageModel2.r9 = image.optString("r9");
        }
        if(image.get("url") != null){
            imageModel2.url = image.optString("url");
        }
        if(image.get("b9") != null){
            imageModel2.r9 = image.optString("b9");
        }
        return imageModel2;
    }
}
