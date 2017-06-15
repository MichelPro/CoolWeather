package com.michel.coolweather.entity

import com.google.gson.annotations.SerializedName

/**
 * Created by Michel on 2017/6/15.
 */
data class Forecast(
        var date: String,

        @SerializedName("tmp")
        var temperature: Temperature,

        @SerializedName("cond")
        var more: More
){
    data class Temperature(
            var max: String,

            var min: String
    )
    data class More(
            @SerializedName("txt_d")
            var info: String
    )
}