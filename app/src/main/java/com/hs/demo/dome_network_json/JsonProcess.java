package com.hs.demo.dome_network_json;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import Utils.App;
import Utils.HttpCallbackListener;
import Utils.HttpUtil;


public class JsonProcess extends ActionBarActivity {


    //定义变量
    public static final int SHOW_RESPONSE = 0;
    private Button btn_get_json;
    //private TextView responseText;


    //定义一个handler

    private Handler handler = new Handler() {


        public void handleMessage(Message msg) {
            // process incoming messages here

            switch (msg.what){
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    //responseText.setText(response);    //设置textview的值
            }
        }

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_process);



        //绑定监听事件
        btn_get_json = (Button) findViewById(R.id.btn_get_json);
        btn_get_json.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("debug", "点击button获取json数据");

                //sendHttpRequestWithHttpClient();

                //使用封装好的实体类发送数据
                String address = "http://10.10.20.145/get_data.json";
                Log.d("debug","调用封装好的实体类发送数据");
                HttpUtil.sendHttpRequest(address,new HttpCallbackListener() {

                    @Override
                    public void onFinish(String response) {
                        //在这里执行返回具体内容的逻辑
                        parseJSONWithGSON(response);
                    }

                    @Override
                    public void onError(Exception e) {
                        //在这里执行异常情况的处理
                        e.printStackTrace();
                    }
                });
            }
        });
    }


    //处理http请求
    //发送http请求 ，http client方式
    //将此函数封装成实体类
    //利用java的回调机制进行进一步的处理
    private void sendHttpRequestWithHttpClient(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Log.d("debug","开启新的线程发送请求");
                    HttpClient httpClient = new DefaultHttpClient() ;
                    //HttpGet httpGet = new HttpGet("http://www.baidu.com");
                    HttpGet httpGet = new HttpGet("http://10.10.20.145/get_data.json");  //获取本地apache服务器上的测试数据，使用的是真机调试
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    if (httpResponse.getStatusLine().getStatusCode() == 200){
                        //请求和响应成功了
                        HttpEntity httpEntity = httpResponse.getEntity();
                        String response = EntityUtils.toString(httpEntity, "utf-8");

                        //parseJSONWithJSONObject(response);  //处理json数据
                        parseJSONWithGSON(response);  //使用GSON处理json数据

                        //Message message = new Message();
                        //message.what = SHOW_RESPONSE;
                        //message.obj = response.toString();
                        //handler.sendMessage(message);

                    }



                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }




    //使用JSONObject处理json数据
    private void parseJSONWithJSONObject(String jsondata){
        Log.d("debug","处理json数据并打印到控制台  ----------->");
        try{
            JSONArray jsonArray = new JSONArray(jsondata);
            for (int i = 0; i< jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String version = jsonObject.getString("version");
                Log.d("debug", "id is " + id);
                Log.d("debug", "name is " + name);
                Log.d("debug", "version is " + version);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //使用GSON处理json数据
    private void parseJSONWithGSON(String jsondata){
        Log.d("debug","处理json数据,  GSON 并打印到控制台  ----------->");
        try{
            Gson gson = new Gson();
            List<App> appList = gson.fromJson(jsondata,new TypeToken<List<App>>(){}.getType());
            //foreach 的写法
            for (App item : appList){
                //遍历并打印相关的信息
                Log.d("debug", "id is " + item.getId());
                Log.d("debug", "name is " + item.getName());
                Log.d("debug", "version is " + item.getVersion());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_json_process, menu);
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
