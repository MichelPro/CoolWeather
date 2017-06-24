package com.michel.coolweather.activity

import android.os.Bundle
import com.michel.coolweather.R
import com.michel.coolweather.base.BaseActivity
import com.michel.coolweather.utils.SpUtils
import org.jetbrains.anko.startActivity

class MainActivity : BaseActivity() {

    override fun getLayoutResId(): Int = R.layout.activity_main

    override fun initVariables() {

    }

    override fun initViews(savedInstanceState: Bundle?) {
    }

    override fun initData() {
        val weather = SpUtils.instance.get("weather", "") as String
        if (weather.isNotEmpty()) {
            startActivity<WeatherActivity>()
            finish()
        }
    }

}
