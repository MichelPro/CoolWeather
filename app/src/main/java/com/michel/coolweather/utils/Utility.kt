package com.michel.coolweather.utils

import android.text.TextUtils
import com.google.gson.Gson
import com.michel.coolweather.entity.City
import com.michel.coolweather.entity.County
import com.michel.coolweather.entity.Province
import com.michel.coolweather.entity.Weather
import com.michel.coolweather.other.iterator
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * 解析处理服务器返回的地域信息
 * Created by Michel on 2017/6/14.
 */
class Utility {

    companion object{

        /**
         * 将返回的JSON数据解析成weather实体类
         */
        fun handleWeatherResponse(response: String): Weather{
            val weatherContent = JSONObject(response).getJSONArray("HeWeather").getJSONObject(0).toString()
            return Gson().fromJson(weatherContent, Weather::class.java)
        }

        /**
         * 解析和处理服务器返回的省级数据
         */
        fun handleProvinceResponse(response: String): Boolean{

            if (!TextUtils.isEmpty(response)) {
                try {
                    JSONArray(response).iterator().forEach {
                        Province(it.getInt("id"),it.getString("name"),it.getInt("id")).save()
                    }
                    return true
                }catch (e: JSONException){
                    e.printStackTrace()
                }
            }
            return false
        }

        /**
         * 解析和处理服务器返回的市级数据
         */
        fun handleCityResponse(response: String, provinceId: Int): Boolean{

            if (!TextUtils.isEmpty(response)) {
                try {
                    JSONArray(response).iterator().forEach {
                        City(it.getInt("id"),it.getString("name"),it.getInt("id"), provinceId).save()
                    }
                    return true
                }catch (e: JSONException){
                    e.printStackTrace()
                }
            }
            return false
        }

        /**
         * 解析和处理服务器返回的县级数据
         */
        fun handleCountyResponse(response: String, cityId: Int): Boolean{

            if (!TextUtils.isEmpty(response)) {
                try {
                    JSONArray(response).iterator().forEach {
                        County(it.getInt("id"),it.getString("name"),it.getString("weather_id"), cityId).save()
                    }
                    return true
                }catch (e: JSONException){
                    e.printStackTrace()
                }
            }
            return false
        }
    }


}


