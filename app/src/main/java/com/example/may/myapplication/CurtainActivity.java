package com.example.may.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class CurtainActivity extends AppCompatActivity {
    private Thread thread2;
    private Button btn_qk;
    private Button btn_bk;
    private Button btn_qb;
    private int degree;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curtain);

        btn_qk = findViewById(R.id.btn_qk);
        btn_bk = findViewById(R.id.btn_bk);
        btn_qb = findViewById(R.id.btn_qb);

        btn_qk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                degree = 100;
                sendData();
            }
        });
        btn_bk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                degree = 50;
                sendData();
            }
        });
        btn_qb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                degree = 0;
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
                    socket = new DatagramSocket(1955);
                    InetAddress serverAddress = InetAddress.getByName("192.168.137.1");
//                    byte data[] = new byte[]{(byte) 0xff, 0x01};
                    byte data[] = new byte[]{(byte)02,(byte)00,(byte) degree};
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
