package com.michel.coolweather.entity

import org.litepal.crud.DataSupport

/**
 * Created by Michel on 2017/6/14.
 */
data class Province(
        var id: Int,
        var provinceName: String,
        var provinceCode: Int
): DataSupport()