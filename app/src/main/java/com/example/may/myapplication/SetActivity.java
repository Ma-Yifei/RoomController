package com.example.may.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.may.myapplication.device.Set;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class SetActivity extends AppCompatActivity {
    private EditText h_temp;
    private EditText l_temp;
    private EditText h_ill;
    private EditText l_ill;
    private Button btn;

    private TextView h_temptv;

    private SQLiteOpenHelper dbHelper;
    private Thread thread2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        h_temp =  findViewById(R.id.h_temp);
        l_temp = findViewById(R.id.l_temp);
        h_ill = findViewById(R.id.h_ill);
        l_ill = findViewById(R.id.l_ill);
        h_temptv = findViewById(R.id.h_temptv);
        btn = findViewById(R.id.btn);

        String htSP = getSharedPreferences("SetDate",MODE_PRIVATE).getString("ht","");
        String ltSP = getSharedPreferences("SetDate",MODE_PRIVATE).getString("lt","");
        h_temp.setText(htSP);
        l_temp.setText(ltSP);
        final SharedPreferences.Editor editor = getSharedPreferences("SetDate",MODE_PRIVATE).edit();
        editor.putString("ht","25");
        editor.putString("lt","14");
        editor.apply();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ht = h_temp.getText().toString();

                String lt = l_temp.getText().toString();
                editor.putString("ht",ht);
                editor.putString("lt",lt);
                Intent intent = new Intent(SetActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
