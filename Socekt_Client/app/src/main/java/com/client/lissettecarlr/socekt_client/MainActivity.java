package com.client.lissettecarlr.socekt_client;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    EditText ip;
    EditText editText;
    TextView text;
    TextView statue;
    public boolean flag =false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ip = (EditText)findViewById(R.id.textID);
        editText =(EditText)findViewById(R.id.textSend);
        text =(TextView)findViewById(R.id.textShow);
        Button connent = (Button)findViewById(R.id.buttonCom);
        Button Send = (Button)findViewById(R.id.buttonSend);
        Button close =(Button)findViewById(R.id.close);
        statue = (TextView)findViewById(R.id.textView_status);

        text.setMovementMethod(ScrollingMovementMethod.getInstance()) ;

        connent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect();
            }
        });

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(flag)
                send();
            else
                Toast.makeText(getApplicationContext(), "没有需要发送的类容", Toast.LENGTH_SHORT).show();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    socket.close();
                    flag = false;
                    statue.setText("连接断开");
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "关闭失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        text.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                text.setText("");
                return false;
            }
        });
    }
//---------------------------------------------------------
    Socket socket = null;
    BufferedWriter writer = null;
    BufferedReader reader = null;

    public void connect() {

            AsyncReadTask asyncReadTask = new AsyncReadTask();
        asyncReadTask.execute(ip.getText().toString().trim());
    }


    private final class AsyncReadTask extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... params) {  //由线程池开启新线程执行该函数中的内容
            try {
                socket = new Socket(params[0], 23456);
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                publishProgress("与服务器连接成功");
                flag = true;
            } catch (UnknownHostException e1) {
                Toast.makeText(getApplicationContext(), "无法建立链接", Toast.LENGTH_SHORT).show();
                flag =false ;
            } catch (IOException e1) {
                Toast.makeText(getApplicationContext(), "无法建立链接", Toast.LENGTH_SHORT).show();
                flag =false;
            }
            try {
                String line;
                while ((line = reader.readLine())!= null) {
                    publishProgress(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {  //此处UI更新

        }

        @Override
        protected void onProgressUpdate(String... values) {    //数据由doInBackground返回过来
            if (values[0].equals("与服务器连接成功")) {
            }
            text.append("获得消息："+values[0]+"\n");
            super.onProgressUpdate(values);
        }
    }

    public void send() {
        try {
            text.append("发送消息："+editText.getText().toString()+"\n");
            writer.write(editText.getText().toString()+"\n");
            writer.flush();
            editText.setText("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
