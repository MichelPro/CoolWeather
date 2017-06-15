package com.michel.coolweather.entity

import com.google.gson.annotations.SerializedName

/**
 * Created by Michel on 2017/6/16.
 */
data class Weather(

        var status: String,

        var basic: Basic,

        var aqi: AQI,

        var now: Now,

        var suggestion: Suggestion,

        @SerializedName("daily_forecast")
        var forecastList: List<Forecast>
)