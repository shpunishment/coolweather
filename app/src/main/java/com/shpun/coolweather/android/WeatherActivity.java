package com.shpun.coolweather.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shpun.coolweather.android.gson.Air_now_city;
import com.shpun.coolweather.android.gson.DailyForecast;
import com.shpun.coolweather.android.gson.Lifestyle;
import com.shpun.coolweather.android.gson.Weather;
import com.shpun.coolweather.android.gson.WeatherAir;
import com.shpun.coolweather.android.service.AutoUpdateService;
import com.shpun.coolweather.android.util.HttpUtil;
import com.shpun.coolweather.android.util.Utility;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 显示天气情况等信息，根据是否有缓存判断是否向服务器请求数据
 * 沉浸式标题栏
 * 下拉更新，侧滑抽屉
 * 背景图片
 */

public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = "WeatherActivity";

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private LinearLayout lifestyleLayout;
    private TextView aqiText;
    private TextView pm25Text;

    private ImageView bingPicImg;
    public SwipeRefreshLayout swipeRefresh;
    private String mWeatherId;

    public DrawerLayout drawerLayout;
    private Button navButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 沉浸式标题栏
        if(Build.VERSION.SDK_INT>=21){
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_weather);

        weatherLayout=(ScrollView)findViewById(R.id.weather_layout);
        titleCity=(TextView)findViewById(R.id.title_city);
        titleUpdateTime=(TextView)findViewById(R.id.title_update_time);
        degreeText=(TextView)findViewById(R.id.degree_text);
        weatherInfoText=(TextView)findViewById(R.id.weather_info_text);
        forecastLayout=(LinearLayout)findViewById(R.id.forecast_layout);
        lifestyleLayout=(LinearLayout)findViewById(R.id.lifestyle_layout);
        aqiText=(TextView)findViewById(R.id.aqi_text);
        pm25Text=(TextView)findViewById(R.id.pm25_text);
        bingPicImg=(ImageView)findViewById(R.id.bing_pic_img);
        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);

        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        navButton=(Button)findViewById(R.id.nav_button);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString=prefs.getString("weather",null); // weather json 信息
        String weatherAirString=prefs.getString("weatherair",null); // weather air json 信息
        String bingPic=prefs.getString("bing_pic",null); // bing Image 地址

        if(weatherString!=null && weatherAirString!=null){
            Weather weather= Utility.handleWeatherResponse(weatherString);
            WeatherAir air=Utility.handleWeatherAirResponse(weatherAirString);
            showWeatherInfo(weather);
            showWeatherAirInfo(air);

            mWeatherId=weather.basic.weatherId;
        }else{
            // 无缓存 在线获取
            mWeatherId=getIntent().getStringExtra("weather_id"); // 获取从 ChooseAreaFragment 得到的weather_id
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }

        if(bingPic!=null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else{
            loadBingPic();
        }

        // 如果下拉刷新，重新从服务器获取数据
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    public void requestWeather(final String weatherId){
        String weatherUrl="https://free-api.heweather.com/s6/weather?location="+weatherId+"&key=ca6b41108d3741298f18e85dc3288dba";
        String aqiUrl="https://free-api.heweather.com/s6/air/now?location="+weatherId+"&key=ca6b41108d3741298f18e85dc3288dba";

        // 获取 weather
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气失败 onFailure",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                final Weather weather=Utility.handleWeatherResponse(responseText);
                // 需更新UI 应在主线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather!=null && "ok".equals(weather.status)){
                            // 自动使用当前包名来命名并创建SharedPreferences 文件
                            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            // 存储 weather 的 json 数据
                            editor.putString("weather",responseText);
                            editor.apply();

                            mWeatherId=weather.basic.weatherId;
                            showWeatherInfo(weather);
                        }else{
                            Toast.makeText(WeatherActivity.this,"获取天气失败 onResponse",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // 获取 weather air
        HttpUtil.sendOkHttpRequest(aqiUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气质量失败 onFailure",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                final WeatherAir air=Utility.handleWeatherAirResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(air!=null && "ok".equals(air.status)){
                            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weatherair",responseText);
                            editor.apply();
                            showWeatherAirInfo(air);
                        }else{
                            Toast.makeText(WeatherActivity.this,"获取天气质量失败 onResponse",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        swipeRefresh.setRefreshing(false);

        loadBingPic();

    }

    // 更新 UI 信息
    private void showWeatherInfo(Weather weather){
        String cityName=weather.basic.cityName;
        String updateTime=weather.update.updateTime.split(" ")[1];
        String degree=weather.now.temperature+"℃";
        String weatherInfo=weather.now.condText;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);

        lifestyleLayout.removeAllViews();
        for(Lifestyle lifestyle:weather.lifestyleList){
            View vComf=LayoutInflater.from(this).inflate(R.layout.lifestyle_item_comf,lifestyleLayout,false);
            View vCw=LayoutInflater.from(this).inflate(R.layout.lifestyle_item_cw,lifestyleLayout,false);
            View vSport=LayoutInflater.from(this).inflate(R.layout.lifestyle_item_sport,lifestyleLayout,false);
            TextView comfortText=(TextView)vComf.findViewById(R.id.comfort_text);
            TextView carwashText=(TextView)vCw.findViewById(R.id.car_wash_text);
            TextView sportText=(TextView)vSport.findViewById(R.id.sport_text);

            if("comf".equals(lifestyle.type)){
                comfortText.setText("舒适度:"+lifestyle.text);
                lifestyleLayout.addView(vComf);
            }else if("cw".equals(lifestyle.type)){
                carwashText.setText("洗车指数:"+lifestyle.text);
                lifestyleLayout.addView(vCw);
            }else if("sport".equals(lifestyle.type)){
                sportText.setText("运动建议:"+lifestyle.text);
                lifestyleLayout.addView(vSport);
            }

        }

        forecastLayout.removeAllViews();
        for(DailyForecast dailyForecast:weather.forecastList){
            View view= LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateText=(TextView)view.findViewById(R.id.date_text);
            TextView infoText=(TextView)view.findViewById(R.id.info_text);
            TextView maxText=(TextView)view.findViewById(R.id.max_text);
            TextView minText=(TextView)view.findViewById(R.id.min_text);

            dateText.setText(dailyForecast.date);
            infoText.setText(dailyForecast.condInfo);
            maxText.setText(dailyForecast.tmpMax);
            minText.setText(dailyForecast.tmpMin);
            forecastLayout.addView(view);
        }

        weatherLayout.setVisibility(View.VISIBLE);

        // 开启后台更新数据服务
        Intent intent=new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

    private void showWeatherAirInfo(WeatherAir air){
        aqiText.setText(air.air_now_city.aqi);
        pm25Text.setText(air.air_now_city.pm25);
    }

    private void loadBingPic(){
        String requestBingPic="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic=response.body().string();
                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });

            }
        });
    }

}
