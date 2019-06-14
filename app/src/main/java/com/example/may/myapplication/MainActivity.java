package com.example.may.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.may.myapplication.device.Aieconditioner;
import com.example.may.myapplication.device.Co2;
import com.example.may.myapplication.device.Curtain;
import com.example.may.myapplication.device.Device;
import com.example.may.myapplication.device.Hcho;
import com.example.may.myapplication.device.Ill;
import com.example.may.myapplication.device.Pm;

import com.example.may.myapplication.device.Set;
import com.example.may.myapplication.device.Socket;
import com.example.may.myapplication.device.Switch;
import com.example.may.myapplication.device.TH;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mReclerAdapter;
    private List<Device> deviceList = new ArrayList<Device>();
    private Thread thread;
//    private Thread thread2;
//    private Thread thread3;
    public MyDatabaseHelper dbHelper;

    public int i = 2;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast.makeText(MainActivity.this, "超过设定的最高室内温度", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(MainActivity.this, "低于设定的最低室内温度", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new MyDatabaseHelper((Context) this,"data.db",null,1);
        dbHelper.getWritableDatabase();


        loadDatas();
        initViews();

        MyThread thread = new MyThread();
        thread.start();

        download();
//        send();
//        adjust();
//        Set.getInstance().adjust(this);



    }

    private void loadDatas() {

        Co2 co2 = new Co2();
        co2.setName("二氧化碳");
        deviceList.add(co2);
        Curtain curtain = new Curtain();
        curtain.setName("窗帘");
        deviceList.add(curtain);
        Hcho hcho = new Hcho();
        hcho.setName("甲醛");
        deviceList.add(hcho);
        Ill ill = new Ill();
        ill.setName("光照度");
        deviceList.add(ill);
        Pm pm = new Pm();
        pm.setName("PM2.5");
        deviceList.add(pm);
        Socket socket = new Socket();
        socket.setName("插座");
        deviceList.add(socket);
        Switch switch1 = new Switch();
        switch1.setName("开关");
        deviceList.add(switch1);
        Aieconditioner aieconditioner = new Aieconditioner();
        aieconditioner.setName("空调");
        deviceList.add(aieconditioner);
        TH th = new TH();
        th.setName("温湿度");
        deviceList.add(th);
        Set set = new Set();
        set.setName("智能环境");
        deviceList.add(set);

    }

    private void initViews() {
        //获得布局的RecyclerView控件
        mRecyclerView = (RecyclerView) findViewById(R.id.demo_recyclerView);
        //设置布局显示方式，这里使用GridLayoutManager
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        //设置添加删除Item的时候的动画效果
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //初始化适配器
        mReclerAdapter = new RecyclerAdapter(this, deviceList);
        //设置适配器
        mRecyclerView.setAdapter(mReclerAdapter);
    }




//    @SuppressLint("HandlerLeak")
//    private Handler handler = new Handler() {
//
//        public void handlerMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 0:
//                    // txt.setText(msg.obj);
//                    break;
//            }
//        }
//
//    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        thread.interrupt();
        Log.d("Mainactivity","onDestroy");
    }

    public void download() {

        thread = new Thread() {

            @Override
            public void run() {
                DatagramSocket socket;
                String result = null;
                try {
                    socket = new DatagramSocket(1990);
                    byte data[] = new byte[4 * 1024];
                    DatagramPacket packet = new DatagramPacket(data, data.length);
                    while (!interrupted()) {
                        socket.receive(packet);
                        byte[] receivedData = packet.getData();
                        // parse
                        if ((receivedData[0] & 0xff) == 1) {
                            // th
                            int t = receivedData[2] & 0xff;
                            int h = receivedData[3] & 0xff;
                            for (Device device : deviceList) {
                                if (device instanceof TH) {
                                    TH th = (TH) device;
                                    th.setT(t);
                                    th.setH(h);

                                    String htSP = getSharedPreferences("SetDate",MODE_PRIVATE).getString("ht","");
                                    String ltSP = getSharedPreferences("SetDate",MODE_PRIVATE).getString("lt","");
                                    try{
                                        int htSPInt = Integer.parseInt(htSP);
                                        int ltSPInt = Integer.parseInt(ltSP);
                                        Message message = new Message();
                                        if (t>htSPInt){

                                            message.what = 1;
                                            handler.sendMessage(message);

                                        }else if (t<ltSPInt){
                                            message.what = 2;
                                            handler.sendMessage(message);

                                        }
                                    }
                                    catch(NumberFormatException e){

                                        e.printStackTrace();
                                    }


                                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                                    ContentValues values = new ContentValues();
                                    values.put("time", System.currentTimeMillis() );
                                    values.put("t",t);
                                    values.put("h",h);
                                    db.insert("Th",null,values);
//                                    // .....aaaaaaaaaaAa

                                }
                            }
                        } else if ((receivedData[0] & 0xff) == 2) {
                            int y = receivedData[2]& 0xff;
                            int j = receivedData[3]& 0xff;
                            int i = (y<<8)+j;
                            for (Device device : deviceList) {
                                if (device instanceof Ill) {
                                    Ill ill = (Ill) device;
                                    ill.setIll(i);
                                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                                    ContentValues values = new ContentValues();
                                    values.put("time", System.currentTimeMillis() );
                                    values.put("content",i);
                                    db.insert("Ill",null,values);



                                }
                            }

                            // ill
                        } else if ((receivedData[0] & 0xff) == 3) {
                            int i = receivedData[2]& 0xff;
                            int j = receivedData[3]& 0xff;
                            int ho = (i<<8)+j;
                            for (Device device : deviceList) {
                                if (device instanceof Hcho) {
                                    Hcho hcho = (Hcho) device;
                                    hcho.setHcho(ho);
                                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                                    ContentValues values = new ContentValues();
                                    values.put("time", System.currentTimeMillis() );
                                    values.put("content",ho);
                                    db.insert("Hcho",null,values);



                                }
                            }
                        } else if ((receivedData[0]& 0xff) == 4) {
                            int p = receivedData[2]& 0xff;
                            for (Device device : deviceList) {
                                if (device instanceof Pm) {
                                    Pm pm = (Pm) device;
                                    pm.setPm(p);
                                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                                    ContentValues values = new ContentValues();
                                    values.put("time", System.currentTimeMillis() );
                                    values.put("content",p);
                                    db.insert("Pm",null,values);


                                }
                            }
                        } else if ((receivedData[0]& 0xff) == 5) {
                            int i = receivedData[2]& 0xff;
                            int j = receivedData[3]& 0xff;
                            int c = (i<<8)+j;
                            for (Device device : deviceList) {
                                if (device instanceof Co2) {
                                    Co2 co2 = (Co2) device;
                                    co2.setCo2(c);
                                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                                    ContentValues values = new ContentValues();
                                    values.put("time", System.currentTimeMillis() );
                                    values.put("content",c);
                                    db.insert("Co2",null,values);


                                }
                            }
                        }


                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        };
        thread.start();
    }

//
//    public void send() {
//        thread2 = new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                DatagramSocket socket;
//                try {
//                    //
//                    socket = new DatagramSocket(1985);
//                    InetAddress serverAddress = InetAddress.getByName("192.168.1.155");
//                    byte data[] = new byte[]{(byte) 0xff, 0x01};
//                    DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, 10025);
//                    socket.send(packet);
//                } catch (SocketException e) {
//                    e.printStackTrace();
//                } catch (UnknownHostException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        });
//        thread2.start();
//    }

//    private void adjust(){
//        thread3 = new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                Set set = new Set();
//                double ht = Double.parseDouble(set.getHt());
//                double lt = Double.parseDouble(set.getLt());
//
//                SQLiteDatabase db = dbHelper.getWritableDatabase();
//                Cursor cursor = db.query("TH", null, null, null, null, null, null);
//                if (cursor.moveToFirst()) {
//                    do {
//                        double content = cursor.getDouble(cursor.getColumnIndex("content"));
//                        Log.d("Mainactivity", "CO2含量" + content);
//
//                        if(ht < content){
//                            int dy =1;
//                            int lr = 0;
//                            int fs = 0;
//                            double degree = ht;
//                            sendData();
//                        } else if(content<lt){
//                            int dy =1;
//                            int lr = 1;
//                            int fs = 0;
//                            double degree = lt;
//                            sendData();
//                        }
//                    } while (cursor.moveToNext());
//                }
//                cursor.close();
//
//
//            }
//        });
//        thread3.start();
//
//    }
//
//
//    public void sendData() {
//        thread3 = new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                DatagramSocket socket;
//                try {
//                    //
//                    socket = new DatagramSocket(1975);
//                    InetAddress serverAddress = InetAddress.getByName("192.168.1.155");
////                    byte data[] = new byte[]{(byte) 0xff, 0x01};
//                    byte data[] = new byte[]{(byte) dy, (byte) lr, (byte) fs, (byte) degree};
//                    DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, 10025);
//                    socket.send(packet);
//                    socket.close();
//                } catch (SocketException e) {
//                    e.printStackTrace();
//                } catch (UnknownHostException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        });
//        thread3.start();
//    }



}
