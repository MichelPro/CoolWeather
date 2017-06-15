package com.michel.coolweather.entity

import com.google.gson.annotations.SerializedName

/**
 * Created by Michel on 2017/6/15.
 */
data class Suggestion(
        @SerializedName("comf")
        var comfort: Comfort,

        @SerializedName("cw")
        var carWash: CarWash,

        var sport: Sport
) {
    data class Comfort(
            @SerializedName("txt")
            var info: String
    )

    data class CarWash(
            @SerializedName("txt")
            var info: String
    )

    data class Sport(
            @SerializedName("txt")
            var info: String
    )
}