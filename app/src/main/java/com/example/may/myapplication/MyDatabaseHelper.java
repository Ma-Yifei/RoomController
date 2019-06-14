package com.example.may.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_TH = "create table Th(" +
            //primary key 将id列设为主键    autoincrement表示id列是自增长的
            "id integer primary key autoincrement," +
            "t real," +
            "h real," +
            "time integer)";
    public static final String CREATE_HCHO = "create table Hcho(" +
            //primary key 将id列设为主键    autoincrement表示id列是自增长的
            "id integer primary key autoincrement," +
            "content real," +
            "time integer)";
    public static final String CREATE_ILL = "create table Ill(" +
            //primary key 将id列设为主键    autoincrement表示id列是自增长的
            "id integer primary key autoincrement," +
            "content real," +
            "time integer)";
    public static final String CREATE_PM = "create table Pm(" +
            //primary key 将id列设为主键    autoincrement表示id列是自增长的
            "id integer primary key autoincrement," +
            "content real," +
            "time integer)";
    public static final String CREATE_CO2 = "create table Co2(" +
            //primary key 将id列设为主键    autoincrement表示id列是自增长的
            "id integer primary key autoincrement," +
            "content real," +
            "time integer)";

    private Context mContext;


    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super((Context) context, name, factory,version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //调用SQLiteDatabase中的execSQL（）执行建表语句。
        db.execSQL(CREATE_TH);
        db.execSQL(CREATE_HCHO);
        db.execSQL(CREATE_ILL);
        db.execSQL(CREATE_PM);
        db.execSQL(CREATE_CO2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}