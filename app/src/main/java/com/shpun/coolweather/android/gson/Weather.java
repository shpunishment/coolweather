package com.shpun.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shpun on 2018/2/22.
 */

public class Weather {

    public String status;

    public Basic basic;

    public Update update;

    public Now now;

    // 让JSON字段与java字段建立映射
    @SerializedName("lifestyle")
    public List<Lifestyle> lifestyleList;

    @SerializedName("daily_forecast")
    public List<DailyForecast> forecastList;

}
