package cn.ityun.com.carservice.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;

import cn.ityun.com.carservice.bean.gsonbean.Car;
import cn.ityun.com.carservice.bean.gsonbean.CarSize;
import cn.ityun.com.carservice.bean.gsonbean.Privince;
import cn.ityun.com.carservice.global.DataInterface;

/**
 * Created by Administrator on 2016/7/2 0002.
 */
public class MyDbOpenHelper extends SQLiteOpenHelper {
    public static SQLiteDatabase dbHelper;

    /**
     * @param context 上下文
     *                name  数据库的名称
     *                factory  游标工厂  null用默认的游标工厂
     *                version  版本号>=1的整数
     */
    //写一个类继承SQLiteOpenHelper
    public MyDbOpenHelper(Context context) {
        super(context, "Car.db", null,1);
        Log.e("===========","MyDbOpenHelper");
        dbHelper = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("create table expressInfo(_id integer primary key autoincrement,mailNo varchar(20),simpleName varchar(20),imgUrl varchar(20),expName varchar(20),phone varchar(20),goodsName varchar(20),content varchar(20) ,time long(20),status varchar(20))");
//        db.execSQL("create table info(_id integer primary key autoincrement,expName varchar(20),phone varchar(20))");
        db.execSQL("create table info(carName varchar(20))");
        db.execSQL("create table prov(carorg varchar(20),engineno varchar(20),frameno varchar(20),lsnum varchar(20),lsprefix varchar(20),province varchar(20))");
        db.execSQL("create table size(code varchar(20),name varchar(20))");
        db.execSQL("create table carinfo(chepai varchar(20),lsprefix varchar(20),lsnum varchar(20),carorg varchar(20),lstype varchar(20),frameno varchar(20),type varchar(20),msg varchar(20),count varchar(20),price varchar(20) ,time long(20),score varchar(20))");


        saveCarSize();
        saveProvinceData();
        saveCarData();



    }

    private void saveCarSize() {
        RequestQueue queues = MyApplication.getHttpQueues();
        String url=DataInterface.CAR_SIZE_URL;
        StringRequest request=new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson=new Gson();
                CarSize carSize = gson.fromJson(s, CarSize.class);
                SQLiteDatabase dbHelper = MyDbOpenHelper.dbHelper;
                ArrayList<CarSize.Size> results = carSize.result;
                for (CarSize.Size result:results){
                    ContentValues values = new ContentValues();
                    values.put("name",result.name);
                    values.put("code",result.code);
                    MyDbOpenHelper.dbHelper.insert("size",null,values);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                saveCarSize();
            }
        });
        request.setTag(url);
        queues.add(request);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    private void saveProvinceData() {
        RequestQueue queues = MyApplication.getHttpQueues();
        String url= DataInterface.PROVINCE_URL;
        StringRequest request=new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson=new Gson();
                Privince privinceInfo = gson.fromJson(s, Privince.class);
                SQLiteDatabase dbHelper = MyDbOpenHelper.dbHelper;
                ArrayList<Privince.type.Sheng> datas = privinceInfo.result.data;
                for (Privince.type.Sheng data:datas){
                    ContentValues values = new ContentValues();
                    if (data.carorg!=null){
                        values.put("carorg",data.carorg);
                    }
                    if (data.engineno!=null){
                        values.put("engineno",data.engineno);
                    }
                    if (data.frameno!=null){
                        values.put("frameno",data.frameno);
                    }
                    if (data.lsnum!=null){
                        values.put("lsnum",data.lsnum);
                    }
                        values.put("lsprefix",data.lsprefix);
                        values.put("province",data.province);
                    dbHelper.execSQL("create table "+data.province+"(carorg varchar(20),city varchar(20),lsnum varchar(20))");
                    if(data.list!=null){
                        ArrayList<Privince.type.Sheng.City> cities = data.list;
                        ContentValues contentValues = new ContentValues();
                        for (Privince.type.Sheng.City city:cities) {
                            contentValues.put("carorg",city.carorg );
                            contentValues.put("city",city.city);
                            contentValues.put("lsnum",city.lsnum);
                            MyDbOpenHelper.dbHelper.insert(data.province,null,contentValues);
                        }
                    }else {
                        ContentValues contentValue = new ContentValues();
                        contentValue.put("carorg",data.carorg );
                        contentValue.put("city",data.province);
                        MyDbOpenHelper.dbHelper.insert(data.province,null,contentValue);
                    }
                    MyDbOpenHelper.dbHelper.insert("prov",null,values);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                saveProvinceData();
            }
        });
        request.setTag(url);
        queues.add(request);

    }






    private void saveCarData() {
        RequestQueue queues = MyApplication.getHttpQueues();
        String url="http://www.autohome.com.cn/ashx/AjaxIndexCarFind.ashx?type=11";
        StringRequest request=new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson=new Gson();
                Car car = gson.fromJson(s, Car.class);
                SQLiteDatabase dbHelper = MyDbOpenHelper.dbHelper;
                ArrayList<Car.Type.Info> infos = car.result.branditems;
                for (Car.Type.Info info:infos){
                    ContentValues values = new ContentValues();
                    values.put("carName",info.name);
                    MyDbOpenHelper.dbHelper.insert("info",null,values);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                saveCarData();
            }
        });
        request.setTag(url);
        queues.add(request);
    }
}
