package com.hs.demo.dome_network_json;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class FileSave extends ActionBarActivity implements View.OnClickListener{


    private EditText edit_input ;
    private Button btn_shared_preference,btn_restore_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_save);

        edit_input = (EditText) findViewById(R.id.edit_input);
        //edit_input.setOnClickListener(this);
        btn_shared_preference = (Button) findViewById(R.id.btn_use_shared_preferenced);

        btn_restore_data = (Button) findViewById(R.id.btn_restore_data);
        setListeners();


    }

    private void setListeners(){
        btn_shared_preference.setOnClickListener(this);
        btn_restore_data.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit_input:
                Log.d("debug","click the onClick button--->");
                break;
            case R.id.btn_use_shared_preferenced:
                Log.d("debug","click the shared preference button--->");
                //将数据简单的保存到SharedPreferences文件中
                SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                editor.putString("name","haoshuo");
                editor.putInt("age",25);
                editor.putBoolean("married",false);

                editor.commit();
                break;

            case R.id.btn_restore_data:
                SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
                String name = pref.getString("name","");
                int age = pref.getInt("age",0);
                boolean married = pref.getBoolean("married",false);
                Log.d("debug","显示获取到的结果==============>  ");
                Log.d("debug","name is : " + name);
                Log.d("debug","age is " + age);
                Log.d("debug","married is " + married);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("debug","页面即将销毁   onDestroy called---->");
        String inputText = edit_input.getText().toString();
        save(inputText);  //保存数据到本地的文件
    }



    //保存文本到文件的函数
    public void save(String inputText){
        Log.d("debug","enter save file func");
        FileOutputStream out = null;
        BufferedWriter writer = null;

        try{
            out = openFileOutput("mydata", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));  //构造流
            writer.write(inputText);   //写入数据
            Log.d("debug","write data---->  ");
            String dir = getBaseContext().getFilesDir().toString();
            Log.d("debug",dir);

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if (writer != null){
                    writer.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file_save, menu);
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
