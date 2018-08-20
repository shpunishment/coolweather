# coolweather

### 综合Fragment，ListView，DrawerLayout，ScrollView等常用控件
### 综合LitePal，GSON，OkHttp,Glide等开源库
### 完成的中国地区天气查询 Android App

#### 通过OkHttp向服务器发送请求，获取中国省市县数据，用JSONObject解析，并保存在Sqlite数据库中
#### 通过OkHttp向服务器发送请求，获取天气的数据，用GSON解析发送回来的数据，并用SharedPreferences的键值对保存在手机
#### 设置Android的定时Service，每八小时向服务器请求一次，并用SharedPreferences保存在手机

    db: Province , City , County  数据库类

    gson:                         JSON数据结构

    util: Httputil (  sendOkHttpRequest(String address,okhttp3.Callback callback) 向服务器请求一次

    utility：
        handleProvinceResponse(String response)  解析json并保存到数据库
        handleCityResponse(String response,int provinceId)
        handleCountyResponse(String response,int cityId)
        handleWeatherResponse(String response)   解析Weather数据并返回
        handleWeatherAirResponse(String response)

    service: AutoUpdateService  每八小时向服务器请求一次数据并保存

    ChooseAreaFragment ( 适配ListView 请求服务器数据 数据库查询省市县 

    WeatherActivity : 
        requestWeather(final String weatherId) 向服务器请求天气数据 交给handleWeatherResponse(String response)解析 并保存
        showWeatherInfo(Weather weather) 更新UI信息
        showWeatherAirInfo(WeatherAir air)
        loadBingPic()

MainActivity 显示天气信息
<br/>
<img src="https://github.com/shpunishment/coolweather/blob/master/app/src/main/res/drawable-xxhdpi/mainActivity.png" width="250" height="450" />

<br/>
<img src="https://github.com/shpunishment/coolweather/blob/master/app/src/main/res/drawable-xxhdpi/mainActivity(2).png" width="250" height="450" />

Drawerlayout 选择地区
<br/>
<img src="https://github.com/shpunishment/coolweather/blob/master/app/src/main/res/drawable-xxhdpi/drawerLayout.png" width="250" height="450" />
