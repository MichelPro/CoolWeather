package com.michel.coolweather.activity


import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.michel.coolweather.R
import com.michel.coolweather.base.BaseActivity
import com.michel.coolweather.entity.Weather
import com.michel.coolweather.other.Constant
import com.michel.coolweather.other.setVisible
import com.michel.coolweather.utils.SpUtils
import com.michel.coolweather.utils.Utility
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import okhttp3.Call
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import java.lang.Exception

class WeatherActivity : BaseActivity(), AnkoLogger {

    lateinit var weatherLayout: ScrollView
    lateinit var titleCity: TextView
    lateinit var titleUpdateTime: TextView
    lateinit var degreeText: TextView
    lateinit var weatherInfoText: TextView
    lateinit var forecastLayout: LinearLayout
    lateinit var aqiText: TextView
    lateinit var pm25Text: TextView
    lateinit var comfortText: TextView
    lateinit var carWashText: TextView
    lateinit var sportText: TextView



    override fun initVariables() {    }

    override fun getLayoutResId(): Int = R.layout.activity_weather


    override fun initViews(savedInstanceState: Bundle?) {
        weatherLayout = find(R.id.weather_layout)
        titleCity = find(R.id.title_city)
        titleUpdateTime = find(R.id.title_update_time)
        degreeText = find(R.id.degree_text)
        weatherInfoText = find(R.id.weather_info_text)
        forecastLayout = find(R.id.forecast_layout)
        aqiText = find(R.id.api_text)
        pm25Text = find(R.id.pm25_text)
        comfortText = find(R.id.comfort_text)
        carWashText = find(R.id.car_wash_text)
        sportText = find(R.id.sport_text)
    }

    override fun initData() {

        val weatherString = SpUtils.instance.get("weather", "") as String
        if (weatherString.isNotEmpty()) {
            // 有缓存时直接解析天气数据
            val weather = Utility.handleWeatherResponse(weatherString)
            showWeatherInfo(weather)
        } else {
            // 无缓存时去服务器查询天气
            val weatherId = intent.getStringExtra("weather_id")
            weatherLayout.setVisible(false)
            requestWeather(weatherId)

        }
    }

    /**
     * 根据天气id请求天气信息
     */
    private fun requestWeather(weatherId: String?) {
        OkHttpUtils.get().url(Constant.URL_WEATHER).addParams("cityid", weatherId)
                .addParams("key", Constant.KEY_WEATHER)
                .build().execute(object : StringCallback(){
            override fun onResponse(response: String, id: Int) {
                debug("根据天气id请求天气信息结果是$response")
                val weather = Utility.handleWeatherResponse(response)
                if (weather != null && "ok" == weather.status) {
                    SpUtils.instance.put("weather", response)
                    showWeatherInfo(weather)
                } else {
                    toast("获取天气信息失败")
                }
            }

            override fun onError(call: Call?, e: Exception, id: Int) {
                e.printStackTrace()
                toast("获取天气信息失败")
            }

        })
    }

    /**
     * 处理并展示Weather实体类中的数据
     */
    private fun showWeatherInfo(weather: Weather) {
        val cityName = weather.basic.cityName
        val updateTime = weather.basic.update.updateTime.split(" ")[1]
        val degree = weather.now.temperature + "℃"
        val weatherInfo = weather.now.more.info
        titleCity.text = cityName
        titleUpdateTime.text = updateTime
        degreeText.text = degree
        weatherInfoText.text = weatherInfo
        forecastLayout.removeAllViews()

        weather.forecastList.forEach {
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false)

            val dateText = view.find<TextView>(R.id.date_text)
            val infoText = view.find<TextView>(R.id.info_text)
            val maxText = view.find<TextView>(R.id.max_text)
            val minText = view.find<TextView>(R.id.min_text)

            dateText.text = it.date
            infoText.text = it.more.info
            maxText.text = it.temperature.max
            minText.text = it.temperature.min

            forecastLayout.addView(view)
        }

        weather.aqi?.let {
            aqiText.text = it.city.aqi
            pm25Text.text = it.city.pm25
        }

        val comfort = "舒适度：${weather.suggestion.comfort.info}"
        val carWash = "洗车指数：${weather.suggestion.carWash.info}"
        val sport = "运动建议：${weather.suggestion.sport.info}"

        comfortText.text = comfort
        carWashText.text = carWash
        sportText.text = sport

        weatherLayout.setVisible(true)
    }

}
