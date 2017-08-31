package com.returnlive.healthinspectioninstrument.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.returnlive.healthinspectioninstrument.bean.DbBloodOxBean;
import com.returnlive.healthinspectioninstrument.bean.DbBloodPreBean;
import com.returnlive.healthinspectioninstrument.bean.DbEcgBean;
import com.returnlive.healthinspectioninstrument.bean.DbTempBean;

import java.util.ArrayList;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/30 0030
 * 时间： 下午 1:37
 * 描述： DBManager是建立在DBHelper之上，封装了常用的业务方法
 */

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }


    //向ecg_data_table表中插入数据
    public void addEcgMessage(String time, String data,String hr,String rr_max,String rr_min,String hrv,String mood,String br) {
        ContentValues cv = new ContentValues();
        cv.put("time", time);
        cv.put("data", data);
        cv.put("hr", hr);
        cv.put("rr_max", rr_max);
        cv.put("rr_min", rr_min);
        cv.put("hrv", hrv);
        cv.put("mood", mood);
        cv.put("br", br);
        db.insert(DBHelper.ECG_TABLE_NAME, null, cv);
    }

    //无条件查询心电数据
    public ArrayList<DbEcgBean> searchEcgData() {
        String sql = "SELECT * FROM " + DBHelper.ECG_TABLE_NAME;
        return ExecSQLForEcgData(sql);
    }

    //搜寻心电
    private ArrayList<DbEcgBean> ExecSQLForEcgData(String sql) {
        ArrayList<DbEcgBean> list = new ArrayList<>();
        Cursor c = ExecSQLForCursor(sql);
        while (c.moveToNext()) {
            DbEcgBean dbEcgBean = new DbEcgBean();
            dbEcgBean.time = c.getString(c.getColumnIndex("time"));
            dbEcgBean.jsonData = c.getString(c.getColumnIndex("data"));
            dbEcgBean.hr = c.getString(c.getColumnIndex("hr"));
            dbEcgBean.rr_max = c.getString(c.getColumnIndex("rr_max"));
            dbEcgBean.rr_min = c.getString(c.getColumnIndex("rr_min"));
            dbEcgBean.hrv = c.getString(c.getColumnIndex("hrv"));
            dbEcgBean.moods = c.getString(c.getColumnIndex("mood"));
            dbEcgBean.brs = c.getString(c.getColumnIndex("br"));
            list.add(dbEcgBean);
        }
        c.close();
        return list;
    }

    //向temp_data_table表中插入数据
    public void addTempMessage(String time, String data) {
        ContentValues cv = new ContentValues();
        cv.put("time", time);
        cv.put("data", data);
        db.insert(DBHelper.TMEP_TABLE_NAME, null, cv);
    }

    //无条件查询体温数据
    public ArrayList<DbTempBean> searchTempData() {
        String sql = "SELECT * FROM " + DBHelper.TMEP_TABLE_NAME;
        return ExecSQLForTempData(sql);
    }

    //搜寻体温
    private ArrayList<DbTempBean> ExecSQLForTempData(String sql) {
        ArrayList<DbTempBean> list = new ArrayList<>();
        Cursor c = ExecSQLForCursor(sql);
        while (c.moveToNext()) {
            DbTempBean dbTempBean = new DbTempBean();
            dbTempBean.time = c.getString(c.getColumnIndex("time"));
            dbTempBean.data = c.getString(c.getColumnIndex("data"));
            list.add(dbTempBean);
        }
        c.close();
        return list;
    }


    //向blood_ox_data_table表中插入数据
    public void addBloodOxMessage(String time, String bloodData, String hrData) {
        ContentValues cv = new ContentValues();
        cv.put("time", time);
        cv.put("blood_data", bloodData);
        cv.put("hr_data", hrData);
        db.insert(DBHelper.BLOOD_OX_TABLE_NAME, null, cv);
    }


    //无条件查询血氧数据
    public ArrayList<DbBloodOxBean> searchBloodOxData() {
        String sql = "SELECT * FROM " + DBHelper.BLOOD_OX_TABLE_NAME;
        return ExecSQLForBloodOxData(sql);
    }

    //搜寻血氧
    private ArrayList<DbBloodOxBean> ExecSQLForBloodOxData(String sql) {
        ArrayList<DbBloodOxBean> list = new ArrayList<>();
        Cursor c = ExecSQLForCursor(sql);
        while (c.moveToNext()) {
            DbBloodOxBean dbBloodOxBean = new DbBloodOxBean();
            dbBloodOxBean.time = c.getString(c.getColumnIndex("time"));
            dbBloodOxBean.bloodOx = c.getString(c.getColumnIndex("blood_data"));
            dbBloodOxBean.hr = c.getString(c.getColumnIndex("hr_data"));
            list.add(dbBloodOxBean);
        }
        c.close();
        return list;
    }


    //向blood_ox_data_table表中插入数据
    public void addBloodPreMessage(String time, String bloodPreSys, String bloodPreDia) {
        ContentValues cv = new ContentValues();
        cv.put("time", time);
        cv.put("blood_pre_sys", bloodPreSys);
        cv.put("blood_pre_dia", bloodPreDia);
        db.insert(DBHelper.BLOOD_PRE_TABLE_NAME, null, cv);
    }


    //无条件查询血氧数据
    public ArrayList<DbBloodPreBean> searchBloodPreData() {
        String sql = "SELECT * FROM " + DBHelper.BLOOD_PRE_TABLE_NAME;
        return ExecSQLForBloodPreData(sql);
    }

    //搜寻血氧
    private ArrayList<DbBloodPreBean> ExecSQLForBloodPreData(String sql) {
        ArrayList<DbBloodPreBean> list = new ArrayList<>();
        Cursor c = ExecSQLForCursor(sql);
        while (c.moveToNext()) {
            DbBloodPreBean dbBloodPreBean = new DbBloodPreBean();
            dbBloodPreBean.time = c.getString(c.getColumnIndex("time"));
            dbBloodPreBean.bloodPreSys = c.getString(c.getColumnIndex("blood_pre_sys"));
            dbBloodPreBean.bloodPreDia = c.getString(c.getColumnIndex("blood_pre_dia"));
            list.add(dbBloodPreBean);
        }
        c.close();
        return list;
    }



    //执行SQL，返回一个游标
    private Cursor ExecSQLForCursor(String sql) {
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public void closeDB() {
        db.close();
    }

    private void ExecSQL(String sql) {
        try {
            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
