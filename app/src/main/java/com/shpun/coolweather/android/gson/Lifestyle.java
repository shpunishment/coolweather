package com.shpun.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shpun on 2018/2/22.
 */

public class Lifestyle {

    /** 数组
     * "lifestyle":[
     *  {"brf":"较舒适",
     "txt":"白天天气晴好，早晚会感觉偏凉，午后舒适、宜人。",
     "type":"comf"},
     {"brf":"较冷",
     "txt":"建议着厚外套加毛衣等服装。年老体弱者宜着大衣、呢外套加羊毛衫。",
     "type":"drsg"},
     {"brf":"较易发",
     "txt":"昼夜温差较大，较易发生感冒，请适当增减衣服。体质较弱的朋友请注意防护。",
     "type":"flu"},
     {"brf":"较适宜",
     "txt":"天气较好，无雨水困扰，较适宜进行各种运动，但因气温较低，在户外运动请注意增减衣物。",
     "type":"sport"},
     {"brf":"适宜",
     "txt":"天气较好，但丝毫不会影响您出行的心情。温度适宜又有微风相伴，适宜旅游。",
     "type":"trav"},
     {"brf":"最弱",
     "txt":"属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。",
     "type":"uv"},
     {"brf":"较适宜",
     "txt":"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。",
     "type":"cw"},
     {"brf":"中",
     "txt":"气象条件对空气污染物稀释、扩散和清除无明显影响，易感人群应适当减少室外活动时间。",
     "type":"air"}]
     */

    @SerializedName("brf")
    public String comfort;

    @SerializedName("txt")
    public String text;

    @SerializedName("type")
    public String type;

}
