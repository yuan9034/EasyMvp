package com.xdja.easymvp.base.delegate

import android.app.Application
import android.content.Context

/**
 * @author yuanwanli
 * @des   用于代理[Application]的生命周期
 * @see AppDelegate
 * @date 2020/6/24
 */
interface AppLifeCycles {
    fun attachBaseContext(base: Context)
    fun onCreate(application: Application)
    fun onTerminate(application: Application)
}