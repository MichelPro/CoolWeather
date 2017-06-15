package com.michel.coolweather.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 *
 * Created by Michel on 2017/6/14.
 */
abstract class BaseFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getLayoutResId(), container, false)

        initViews(view, savedInstanceState)

        initVariate()

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData(savedInstanceState)
    }

    abstract fun getLayoutResId(): Int

    abstract fun initViews(view: View, savedInstanceState: Bundle?)

    open fun initVariate() {}

    abstract fun initData(savedInstanceState: Bundle?)
}