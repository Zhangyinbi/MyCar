package cn.ityun.com.carservice.utils;

import android.app.Application;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.smssdk.SMSSDK;


/**
 * Created by Administrator on 2016/6/28 0028.
 */
public class MyApplication extends Application {
    public static RequestQueue queues;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    //以下为后者的举例：
    AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                   /* amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    amapLocation.getLatitude();//获取纬度
                    amapLocation.getLongitude();//获取经度
                    amapLocation.getAccuracy();//获取精度信息
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(amapLocation.getTime());
                    df.format(date);//定位时间
                    amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    amapLocation.getCountry();//国家信息
                    amapLocation.getProvince();//省信息
                    amapLocation.getCity();//城市信息
                    amapLocation.getDistrict();//城区信息
                    amapLocation.getStreet();//街道信息
                    amapLocation.getStreetNum();//街道门牌号信息
                    amapLocation.getCityCode();//城市编码
                    amapLocation.getAdCode();//地区编码
                    amapLocation.getAoiName();//获取当前定位点的AOI信息*/
//                    Log.e("----------------", amapLocation.getAddress() );
                    if (ss != null) {
                        ss.setData(mLocationClient, amapLocation);//传参数
                    }
                } else {
                    Utils.showToast(getApplicationContext(),"定位失败");
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
//                    Log.e("AmapError", "location Error, ErrCode:" + amapLocation.getErrorCode() + ", errInfo:" + amapLocation.getErrorInfo());
                }
            }
        }
    };
    //声明AMapLocationClient类对象
    public static AMapLocationClient mLocationClient = null;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        SMSSDK.initSDK(getApplicationContext(), "155bff5f6ce3a", "bcb8c69b2d68ab969a3aa608dfbc32ed");
        queues = Volley.newRequestQueue(this);
//初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        mLocationClient = new AMapLocationClient(getApplicationContext());
//设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
//设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        if (mLocationOption.isOnceLocationLatest()) {
            mLocationOption.setOnceLocationLatest(true);
//设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
//如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会。
        }
//设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
//设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(600000);
//给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
//启动定位
        mLocationClient.startLocation();
    }

    public static RequestQueue getHttpQueues() {

        return queues;
    }


    public static OnLocation ss;

    public static void setOnLocation(OnLocation ss) {
        MyApplication.ss = ss;
    }

    public interface OnLocation {
        void setData(AMapLocationClient mLocationClient, AMapLocation amapLocation);
    }

    public static AMapLocationClient get() {
        return mLocationClient;
    }

}
