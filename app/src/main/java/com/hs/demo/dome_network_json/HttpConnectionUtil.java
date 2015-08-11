package com.hs.demo.dome_network_json;

import android.os.Handler;
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

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;





/*
 *
 * 手动发送http请求
 * HttpURLConnection  的方式
 */

public class HttpConnectionUtil extends ActionBarActivity {



    //定义变量
    public static final int SHOW_RESPONSE = 0;
    private Button sendRequest,btn_sentRequest;
    private TextView responseText;





    //定义一个handler

    private Handler handler = new Handler() {


        public void handleMessage(Message msg) {
            // process incoming messages here

            switch (msg.what){
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    responseText.setText(response);    //设置textview的值
            }
        }

    };






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();
        Log.d("debug", "create http url connection activity---->");

        setContentView(R.layout.activity_http_connection_util);

        sendRequest = (Button) findViewById(R.id.btn_send_request);
        responseText = (TextView) findViewById(R.id.txt_response_text);
        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("debug","click the button 发送http请求");
                sendRequestWithHttpURLConnection();

            }
        });


        //绑定监听事件
        btn_sentRequest = (Button) findViewById(R.id.btn_send_request_http_client);
        btn_sentRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("debug","click the button 发送http请求-----httpClient");
                Log.d("debug","btn send request with http clients");
                sendHttpRequestWithHttpClient();
            }
        });
    }



    //发送http请求 ，http client方式
    private void sendHttpRequestWithHttpClient(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    HttpClient httpClient = new DefaultHttpClient() ;
                    HttpGet httpGet = new HttpGet("http://www.baidu.com");
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    if (httpResponse.getStatusLine().getStatusCode() == 200){
                        //請求和相应成功了
                        HttpEntity httpEntity = httpResponse.getEntity();
                        String response = EntityUtils.toString(httpEntity,"utf-8");
                        Message message = new Message();

                        message.what = SHOW_RESPONSE;
                        message.obj = response.toString();
                        handler.sendMessage(message);

                    }



                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }




    //发送http请求
    private void sendRequestWithHttpURLConnection(){
        //开启线程发送网络请求
        //开启一个子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{


                    URL url = new URL("http://www.baidu.com");
                    connection = (HttpURLConnection) url.openConnection();
                    // set connection
                    connection.setRequestMethod("GET");  //get只是获取服务器的数据，post的话可以向服务器发送数据
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();

                    //对获取到的输入流进行读取
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        response.append(line);
                    }

                    //子线程中无法对UI进行操作
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;

                    //将服务器返回的结果放到Message中
                    message.obj = response.toString();
                    handler.sendMessage(message);



                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (connection != null){
                        connection.disconnect(); //将http连接关闭掉
                    }
                }
            }
        }).start();
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_http_connection_util, menu);
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
