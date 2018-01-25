package com.example.day0124homework;

import android.os.Handler;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lenovo on 2018/1/24.
 */

public class OKhtttpUtil {
    //volatile在多线程中保持和OKHttpUtil中保持一致性
    private static volatile OKhtttpUtil mInstance;
    private final OkHttpClient mOkHttpClient;
    private final Handler mHanlder;
    public OKhtttpUtil() {
        mOkHttpClient = new OkHttpClient();
        mHanlder=new Handler();
    }
    //同步代码块进行双重认证，在多线程中表示对象唯一性
    public static OKhtttpUtil getInstance(){
    if(null==mInstance){
     synchronized (OKhtttpUtil.class){
      if(null==mInstance){
         mInstance=new OKhtttpUtil();
      }
     }
    }
     return mInstance;
    }
    public void doGet(String url, HashMap<String,String> parmMap,final OnResponseListener listener){
     StringBuffer sb=new StringBuffer();
     //加上字符串
        sb.append(url);
        //字符串不存在？拼接上？字符串
        if(sb.lastIndexOf("?")==-1){
         sb.append("?");
        }
        //通过循环将参数拼接起来
        for (Map.Entry<String,String> entry:parmMap.entrySet()){
        sb.append(entry.getKey())
                .append("=")
                .append(entry.getValue())
                .append("&");
        }
        sb.deleteCharAt(sb.length()-1);
        Request re=new Request.Builder().url(sb.toString()).build();
        mOkHttpClient.newCall(re).enqueue(new Callback() {
            @Override
            public void onFailure(Call call,final IOException e) {
                //子线程转换成主线程
                mHanlder.post(new Runnable() {
                    @Override
                    public void run() {
                        if(null!=listener){
                            listener.onFailure(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
             mHanlder.post(new Runnable() {
                 @Override
                 public void run() {
                     if(response.isSuccessful()){
                         try {
                             String string = response.body().string();
                             //将数据设置到接口方法中
                             if(null!=listener){
                                 listener.onSuccess(string);
                             }
                         } catch (IOException e) {
                             e.printStackTrace();
                         }
                     }
                 }
             });
            }
        });
    }
    public void dopost(String url,HashMap<String,String> parmMap,final OnResponseListener listener){
        FormBody.Builder fo=new FormBody.Builder();
        //通过循环将参数封装到form表单中
        for(Map.Entry<String,String> entry:parmMap.entrySet()){
        fo.add(entry.getKey(),entry.getValue());
        }
        Request re=new Request.Builder().post(fo.build()).url(url).build();
        mOkHttpClient.newCall(re).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHanlder.post(new Runnable() {
                    @Override
                    public void run() {
                        if(null!=listener){
                        listener.onFailure(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
              mHanlder.post(new Runnable() {
                  @Override
                  public void run() {
                      if(response.isSuccessful()){
                          try {
                              String string = response.body().string();
                              if(null!=listener){
                                 listener.onSuccess(string);
                              }
                          } catch (IOException e) {
                              e.printStackTrace();
                          }
                      }
                  }
              });
            }
        });
    }
    public void doFileUplaod(String url,HashMap<String,Object> parmMap, final OnResponseListener listener ){
        MultipartBody.Builder mu=new MultipartBody.Builder();
        mu.setType(MultipartBody.FORM);
        //循环设置参数
        for(Map.Entry<String,Object> entry:parmMap.entrySet()){
         if(entry.getValue() instanceof String){
          mu.addFormDataPart(entry.getKey(),((String) entry.getValue()).toString());
         }else if(entry.getValue() instanceof File){
           File file=(File) entry.getValue();
             //将参数封装到表单体中
             mu.addFormDataPart(entry.getKey(),file.getName(),MultipartBody.create(MultipartBody.FORM,file));

         }
            Request re=new Request.Builder().post(mu.build()).url(url).build();
            mOkHttpClient.newCall(re).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    mHanlder.post(new Runnable() {
                        @Override
                        public void run() {
                            if(null!=listener){
                               listener.onFailure(e.getMessage());
                            }
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String string = response.body().string();
                    if(null!=listener){
                        listener.onSuccess(string);
                    }
                }
                }
            });
        }
    }
    interface OnResponseListener {
        public void onSuccess(String result);
        public void onFailure(String result);
    }
}
