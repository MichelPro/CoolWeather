package com.michel.coolweather

import android.app.Application
import android.content.Context
import org.litepal.LitePal
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.log.LoggerInterceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


/**
 * Created by Michel on 2017/6/13.
 */
class App: Application(){

    companion object{
        lateinit var context: Context

        fun getAppContext(): Context = context
    }

    override fun onCreate() {
        super.onCreate()

        context = this

        initLitePal()

        initOkHttpUtil()
    }

    /**
     * 初始化数据库库
     */
    private fun initLitePal() {
        LitePal.initialize(this)
    }

    private fun initOkHttpUtil() {
        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build()

        OkHttpUtils.initClient(okHttpClient)
    }
}