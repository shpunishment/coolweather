package com.shpun.coolweather.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shpun.coolweather.android.db.City;
import com.shpun.coolweather.android.db.County;
import com.shpun.coolweather.android.db.Province;
import com.shpun.coolweather.android.util.HttpUtil;
import com.shpun.coolweather.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.internal.Util;

/**
 * Created by shpun on 2018/2/22.
 */

/**
 * 选择地区的碎片，完成对地区的选择 和 地区的切换
 */

public class ChooseAreaFragment extends Fragment {

    public static final int LEVEL_PROVINCE=0;
    public static final int LEVEL_CITY=1;
    public static final int LEVEL_COUNTY=2;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList=new ArrayList<>();

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;

    private Province selectedProvince;
    private City selectedCity;

    private int currentLevel;

    private int pProvince;
    private int pCity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.choose_area,container,false); // 加载碎片
        titleText=(TextView)view.findViewById(R.id.title_text);
        backButton=(Button)view.findViewById(R.id.back_button);
        listView=(ListView)view.findViewById(R.id.list_view);
        adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(currentLevel==LEVEL_PROVINCE){
                    selectedProvince=provinceList.get(i);
                    pProvince=i;
                    queryCities();
                }else if(currentLevel==LEVEL_CITY){
                    selectedCity=cityList.get(i);
                    pCity=i;
                    queryCounties();
                }else if(currentLevel==LEVEL_COUNTY){
                    String weatherId=countyList.get(i).getWeatherId();

                    if(getActivity() instanceof MainActivity){ // 还在未选择的界面
                        Intent intent=new Intent(getActivity(),WeatherActivity.class);
                        intent.putExtra("weather_id",weatherId); // 给WeatherActivity 传weather_id 值
                        startActivity(intent);
                        getActivity().finish();
                    }else if(getActivity() instanceof WeatherActivity){ // 在进行城市切换时
                        WeatherActivity activity=(WeatherActivity)getActivity();
                        activity.drawerLayout.closeDrawers(); // 关闭滑动菜单
                        activity.swipeRefresh.setRefreshing(true); // 显示下拉刷新
                        activity.requestWeather(weatherId); // 请求Weather
                    }
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentLevel==LEVEL_COUNTY){
                    queryCities();
                }else if(currentLevel==LEVEL_CITY){
                    queryProvinces();
                }
            }
        });

        queryProvinces();
    }

    /**
     * 查询 全国所有的省，优先从数据库，没有从服务器
     */
    private void queryProvinces(){
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList= DataSupport.findAll(Province.class);
        if(provinceList.size()>0){
            dataList.clear();
            for(Province province:provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged(); // 更新适配器
            listView.setSelection(pProvince);
            currentLevel=LEVEL_PROVINCE;
        }else{
            // 本地数据库没有 从网络获取
            String address="http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }

    }
    /**
     * 查询 选中省内的所有市，优先从数据库，没有从服务器
     */
    private void queryCities(){
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList= DataSupport.where("provinceId=?",String.valueOf(selectedProvince.getId())).find(City.class);
        if(cityList.size()>0){
            dataList.clear();
            for(City city:cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(pCity);
            currentLevel=LEVEL_CITY;
        }else{
            int provinceCode=selectedProvince.getProvinceCode();
            String address="http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(address,"city");
        }

    }

    private void queryCounties(){
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList= DataSupport.where("cityId=?",String.valueOf(selectedCity.getId())).find(County.class);
        if(countyList.size()>0){
            dataList.clear();
            for(County county:countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_COUNTY;
        }else{
            int provinceCode=selectedProvince.getProvinceCode();
            int cityCode=selectedCity.getCityCode();
            String address="http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            queryFromServer(address,"county");
        }
    }

    private void queryFromServer(String address,final String type){
        showProgressDialog();

        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText=response.body().string();
                boolean result=false;
                // 根据 省市县 的不同，选择数据处理函数
                if("province".equals(type)){
                    result= Utility.handleProvinceResponse(responseText);
                }else if("city".equals(type)){
                    result=Utility.handleCityResponse(responseText,selectedProvince.getId());
                }else if("county".equals(type)){
                    result=Utility.handleCountyResponse(responseText,selectedCity.getId());
                }

                if(result){
                    // query.. 等函数有UI操作，切换主线程
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                                queryProvinces();
                            }else if("city".equals(type)){
                                queryCities();
                            }else if("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }

            }
        });

    }

    private void showProgressDialog(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("加载中...");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }

}
