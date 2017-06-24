package com.michel.coolweather.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import com.michel.coolweather.R
import com.michel.coolweather.activity.WeatherActivity
import com.michel.coolweather.base.BaseFragment
import com.michel.coolweather.entity.City
import com.michel.coolweather.entity.County
import com.michel.coolweather.entity.Province
import com.michel.coolweather.other.Constant
import com.michel.coolweather.other.setVisible
import com.michel.coolweather.utils.Utility
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import okhttp3.Call
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.litepal.crud.DataSupport
import java.lang.Exception

/**
 * 地区选择页面
 * Created by Michel on 2017/6/15.
 */
class ChooseAreaFragment : BaseFragment() {

    val LEVEL_PROVINCE: Int = 0
    val LEVEL_CITY: Int = 1
    val LEVEL_COUNTY: Int = 2

    lateinit var titleText: TextView
    lateinit var backButton: Button
    lateinit var listView: ListView
    lateinit var adapter: ArrayAdapter<String>
    var progressDialog: ProgressDialog? = null

    var dataList: ArrayList<String> = ArrayList()

    lateinit var provinceList: List<Province>   // 省列表
    lateinit var cityList: List<City>           // 市列表
    lateinit var countyList: List<County>       // 县列表

    var currentLevel: Int = 0                   // 当前选中的级别
    lateinit var selectedProvince: Province     // 选中的省份
    lateinit var selectedCity: City             // 选中的城市


    override fun getLayoutResId(): Int = R.layout.choose_area

    override fun initViews(view: View, savedInstanceState: Bundle?) {
        titleText = view.find(R.id.title_text)
        backButton = view.find(R.id.back_button)
        listView = view.find(R.id.list_view)
        adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, dataList)
        listView.adapter = adapter
    }

    override fun initData(savedInstanceState: Bundle?) {

        queryProvinces()

        // 列表条目点击跳转下页数据
        listView.setOnItemClickListener { _, _, position, _ ->
            when (currentLevel) {
                LEVEL_PROVINCE -> {
                    // 获取当前选中省
                    selectedProvince = provinceList[position]
                    // 当前为省，跳转到市
                    queryCity()
                }
                LEVEL_CITY -> {
                    // 获取当前选中市
                    selectedCity = cityList[position]
                    // 当前为市，跳转到县
                    queryCounty()
                }
                LEVEL_COUNTY -> {
                    val weatherId = countyList[position].weatherId
                    startActivity<WeatherActivity>("weather_id" to weatherId)
                    activity.finish()
                }
            }
        }

        // 按下返回键返回上页数据
        backButton.setOnClickListener {
            when (currentLevel) {
                LEVEL_COUNTY -> {
                    // 当前为县，返回到市
                    queryCity()
                }
                LEVEL_CITY -> {
                    // 当前为市，返回到省
                    queryProvinces()
                }
            }
        }
    }

    /**
     * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private fun queryProvinces() {
        titleText.text = "中国"
        backButton.setVisible(false)
        provinceList = DataSupport.findAll(Province::class.java)
        if (provinceList.isNotEmpty()) {
            dataList.clear()
            provinceList.forEach {
                dataList.add(it.provinceName)
            }
            adapter.notifyDataSetChanged()
            listView.setSelection(0)
            currentLevel = LEVEL_PROVINCE
        } else {
            val address = Constant.URL_ADDRESS
            queryFromServer(address, "province")
        }
    }

    /**
     * 查询选中省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private fun queryCity() {
        titleText.text = selectedProvince.provinceName
        backButton.setVisible(true)
        cityList = DataSupport.where("provinceid = ?", selectedProvince.id.toString()).find(City::class.java)
        if (cityList.isNotEmpty()) {
            dataList.clear()
            cityList.forEach {
                dataList.add(it.cityName)
            }
            adapter.notifyDataSetChanged()
            listView.setSelection(0)
            currentLevel = LEVEL_CITY
        } else {
            val provinceCode = selectedProvince.provinceCode
            val address = Constant.URL_ADDRESS + "/$provinceCode"
            queryFromServer(address, "city")
        }
    }

    /**
     * 查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private fun queryCounty() {

        titleText.text = selectedCity.cityName
        backButton.setVisible(true)
        countyList = DataSupport.where("cityid = ?", selectedCity.id.toString()).find(County::class.java)
        if (countyList.isNotEmpty()) {
            dataList.clear()
            countyList.forEach {
                dataList.add(it.countyName)
            }
            adapter.notifyDataSetChanged()
            listView.setSelection(0)
            currentLevel = LEVEL_COUNTY
        } else {
            val provinceCode = selectedProvince.provinceCode
            val cityCode = selectedCity.cityCode
            val address = Constant.URL_ADDRESS + "/$provinceCode/$cityCode"
            queryFromServer(address, "county")
        }
    }

    /**
     * 根据传入的地址和类型从服务器上查询省市县数据
     */
    private fun queryFromServer(address: String, type: String) {
        showProgressDialog()
        OkHttpUtils.get().url(address).build().execute(object :StringCallback(){
            override fun onResponse(response: String, id: Int) {
                var result = false
                when (type) {
                    "province" -> {
                        result = Utility.handleProvinceResponse(response)
                    }
                    "city" ->{
                        result = Utility.handleCityResponse(response, selectedProvince.id)
                    }
                    "county" ->{
                        result = Utility.handleCountyResponse(response, selectedCity.id)
                    }
                }
                // 如果解析成功，并保存在本地
                if (result) {
                    closeProgressDialog()
                    when (type) {
                        "province" -> {
                            queryProvinces()
                        }
                        "city" ->{
                            queryCity()
                        }
                        "county" ->{
                            queryCounty()
                        }
                    }
                }
            }

            override fun onError(call: Call?, e: Exception?, id: Int) {
                closeProgressDialog()
                toast("加载失败")
            }

        })
    }

    private fun showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(context)
            progressDialog!!.setMessage("正在加载")
            progressDialog!!.setCanceledOnTouchOutside(false)
        }
        progressDialog!!.show()
    }

    private fun closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog?.dismiss()
        }
    }
}