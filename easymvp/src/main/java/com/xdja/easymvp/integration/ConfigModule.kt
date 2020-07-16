package com.xdja.easymvp.integration

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.xdja.easymvp.base.delegate.AppLifeCycles
import com.xdja.easymvp.di.module.GlobalConfigModule

/**
 * @author yuanwanli
 * @des   可以给框架配置一些参数,需要实现[ConfigModule]后,在AndroidManifest中声明该实现类
 * @date 2020/7/3
 */
interface ConfigModule {
    /**
     * 使用[GlobalConfigModule.Builder]给框架配置一些参数
     */
    fun applyOptions(context: Context, builder: GlobalConfigModule.Builder)

    /**
     * 使用[AppLifeCycles]在Application的生命周期注入一些操作
     * @param lifeCycles Application的生命周期容器,可向框架中添加多个Application的生命周期类
     */
    fun injectAppLifecycle(context: Context, lifeCycles: MutableList<AppLifeCycles>)

    /**
     * 使用[Application.ActivityLifecycleCallbacks]在Activity的生命周期中注入一些操作
     * @param context
     * @param lifeCycles Activity的生命周期容器,可向框架中添加多个Activity的生命周期类
     */
    fun injectActivityLifecycle(
        context: Context,
        lifeCycles: MutableList<Application.ActivityLifecycleCallbacks>
    )

    /**
     * 使用[FragmentManager.FragmentLifecycleCallbacks]在Fragment的生命周期中注入一些操作
     * @param context
     * @param lifeCycles Fragment的生命周期容器,可向框架中添加多个Fragment生命周期类
     */
    fun injectFragmentLifecycle(
        context: Context,
        lifeCycles: MutableList<FragmentManager.FragmentLifecycleCallbacks>
    )
}