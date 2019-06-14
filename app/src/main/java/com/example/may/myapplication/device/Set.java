package com.example.may.myapplication.device;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.may.myapplication.MainActivity;
import com.example.may.myapplication.MyDatabaseHelper;
import com.example.may.myapplication.R;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class Set extends Device  {
    private static Set set = null;
    public static Set getInstance() {
        if (set == null) {
            set = new Set();
        }
        return set;
    }// Set.getInxxxx

    private String ht;
    private String lt;
    private SQLiteOpenHelper dbHelper;
    private Thread thread3;
    private int dy;
    private int fs;
    private int lr;
    private int degree;
    @Override
    public int getImage() {
        return R.mipmap.setting;
    }



    public void setHt(String ht) {
        this.ht = ht;
    }


    public void setLt(String lt) {
        this.lt = lt;
    }

    public void adjust(Context context){
        dbHelper = new MyDatabaseHelper(context,"data.db",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("TH", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
        do {
            double t_content = cursor.getDouble(cursor.getColumnIndex("t"));


            double h_t = Double.parseDouble(ht);
            double l_t = Double.parseDouble(lt);
            if(h_t <t_content){
                int dy =1;
                int lr = 0;
                int fs = 0;
                double degree = h_t;
                sendData1();
            } else if(t_content<l_t){
                int dy =1;
                int lr = 1;
                int fs = 0;
                double degree = l_t;
                sendData1();
            }
        } while (cursor.moveToNext());
    }
                cursor.close();
}
    public void sendData1() {
        thread3 = new Thread(new Runnable() {

            @Override
            public void run() {
                DatagramSocket socket;
                try {
                    //
                    socket = new DatagramSocket(1905);
                    InetAddress serverAddress = InetAddress.getByName("192.168.1.158");
//                    byte data[] = new byte[]{(byte) 0xff, 0x01};
                    byte data[] = new byte[]{(byte) dy, (byte) lr, (byte) fs, (byte) degree};
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
        thread3.start();
    }
}
