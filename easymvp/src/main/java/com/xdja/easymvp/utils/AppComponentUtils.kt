package com.xdja.easymvp.utils

import android.content.Context
import com.xdja.easymvp.base.App
import com.xdja.easymvp.di.component.AppComponent

/**
 * @author yuanwanli
 * @des   AppComponent工具类
 * @date 2020/6/30
 */
object AppComponentUtils {
    /**
     * 通过Context拿到全局的AppComponent
     */
    fun getAppComponentFromContext(context: Context?): AppComponent {
        if (context == null) {
            throw NullPointerException("${Context::class.java.name} cannot be null")
        }
        val app = context.applicationContext
        if (app is App) {
            return app.getAppComponent()
        } else {
            throw IllegalStateException("${app!!.javaClass.name} must be implements ${App::javaClass.name}")
        }
    }
}