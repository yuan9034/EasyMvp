package com.xdja.easymvp.di

import com.xdja.easymvp.di.module.EasyMvpAppModule
import com.xdja.easymvp.di.module.EasyMvpClientModule

/**
 * @author yuanwanli
 * @des   EasyMvp全局注入
 * @date 2020/6/29
 */
object EasyMvpModule {
    val theLibModule = EasyMvpAppModule + EasyMvpClientModule
}