package com.camera.operation.cameraandgps.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import java.util.List;


/**
 * Created by similar on 2017/2/10.
 */

public class GPSUtil {

    private Context mContext;
    private LocationManager locationManager;

    public GPSUtil(Context context) {
        super();
        mContext = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * 获取地址的经纬度
     * @return 维度_经度
     */
    public String getGpsAddress() {
        // 返回所有已知的位置提供者的名称列表，包括未获准访问或调用活动目前已停用的。
        List<String> lp = locationManager.getAllProviders();
        for (String item : lp) {
            Log.i("8023", "可用位置服务：" + item);
        }
        Criteria criteria = new Criteria();
        criteria.setCostAllowed(false);
        // 设置位置服务免费
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 设置水平位置精度
        // getBestProvider 只有允许访问调用活动的位置供应商将被返回
        String providerName = locationManager.getBestProvider(getCriteria(), true);
        Log.i("8023", "------位置服务：" + providerName);
        if (providerName != null) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            Location location = locationManager.getLastKnownLocation(providerName);
            Log.i("8023", "------位置服务---------------：");
            if (location != null) {
                Constants.LongitudeStr = location.getLongitude()+"";
                Constants.LatitudeStr = location.getLatitude()+"";
                return "纬度:" + location.getLatitude() + " 经度:" + location.getLongitude();
            }
        } else {
            Constants.LongitudeStr = "0";
            Constants.LatitudeStr = "0";
            return "1.请检查网络连接 \n2.请打开我的位置";
        }
        Constants.LongitudeStr = "0";
        Constants.LatitudeStr = "0";
        return "未能获取到当前位置，请检测以下设置：\n1.检查网络连接 \n2.打开我的位置(GPS)";
    }

    /**
     * 返回查询条件
     * @return
     */
    private Criteria getCriteria(){
        Criteria criteria=new Criteria();
        //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        //设置是否需要方位信息
        criteria.setBearingRequired(false);
        //设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }
}