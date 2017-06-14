package com.michel.coolweather.entity

import org.litepal.crud.DataSupport

/**
 * Created by Michel on 2017/6/14.
 */
data class City(
        var id: Int,
        var cityName: String,
        var cityCode: Int,
        var provinceId: Int
): DataSupport()