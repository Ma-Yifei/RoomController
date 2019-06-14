package com.example.may.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SocketActivity extends AppCompatActivity {
    private CheckBox sw;
    public int i = 0;

    private Thread thread2;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        iv = findViewById(R.id.iv);
        iv.setImageResource(R.mipmap.socket0);
        sw = findViewById(R.id.sw);
        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sw.isChecked()){
                    sw.setText("关");
                    iv.setImageResource(R.mipmap.socket1);
                    i = 1;
                }else{
                    sw.setText("开");
                    iv.setImageResource(R.mipmap.socket0);
                    i = 0;
                }
                sendSwitch();
            }
        });

    }
    public void sendSwitch() {
        thread2 = new Thread(new Runnable() {

            @Override
            public void run() {
                DatagramSocket socket;
                try {
                    //
                    socket = new DatagramSocket(1965);
                    InetAddress serverAddress = InetAddress.getByName("192.168.137.1");
//                    byte data[] = new byte[]{(byte) 0xff, 0x01};
                    byte data[] = new byte[]{(byte)04,(byte)00,(byte) i};
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
