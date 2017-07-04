package com.michel.coolweather.other

/**
 * 常量
 * Created by Michel on 2017/6/15.
 */
interface Constant {
    companion object {
        // 查询地址API
        val URL_ADDRESS = "http://guolin.tech/api/china"
        // 查询天气API
        val URL_WEATHER = "http://guolin.tech/api/weather"
        // 查询天气KEY
        val KEY_WEATHER = "e40beffb07fc4a0b8c94a6cdb07041df"
        // 获取必应每日一图
        val URL_BING_PIC = "http://guolin.tech/api/bing_pic"
    }
}
