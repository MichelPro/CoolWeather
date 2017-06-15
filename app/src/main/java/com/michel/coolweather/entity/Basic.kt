package com.michel.coolweather.entity

import com.google.gson.annotations.SerializedName

/**
 * Created by Michel on 2017/6/15.
 */
data class Basic(
        @SerializedName("city")
        var cityName: String,

        @SerializedName("id")
        var weatherId: String,

        var update: Update
) {
    data class Update(
            @SerializedName("loc")
            var updateTime: String
    )
}