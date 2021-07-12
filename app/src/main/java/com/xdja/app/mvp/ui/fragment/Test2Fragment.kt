package com.xdja.app.mvp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.LogUtils
import com.xdja.app.R
import com.xdja.app.mvp.presenter.TestPresenter
import com.xdja.easymvp.base.BaseFragment

class Test2Fragment:BaseFragment<TestPresenter>() {

    companion object{
        fun newInstance():Test2Fragment {
            val args = Bundle()

            val fragment = Test2Fragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initView(
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        LogUtils.e("initview")
        return  layoutInflater.inflate(R.layout.activity_test,null)
    }

    override fun initData(savedInstanceState: Bundle?) {
        LogUtils.e("Test2Fragment")
    }

    override fun setData(data: Any?) {

    }
}