package com.client.lissettecarlr.socekt_client;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    EditText ip;
    EditText editText;
    TextView text;
    private boolean con_flag =false ; //连接状态标识位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ip = (EditText)findViewById(R.id.textID);
        editText =(EditText)findViewById(R.id.textSend);
        text =(TextView)findViewById(R.id.textShow);
        Button connent = (Button)findViewById(R.id.buttonCom);
        Button Send = (Button)findViewById(R.id.buttonSend);

        connent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                connect();
                con_flag =true ;
            }
        });

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(con_flag)
                send ();
                else
                    Toast.makeText(getApplicationContext(), "没有建立连接", Toast.LENGTH_SHORT).show();
            }
        });


    }
//---------------------------------------------------------
    Socket socket = null;
    BufferedWriter writer = null;
    BufferedReader reader = null;

    public void connect() {

        AsyncTask<Void, String, Void> read = new AsyncTask<Void, String, Void>() {

            @Override
            protected Void doInBackground(Void... arg0) {
                try {
                    socket = new Socket("192.168.56.1", 23456);
                    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    publishProgress("@success");
                } catch (UnknownHostException e1) {
                    Toast.makeText(getApplicationContext(), "无法建立链接", Toast.LENGTH_SHORT).show();
                } catch (IOException e1) {
                    Toast.makeText(getApplicationContext(), "无法建立链接", Toast.LENGTH_SHORT).show();
                }
                try {
                    String line;
                    while ((line = reader.readLine())!= null) {
                        publishProgress(line);
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                if (values[0].equals("@success")) {
                    Toast.makeText(getApplicationContext(), "链接成功！", Toast.LENGTH_SHORT).show();
                }
                text.append("别人说："+values[0]+"\n");
                super.onProgressUpdate(values);
            }
        };
        read.execute();

    }


    public void send() {

        if(!editText.getText().toString().equals("")) {
            try {
                text.append("我说：" + editText.getText().toString() + "\n");
                writer.write(editText.getText().toString() + "\n");
                writer.flush();
                editText.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            Toast.makeText(getApplicationContext(), "没有需要发送的文字！", Toast.LENGTH_SHORT).show();
    }


}
