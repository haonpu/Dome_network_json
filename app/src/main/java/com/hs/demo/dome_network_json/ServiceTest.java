package com.hs.demo.dome_network_json;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ServiceTest extends ActionBarActivity implements View.OnClickListener {


    public static final  int UPDATE_TEXT = 1;
    private TextView tv_change;
    private Button btn_change_text;

    private Button btn_service_start,btn_service_stop ,  btn_bind_service,btn_unbind_service,btn_intent_service;

    private MyService.DownloadBinder downloadBinder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            downloadBinder = (MyService.DownloadBinder) iBinder;
            downloadBinder.startDownload();
            downloadBinder.getProgress();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case UPDATE_TEXT:
                    // UI Operation
                    tv_change.setText("下载开始...");
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        setContentView(R.layout.activity_service_test);


        btn_change_text = (Button) findViewById(R.id.btn_change_text);

        tv_change = (TextView) findViewById(R.id.tv_show_text);
        btn_service_start = (Button) findViewById(R.id.btn_service_start);
        btn_service_stop = (Button) findViewById(R.id.btn_service_stop);
        btn_bind_service = (Button) findViewById(R.id.btn_bind_service);
        btn_unbind_service = (Button) findViewById(R.id.btn_unbind_service);
        btn_intent_service = (Button) findViewById(R.id.btn_intent_service);


        btn_change_text.setOnClickListener(this);
        btn_service_start.setOnClickListener(this);
        btn_service_stop.setOnClickListener(this);
        btn_bind_service.setOnClickListener(this);
        btn_unbind_service.setOnClickListener(this);
        btn_intent_service.setOnClickListener(this);



    }



    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btn_change_text:

                Log.d("debug","点击改变文字的按钮--->");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = UPDATE_TEXT;
                        handler.sendMessage(message);  //将message对象发送出去
                    }
                }).start();
                break;
            case R.id.btn_service_start:
                Log.d("debug","点击启动service");
                Intent startIntent = new Intent(ServiceTest.this, MyService.class);
                startService(startIntent);
                break;
            case R.id.btn_service_stop:
                Log.d("debug","click stop service ");
                Intent stopIntent = new Intent(ServiceTest.this, MyService.class);
                stopService(stopIntent);  // stop service
                break;

            case R.id.btn_bind_service:
                Intent bindIntent = new Intent(this,MyService.class);
                bindService(bindIntent,connection,BIND_AUTO_CREATE); //绑定服务


                break;

            case R.id.btn_unbind_service:
                unbindService(connection); //解绑 服务

                break;
            case R.id.btn_intent_service:
                Log.d("debug","click intent service");
                Log.d("debug","Main Thread is : " + Thread.currentThread().getId());
                Intent intentService = new Intent(this,MyIntentService.class);   //intentService执行完之后就会自动销毁
                startService(intentService);
                break;
            default:
                break;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_service_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
