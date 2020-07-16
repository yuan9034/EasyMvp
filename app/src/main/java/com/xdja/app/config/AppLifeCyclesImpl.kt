package com.xdja.app.config

import android.app.Application
import android.content.Context
import com.xdja.easymvp.base.delegate.AppLifeCycles

/**
 * @author yuanwanli
 * @des   展示[AppLifeCycles]的用法
 * @date 2020/7/6
 */
class AppLifeCyclesImpl : AppLifeCycles {
    override fun attachBaseContext(base: Context) {

    }

    override fun onCreate(application: Application) {

    }

    override fun onTerminate(application: Application) {

    }
}