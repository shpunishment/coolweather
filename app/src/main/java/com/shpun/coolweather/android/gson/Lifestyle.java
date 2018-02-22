package com.shpun.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shpun on 2018/2/22.
 */

public class Lifestyle {

    /** 数组
     *  "lifestyle": [
     {
     "brf": "舒适",
     "txt": "今天夜间不太热也不太冷，风力不大，相信您在这样的天气条件下，应会感到比较清爽和舒适。",
     "type": "comf"
     },
     {
     "brf": "较舒适",
     "txt": "建议着薄外套、开衫牛仔衫裤等服装。年老体弱者应适当添加衣物，宜着夹克衫、薄毛衣等。",
     "type": "drsg"
     },
     {
     "brf": "适宜",
     "txt": "天气较好，赶快投身大自然参与户外运动，尽情感受运动的快乐吧。",
     "type": "sport"
     },
     */

    @SerializedName("brf")
    public String comfort;

    @SerializedName("txt")
    public String comfortText;

    @SerializedName("type")
    public String type;

}
