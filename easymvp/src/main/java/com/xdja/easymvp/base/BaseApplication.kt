package com.xdja.easymvp.base

import android.app.Application
import android.content.Context
import com.xdja.easymvp.base.delegate.AppDelegate
import com.xdja.easymvp.base.delegate.AppLifeCycles
import com.xdja.easymvp.di.component.AppComponent

/**
 * @author yuanwanli
 * @des MVPArms 是一个整合了大量主流开源项目的 Android MVP 快速搭建框架, 其中包含 Retrofit、协程、Room,
 * 本框架将它们结合起来, 并全部使用 Koin 管理并提供给开发者使用, 使用本框架开发您的项目,
 * 就意味着您已经拥有一个 MVP + koin + Retrofit +Room 协程 项目
 * @date 2020/6/24
 */
open class BaseApplication : Application(), App {
    private var mAppDelegate: AppLifeCycles? = null

    /**
     * 这里会在[BaseApplication.onCreate]之前被调用,可以做一些较早的初始化
     * 常用于MultiDex以及插件化的初始化
     */
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        if (mAppDelegate == null) {
            mAppDelegate = AppDelegate(base)
        }
        mAppDelegate!!.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        mAppDelegate?.onCreate(this)
    }

    /**
     * 在模拟环境中程序终止时会被调用
     */
    override fun onTerminate() {
        super.onTerminate()
        mAppDelegate?.onTerminate(this)
    }

    /**
     * 将 [AppComponent] 返回出去, 供其它地方使用, [AppComponent]接口中声明的方法所返回的实例, 在 [getAppComponent] 拿到对象后都可以直接使用
     * @return AppComponent
     */
    override fun getAppComponent(): AppComponent {
        if (mAppDelegate == null) {
            throw NullPointerException("${AppDelegate::class.java.name} cannot be null")
        }
        val app = mAppDelegate
        if (app is App) {
            return app.getAppComponent()
        } else {
            throw IllegalStateException("${mAppDelegate!!.javaClass.simpleName} must be implements ${App::javaClass.name}")
        }
    }
}