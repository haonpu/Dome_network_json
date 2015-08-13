package com.hs.demo.dome_network_json;

import android.app.Application;
import android.content.Context;

import com.thinkland.sdk.android.JuheSDKInitializer;

/**
 * Created by User on 2015/8/12.
 * author by haoshuo
 * 在调用接口之前需要初始化聚合数据SDK,只需要初始化一次即可。需要在Application中调用初始化方法
 */
public class MyApplication extends Application{

    //private static Context myContext;

    @Override
    public void onCreate() {
        super.onCreate();
        //myContext = getApplicationContext();
        JuheSDKInitializer.initialize(getApplicationContext());
        //SDKInitializer.initialize(getApplicationContext());
    }

    //public static Context getMyContext(){
    //    return myContext;
    //}

}
