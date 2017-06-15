package com.michel.coolweather.entity

import com.google.gson.annotations.SerializedName

/**
 * Created by Michel on 2017/6/15.
 */
data class Now(
        @SerializedName("tmp")
        var temperature: String,

        @SerializedName("cond")
        var more: More
){
    data class More(
            @SerializedName("txt")
            var info: String
    )
}