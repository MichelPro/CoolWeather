package com.michel.coolweather.entity

import org.litepal.crud.DataSupport

/**
 * Created by Michel on 2017/6/14.
 */
data class County(
        var id: Int,
        var countyName: String,
        var weatherId: String,
        var cityId: Int
): DataSupport()