package com.shpun.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shpun on 2018/2/22.
 */

public class Air_now_city {

    /**
     * "air_now_city": {
     "aqi": "19",
     "pm25": "8",
     },
     */

    @SerializedName("aqi")
    public String aqi;

    @SerializedName("pm25")
    public String pm25;

}
