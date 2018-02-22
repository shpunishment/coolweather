package com.shpun.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shpun on 2018/2/22.
 */

public class DailyForecast {

    /*
         "daily_forecast": [
                {
                    "cond_txt_d": "晴间多云",
                    "date": "2017-10-26",
                    "tmp_max": "16",
                    "tmp_min": "8",
                },
                {
                    "cond_txt_d": "多云",
                    "date": "2017-10-27",
                    "tmp_max": "18",
                    "tmp_min": "9",
                },
                {
                    "cond_txt_d": "多云",
                    "date": "2017-10-28",
                    "tmp_max": "17",
                    "tmp_min": "5",
                }
            ],
     */

    @SerializedName("cond_txt_d")
    public String condInfo;

    @SerializedName("date")
    public String date;

    @SerializedName("tmp_max")
    public String tmpMax;

    @SerializedName("tmp_min")
    public String tmpMin;

}
