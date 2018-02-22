package com.shpun.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shpun on 2018/2/22.
 */

public class Basic {
    /**
     * "basic": {
     "cid": "CN101010100",
     "location": "北京",
     },
     */

    @SerializedName("location")
    public String cityName;

    @SerializedName("cid")
    public String weatherId;

}
