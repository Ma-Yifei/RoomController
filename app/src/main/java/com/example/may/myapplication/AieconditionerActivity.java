package com.example.may.myapplication;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.may.myapplication.R;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class AieconditionerActivity extends AppCompatActivity {

    private Thread thread2;

    private Button btn_sw;
    private Button btn_fs;
    private Button btn_zr;
    private Button btn_zl;


    private TextView tv_xs;
    private Button btn_jw;
    private CheckBox btn_dy;
    private int dy = 0;
    private int lr = 0;
    private int fs = 0;
    private int degree = 25;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aieconditioner);


        tv_xs = findViewById(R.id.tv_xs);
        btn_sw = findViewById(R.id.btn_sw);
        btn_dy = findViewById(R.id.btn_dy);
        btn_jw = findViewById(R.id.btn_jw);
        btn_fs = findViewById(R.id.btn_fs);
        btn_zr = findViewById(R.id.btn_zr);
        btn_zl = findViewById(R.id.btn_zl);


        btn_dy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_dy.isChecked()){
                    tv_xs.setText(degree+" ℃");
                    btn_dy.setText("关");
                    dy = 1;
                }else{
                    tv_xs.setText(" ");
                    dy = 0;
                    btn_dy.setText("开");
                    degree = 25;
                }
                sendData();
            }
        });

        btn_sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                degree++;
                if (degree>30){
                    degree = 18;
                }
                tv_xs.setText(degree+" ℃");
                sendData();
            }
        });
        btn_jw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                degree--;
                if(degree<18){
                    degree = 30;
                }
                tv_xs.setText(degree+" ℃");
                sendData();
            }
        });
        btn_fs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fs++;
                if(fs>3){
                    fs = 0;
                }
                if(fs == 0){
                    btn_fs.setText("自动");
                }else if (fs == 1){
                    btn_fs.setText("高");
                }else if (fs == 2){
                    btn_fs.setText("中");
                }else if (fs == 3){
                    btn_fs.setText("低");
                }
                sendData();
            }
        });

        btn_zr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lr = 1;
                btn_zr.setTextColor(Color.rgb(102, 102, 102));
                btn_zl.setTextColor(Color.rgb(255, 255, 255));
                sendData();
            }
        });
        btn_zl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lr = 0;
                btn_zl.setTextColor(Color.rgb(102, 102, 102));
                btn_zr.setTextColor(Color.rgb(255, 255, 255));
                sendData();
            }
        });


    }

    public void sendData() {
        thread2 = new Thread(new Runnable() {

            @Override
            public void run() {
                DatagramSocket socket;
                try {
                    //
                    socket = new DatagramSocket(1945);
                    InetAddress serverAddress = InetAddress.getByName("192.168.137.1");
//                    byte data[] = new byte[]{(byte) 0xff, 0x01};
                    byte data[] = new byte[]{(byte)01,(byte)00,(byte) dy, (byte) lr, (byte) fs, (byte) degree};
                    DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, 10025);
                    socket.send(packet);
                    socket.close();
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
        thread2.start();
    }
}
