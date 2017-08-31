package com.returnlive.healthinspectioninstrument.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/21 0021
 * 时间： 上午 10:23
 * 描述： DBHelper继承了SQLiteOpenHelper，作为维护和管理数据库的基类
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "health.db";//数据库名称
    public static final String ECG_TABLE_NAME = "ecg_data_table";//心电
    public static final String TMEP_TABLE_NAME = "temp_data_table";//体温
    public static final String BLOOD_OX_TABLE_NAME = "blood_ox_data_table";//血氧
    public static final String BLOOD_PRE_TABLE_NAME = "blood_pre_data_table";//血压
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ECG_TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, time STRING,data STRING,hr STRING,rr_max STRING,rr_min STRING,hrv STRING,mood STRING,br STRING)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TMEP_TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, time STRING,data STRING)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + BLOOD_OX_TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, time STRING,blood_data STRING,hr_data STRING)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + BLOOD_PRE_TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, time STRING,blood_pre_sys STRING,blood_pre_dia STRING)");
    }

    //数据库第一次创建时onCreate方法会被调用，我们可以执行创建表的语句，当系统发现版本变化之后，会调用onUpgrade方法，我们可以执行修改表结构等语句
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //在表info中增加一列other
        //db.execSQL("ALTER TABLE info ADD COLUMN other STRING");
    }
}
