package com.michel.coolweather.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.v4.content.SharedPreferencesCompat
import com.michel.coolweather.App
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.*

/**
 * Created by Michel on 2017/6/23.
 */
class SpUtils private constructor() {


    val FILE_NAME = "app_data"
    var sp: SharedPreferences
    var editor: SharedPreferences.Editor

    init {
        sp = App.getAppContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        editor = sp.edit()
    }

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
            SpUtils()
        }

    }

    fun put(key: String, value: Any) {
        when (value) {
            is String -> {
                editor.putString(key, value)
            }
            is Int -> {
                editor.putInt(key, value)
            }
            is Boolean -> {
                editor.putBoolean(key, value)
            }
            is Float ->{
                editor.putFloat(key, value)
            }
            is Long ->{
                editor.putLong(key, value)
            }
            else -> {
                editor.putString(key, value.toString())
            }
        }
        // 使用自定义兼容类
        SharedPreferencesCompat.apply(editor)
    }

    fun get(key: String, defaultVal: Any): Any? {
        when (defaultVal) {
            is String -> {
                return sp.getString(key, defaultVal)
            }
            is Int->{
                return sp.getInt(key, defaultVal)
            }
            is Boolean ->{
                return sp.getBoolean(key, defaultVal)
            }
            is Float ->{
                return sp.getFloat(key, defaultVal)
            }
            is Long ->{
                return sp.getLong(key, defaultVal)
            }
            else -> {
                return null
            }
        }
    }

    class SharedPreferencesCompat{

        companion object{

            private val sApplyMethod = findApplyMethod()

            /**
             * 反射查找apply的方法

             * @return
             */
            private fun findApplyMethod(): Method? {
                try {
                    val clz = SharedPreferences.Editor::class.java
                    return clz.getMethod("apply")
                } catch (e: NoSuchMethodException) {
                }

                return null
            }

            /**
             * 如果找到则使用apply执行，否则使用commit

             * @param editor
             */
            fun apply(editor: SharedPreferences.Editor) {
                try {
                    if (sApplyMethod != null) {
                        sApplyMethod.invoke(editor)
                        return
                    }
                } catch (e: IllegalArgumentException) {
                } catch (e: IllegalAccessException) {
                } catch (e: InvocationTargetException) {
                }

                editor.commit()
            }
        }
    }
}