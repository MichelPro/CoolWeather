package com.michel.coolweather.entity

/**
 * Created by Michel on 2017/6/15.
 */
data class AQI(
        var city: AQICity
){
    data class AQICity(
            var aqi: String,
            var pm25: String
    )
}