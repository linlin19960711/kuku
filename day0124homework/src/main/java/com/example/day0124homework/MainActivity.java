package com.example.day0124homework;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void doGetRequest(View view){
        HashMap<String,String > hashMap=new HashMap<>();
        hashMap.put("key","22ecf6c32440e");
        OKhtttpUtil.getInstance().doGet("http://apicloud.mob.com/v1/weather/type",hashMap, new OKhtttpUtil.OnResponseListener() {
            @Override
            public void onSuccess(String result) {
                Log.i("t", "doGet onSuccess result=" + result);
            }

            @Override
            public void onFailure(String result) {
                Log.i("t", "doGet onFailure result=" + result);
            }
        });
    }
    public void doPostRequest(View view){
     HashMap<String,String> han=new HashMap<>();
        han.put("mobile", "17611200379");
        han.put("password", "123456");
        OKhtttpUtil.getInstance().dopost("http://120.27.23.105/user/login",han, new OKhtttpUtil.OnResponseListener() {
            @Override
            public void onSuccess(String result) {
                Log.i("t", "doPost onSuccess result=" + result);
            }

            @Override
            public void onFailure(String result) {
                Log.i("t", "doPost onFailure result=" + result);
            }
        });
    }
    public void doFileUpload(View view){
    String uid="11972";
        String filepath= Environment.getExternalStorageDirectory().getPath()+"/"+"img.png";
        File file=new File(filepath);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("uid",uid);
        hashMap.put("file",file);
        OKhtttpUtil.getInstance().doFileUplaod("https://www.zhaoapi.cn/file/upload",hashMap, new OKhtttpUtil.OnResponseListener() {
            @Override
            public void onSuccess(String result) {
                Log.i("t", "doFileUpload onSuccess result=" + result);
            }

            @Override
            public void onFailure(String result) {
                Log.i("t", "doFileUpload onSuccess result=" + result);
            }
        });
    }
    public void doFileDownload(View view){

    }
}
