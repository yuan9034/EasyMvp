package com.xdja.easymvp.base

import android.app.Application
import android.content.ComponentCallbacks2
import android.content.res.Configuration
import com.xdja.easymvp.di.component.AppComponent

/**
 * @author yuanwanli
 * @des   [ComponentCallbacks2] 是一个细粒度的内存回收管理回调
 * Application、Activity、Service、ContentProvider、Fragment实现了 [ComponentCallbacks2]接口
 * 开发者应该实现 [ComponentCallbacks2.onTrimMemory]方法, 细粒度 release 内存, 参数的值不同可以体现出不同程度的内存可用情况
 * 响应 [ComponentCallbacks2.onTrimMemory]回调, 开发者的 App 会存活的更持久, 有利于用户体验
 * 不响应 [ComponentCallbacks2.onTrimMemory]回调, 系统 kill 掉进程的几率更大
 * @date 2020/7/6
 */
class AppComponentCallbacks(
    mApplication: Application,
    mAppComponent: AppComponent?
) : ComponentCallbacks2 {
    override fun onLowMemory() {

    }

    override fun onConfigurationChanged(newConfig: Configuration) {

    }

    override fun onTrimMemory(level: Int) {

    }

}