package com.shpun.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shpun on 2018/2/22.
 */

public class Now {

    /**
     * "now": {
     "cond_txt": "多云",
     "tmp": "14",
     },
     */

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond_txt")
    public String condText;


}
