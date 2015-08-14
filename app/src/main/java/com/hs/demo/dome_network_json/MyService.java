package com.hs.demo.dome_network_json;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {


    private DownloadBinder mBinder = new DownloadBinder();


    public MyService() {
    }


    class  DownloadBinder extends Binder{
        public  void startDownload(){
            Log.d("debug","star download --->");
        }

        public int getProgress(){
            Log.d("debug","get progress executed --->");
            return 0;
        }

    }






    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        return mBinder;
        //throw new UnsupportedOperationException("Not yet implemented");

    }


    @Override
    public void onCreate() {  //服务创建
        super.onCreate();
        Log.d("debug","onCreate created");
        Notification notification = new Notification(R.drawable.app_notifition_logo,"Notification comes",System.currentTimeMillis());
        Intent notificationIntent = new Intent(this,Main.class);  //第二个参数表示的是点击通知时，返回的Activity
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);
        notification.setLatestEventInfo(this,"this is title","this is content",pendingIntent);
        startForeground(1,notification);
    }



    //服务启动
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("debug","onStartCommand created");
        return super.onStartCommand(intent, flags, startId);
    }


    //服务销毁
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("debug","onDestroy created");
    }
}
