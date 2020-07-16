package com.xdja.easymvp.base

import com.xdja.easymvp.di.component.AppComponent

/**
 * @author yuanwanli
 * @des   框架要求每个[android.app.Application] 都需要实现此类,以满足规范
 * @see BaseApplication
 * @date 2020/6/22
 */
interface App {
    fun getAppComponent(): AppComponent
}