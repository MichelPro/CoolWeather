package com.michel.coolweather.other

import android.view.View
import org.json.JSONArray
import org.json.JSONObject


/**
 * JSONArray的扩展方法-变成可迭代变量
 */
operator fun JSONArray.iterator(): Iterator<JSONObject> = object : Iterator<JSONObject> {

    private var index = 0

    override fun hasNext(): Boolean = index < length()

    override fun next(): JSONObject = get(index++) as JSONObject
}

/**
 * View的扩展方法-设置View的可见性
 */
fun View.setVisible(isVisible: Boolean){
    if (isVisible) {
        visibility = View.VISIBLE
    } else {
        visibility = View.INVISIBLE
    }
}