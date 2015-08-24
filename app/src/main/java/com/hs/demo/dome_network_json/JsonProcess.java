package com.hs.demo.dome_network_json;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hs.demo.dome_network_json.activities.Database;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import Utils.App;
import Utils.HttpCallbackListener;
import Utils.HttpUtil;
import Utils.MyDatabaseHelper;
import Utils.PersonID;


/*
*
* 发送http请求返回json数据
*
 */


public class JsonProcess extends ActionBarActivity implements View.OnClickListener {


    //定义变量
    public static final int SHOW_RESPONSE = 0;
    private Button btn_get_json;
    private  Button btn_get_id;
    private EditText input_id;   //身份账号输入框，获取身份证号码
    private TextView   tv_show_id_info;
    private MyDatabaseHelper dbHelper;
    private Button btn_search_history_info;

    //private TextView responseText;


    //定义一个handler

    private Handler handler = new Handler() {


        public void handleMessage(Message msg) {
            // process incoming messages here

            switch (msg.what){
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    //responseText.setText(response);    //设置textview的值
                    tv_show_id_info.setText(response);

            }
        }

    };




    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_search_history_info:
                Log.d("debug","查询历史信息---->");

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                //查询表中的所有的数据  Id_recorders
                String sql_str = "select * from Id_records";
                Cursor cursor = db.rawQuery(sql_str, null);

                if(cursor.moveToFirst()){
                    do {
                        //遍历cursor
                        String area = cursor.getString(cursor.getColumnIndex("area"));
                        String gender = cursor.getString(cursor.getColumnIndex("gender"));
                        String birth_date = cursor.getString(cursor.getColumnIndex("birth_date"));

                        Log.d("debug"," area:" +area );
                        Log.d("debug"," gender:" + gender);
                        Log.d("debug"," birth_date:" +birth_date );
                        //Log.d("debug"," :" + );

                    }while (cursor.moveToNext() );
                }

                cursor.close();
                break;

            default:
                break;

        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_process);


        input_id = (EditText) findViewById(R.id.input_id);


        btn_get_id = (Button) findViewById(R.id.btn_get_id);
        btn_search_history_info = (Button)findViewById(R.id.btn_search_history_info);

        tv_show_id_info = (TextView) findViewById(R.id.tv_show_id_info);

        dbHelper = new MyDatabaseHelper(this,"IdRecords.db",null,2);

        btn_search_history_info.setOnClickListener(this);

        btn_get_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("debug", "使用 聚合数据 API 点击button获取身份证json数据");
                Parameters params = new Parameters();

                /**
                 * 请求的方法 参数: 第一个参数 当前请求的context 第二个参数 接口id 第三个参数 接口请求的url 第四个参数 接口请求的方式
                 * 第五个参数 接口请求的参数,键值对com.thinkland.sdk.android.Parameters类型; 第六个参数
                 * 请求的回调方法,com.thinkland.sdk.android.DataCallBack;
                 *
                 */


                //获取用户输入的身份账号码
                String myId = input_id.getText().toString().trim();  //trim过滤首尾的空格

                params.add("cardno", myId);
                params.add("dtype", "json");
                JuheData.executeWithAPI(getApplicationContext(),38, "http://apis.juhe.cn/idcard/index", JuheData.GET, params, new DataCallBack() {


                    /**
                     * 请求成功时调用的方法 statusCode为http状态码,responseString    *为请求返回数据.
                     */


                    @Override
                    public void onSuccess(int statusCode, String responseString) {
                        // TODO Auto-generated method stub
                        //tv.append(responseString + "\n");
                        Log.d("debug","成功返回请求----->");

                        Log.d("debug",responseString);

                        //Toast.makeText(getApplicationContext(), responseString,Toast.LENGTH_LONG).show();

                        parseJSONTOPersonID(responseString);
                    }

                    /**
                     * 请求完成时调用的方法,无论成功或者失败都会调用.
                     */
                    @Override
                    public void onFinish() {
                        // TODO Auto-generated method stub
                        //Toast.makeText(getApplicationContext(), "finish",Toast.LENGTH_SHORT).show();
                    }
                    /**
                     * 请求失败时调用的方法,statusCode为http状态码,throwable为捕获到的异常
                     * statusCode:30002 没有检测到当前网络. 30003 没有进行初始化. 0
                     * 未明异常,具体查看Throwable信息. 其他异常请参照http状态码.
                     */
                    @Override
                    public void onFailure(int statusCode,String responseString, Throwable throwable) {
                        // TODO Auto-generated method stub
                        //tv.append(throwable.getMessage() + "\n");

                        Log.d("debug","失败  返回请求----->");


                        Log.d("debug",throwable.getMessage());
                        String errMessage = "查询失败，详情：" + throwable.getMessage();
                        Toast.makeText(getApplicationContext(), errMessage,Toast.LENGTH_LONG).show();
                    }
                });







            }
        });







        //绑定监听事件
        btn_get_json = (Button) findViewById(R.id.btn_get_json);
        btn_get_json.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("debug", "点击button获取百度 的 ---json数据");

                //sendHttpRequestWithHttpClient();

                //查询身份证信息  ，api 来自百度 API STORE
                String httpUrl = "http://apis.baidu.com/apistore/idservice/id";
                String httpArg = "id=130625198912180418";
                httpUrl = httpUrl + "?" + httpArg;


                HttpUtil.sendHttpRequest(httpUrl,new HttpCallbackListener() {

                    @Override
                    public void onFinish(String response) {
                        //在这里执行返回具体内容的逻辑
                        parseJSONWithGSON(response);
                        Log.d("debug","msg from baidu is : "   + response);
                    }

                    @Override
                    public void onError(Exception e) {
                        //在这里执行异常情况的处理
                        e.printStackTrace();
                    }
                });


                //String jsonResult = request(httpUrl, httpArg);
                //System.out.println(jsonResult);
                //Log.d("debug","来自百度的查询结果:  "  + jsonResult);



                //=================


                //使用封装好的实体类发送数据
                /*
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
                */
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


    //处理身份证号信息
    //将字符串转换成数组，元素类型为PersonID
    private void parseJSONTOPersonID(String jsondata){
        Log.d("debug","-----  处理身份证信息  json  ----------->");

        try{

            //将JSON文本解析为对象
            JSONTokener jsonParser = new JSONTokener(jsondata);
            JSONObject personID = (JSONObject) jsonParser.nextValue();

            //从json对象中回去信息
            JSONObject  person = personID.getJSONObject("result");

            Log.d("debug",person.getString("area"));

            String str_search_result = "地址：    " + person.getString("area") + "\n";
            str_search_result +=       "性别:     "  + person.getString("sex") + "\n";
            str_search_result +=       "出生日期: "  + person.getString("birthday") + "\n";


            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            //开始组装数据
            Log.d("debug","将数据插入sqlite数据库");
            String id_num = input_id.getText().toString().trim();
            Log.d("debug","id nums" + id_num);

            values.put("id_num",id_num);
            values.put("gender",person.getString("sex"));
            values.put("area", person.getString("area") );
            values.put("birth_date", person.getString("birthday") );
            Log.d("debug","数据插入 完成");
            db.insert("Id_records",null,values);

            values.clear();




            Message message = new Message();
            message.what = SHOW_RESPONSE;
            message.obj = str_search_result;
            handler.sendMessage(message);   //发送消息 更新UI的显示效果

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
