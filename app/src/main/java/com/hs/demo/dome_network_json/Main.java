package com.hs.demo.dome_network_json;



import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;   //去除标题栏引入的类
import android.support.v7.app.ActionBarActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class Main extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去除标题栏
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();
        Log.d("debug","create main activity---->");

        setContentView(R.layout.main);


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
