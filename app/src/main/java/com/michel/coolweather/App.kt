package com.michel.coolweather

import android.app.Application
import org.litepal.LitePal

/**
 * Created by Michel on 2017/6/13.
 */
class App: Application(){

    override fun onCreate() {
        super.onCreate()
        initLitePal()
    }

    /**
     * 初始化数据库库
     */
    private fun initLitePal() {
        LitePal.initialize(this)
    }
}