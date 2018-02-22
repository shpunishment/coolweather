package com.shpun.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shpun on 2018/2/22.
 */

public class Update {

    /**
     * "update": {
     "loc": "2017-10-26 23:09",
     }
     */
    @SerializedName("loc")
    public String updateTime;

}
