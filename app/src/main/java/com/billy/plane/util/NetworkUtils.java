package com.billy.plane.util;

import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkUtils {

    private static OkHttpClient client  = new OkHttpClient();
    private static Gson gson = new Gson();
    public static <T> void getMethod(String url,final Class<T> cls,final INetCallback<T> callback)
    {

        final Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    //回调的方法执行在子线程。
                    callback.onSuccess(gson.fromJson( response.body().string(),cls));
                }
            }
        });


    }

    public static <T> void postMethod(String url, HashMap<String,String> map, final Class<? extends T> cls, final INetCallback<T> callback){
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        for (String key:map.keySet())
        {
            formBody.add(key,map.get(key));
        }
        final Request request = new Request.Builder()//创建Request 对象。
                .url(url)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.d("Network","failure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){

                    String str = response.body().string();
                    Log.d("TAG", str);
                    callback.onSuccess(gson.fromJson(str,cls));
                }
            }
        });
    }

    public static <T> void upLoadImg(String url, String uid ,String imgPath, final Class<T> cls, final INetCallback<T> callback){
        final String imageType = "multipart/form-data";
        File file = new File(imgPath);
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("img",file.getName(),fileBody)
                .addFormDataPart("uid", uid)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newBuilder().readTimeout(10000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d("Timeout","超时超时");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    callback.onSuccess(gson.fromJson(response.body().string(),cls));
                }
            }
        });


    }

}
