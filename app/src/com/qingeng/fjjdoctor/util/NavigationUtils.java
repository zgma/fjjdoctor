package com.qingeng.fjjdoctor.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class NavigationUtils {

    public static void invokingBD(Activity activity, Double latitude1, Double longitude1) {
        Map<String, Float> floatMap = bdEncrypt(latitude1, longitude1);
        Double latitude = floatMap.get("lat").doubleValue();
        Double longitude = floatMap.get("lon").doubleValue();
        Intent intent = new Intent();
        try {
            intent.setData(Uri.parse("baidumap://map/direction?" +
                    "destination=latlng:" + latitude + "," + longitude +
                    "&mode=driving"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isInstallByread("com.baidu.BaiduMap")) {
//          activity.startActivity(intent); //启动调用
            openBaiduNavi(activity, latitude + "", longitude + "");
            android.util.Log.e("GasStation", "百度地图客户端已经安装");
        } else {
            Toast.makeText(activity, "没有安装百度地图客户端", Toast.LENGTH_SHORT).show();
        }
    }

    public static void invokingGD(Activity activity, Double latitude, Double longitude, String address) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("amapuri://route/plan/?" +
                "dlat=" + latitude +
                "&dlon=" + longitude +
                "&dname=" + address +
                "&dev=0" +
                "&t=0"));

     /*   //  com.autonavi.minimap这是高德地图的包名
        Intent intent = new Intent("android.intent.action.VIEW",android.net.Uri.parse("androidamap://navi?sourceApplication=国投充电&lat="+ "&dev=0"));
        intent.setPackage("com.autonavi.minimap");
        intent.setData(Uri.parse("androidamap://poi?sourceApplication=softname&keywords="+stationBean.getAddress()));*/

        if (isInstallByread("com.autonavi.minimap")) {
            activity.startActivity(intent);

            android.util.Log.e("GasStation", "高德地图客户端已经安装");
        } else {
            Toast.makeText(activity, "没有安装高德地图客户端", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private static boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }


    /**
     * 打开百度地图导航客户端
     * intent = Intent.getIntent("baidumap://map/navi?location=34.264642646862,108.95108518068&type=BLK&src=thirdapp.navi.you
     * location 坐标点 location与query二者必须有一个，当有location时，忽略query
     * query    搜索key   同上
     * type 路线规划类型  BLK:躲避拥堵(自驾);TIME:最短时间(自驾);DIS:最短路程(自驾);FEE:少走高速(自驾);默认DIS
     */
    public static void openBaiduNavi(Context context, String lat, String lng) {
        StringBuffer stringBuffer = new StringBuffer("baidumap://map/geocoder?location=")
                .append(lat).append(",").append(lng).append("&type=TIME");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(stringBuffer.toString()));
        intent.setPackage("com.baidu.BaiduMap");
        context.startActivity(intent);
    }

    static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
    //高德转百度
    public static Map<String, Float> bdEncrypt(double gg_lat, double gg_lon) {
        Map<String, Float> data = new HashMap<String, Float>();
        double x = gg_lon, y = gg_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        System.out.println(bd_lon + "," + bd_lat);
        System.out.println(new BigDecimal(String.valueOf(bd_lon)).floatValue() + "," + new BigDecimal(String.valueOf(bd_lat)).floatValue());
        data.put("lon", new BigDecimal(String.valueOf(bd_lon)).floatValue());
        data.put("lat", new BigDecimal(String.valueOf(bd_lat)).floatValue());
        return data;
    }


}
