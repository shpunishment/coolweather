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

    public Air_now_city aqi;

    public Now now;

    @SerializedName("lifestyle")
    public List<Lifestyle> lifestyle;

    @SerializedName("daily_forecast")
    public List<DailyForecast> forecastList;

}
