package com.shpun.coolweather.android.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.shpun.coolweather.android.db.City;
import com.shpun.coolweather.android.db.County;
import com.shpun.coolweather.android.db.Province;
import com.shpun.coolweather.android.gson.Air_now_city;
import com.shpun.coolweather.android.gson.Weather;
import com.shpun.coolweather.android.gson.WeatherAir;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shpun on 2018/2/22.
 */

public class Utility {

    // 将省市县数据存入数据库

    /*
    *  解析和处理服务器返回的省级数据
    * */
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try{
                // 解析json
                JSONArray allProvinces=new JSONArray(response);
                for(int i=0;i<allProvinces.length();i++){
                    // 依次提取并存入数据库
                    JSONObject provinceObject=allProvinces.getJSONObject(i);
                    Province province=new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /*
    *  解析和处理服务器返回的市级数据
    * */
    public static boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCities=new JSONArray(response);
                for(int i=0;i<allCities.length();i++){
                    JSONObject cityObject=allCities.getJSONObject(i);
                    City city=new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /*
    *  解析和处理服务器返回的县级数据
    * */
    public static boolean handleCountyResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCounties=new JSONArray(response);
                for(int i=0;i<allCounties.length();i++){
                    JSONObject countyObject=allCounties.getJSONObject(i);
                    County county=new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;

    }

    /*
    *  解析和处理服务器返回的weather数据
    * */
    public static Weather handleWeatherResponse(String response){
        try{
            // 通过GSON 处理 json 数据
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather6");
            String weatherContent=jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /*
    *  解析和处理服务器返回的weatherair数据
    * */
    public static WeatherAir handleWeatherAirResponse(String response){
        try{
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather6");
            String airContent=jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(airContent,WeatherAir.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
