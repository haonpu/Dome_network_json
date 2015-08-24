package com.hs.demo.dome_network_json;



import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;   //去除标题栏引入的类
import android.support.v7.app.ActionBarActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;

import com.hs.demo.dome_network_json.activities.Database;


public class Main extends ActionBarActivity implements View.OnClickListener{

    private  Button btn_goto_service,btn_goto_save_data,btn_sqlite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去除标题栏
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();
        Log.d("debug","create main activity---->");

        setContentView(R.layout.main);


        //打开程序时，启动定时任务
        Intent intent = new Intent(this,LongRunningService.class);

        startService(intent);


        btn_goto_save_data = (Button) findViewById(R.id.btn_goto_save_data);
        btn_sqlite = (Button) findViewById(R.id.btn_sqlite);

        btn_goto_save_data.setOnClickListener(this);
        btn_sqlite.setOnClickListener(this);

        //获取button控件
        Button btn_goto_webview = (Button) findViewById(R.id.btn_goto_webview);
        //为按钮增加监听事件
        btn_goto_webview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  =  new Intent(Main.this,WebViewUtil.class);
                startActivity(intent);
            }
        });

        Button btn_goto_http = (Button) findViewById(R.id.btn_goto_http);
        btn_goto_http.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main.this,HttpConnectionUtil.class);
                startActivity(intent);
            }
        });

        Button btn_goto_json = (Button) findViewById(R.id.btn_goto_json);
        btn_goto_json.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(Main.this,JsonProcess.class);
                startActivity(intent);
            }
        });

        btn_goto_service = (Button) findViewById(R.id.btn_goto_service);
        btn_goto_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到 ServiceTest页面
                Log.d("debug","goto  service test activity");
                Intent intent = new Intent(Main.this,ServiceTest.class);
                startActivity(intent);

            }
        });


    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_goto_save_data:
                Log.d("debug","click the goto save data button---->");
                Intent intent = new Intent(Main.this,FileSave.class);
                startActivity(intent);
                break;
            case R.id.btn_sqlite:

                Intent intent1 = new Intent(Main.this,Database.class);
                startActivity(intent1);

                break;
            default:
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
