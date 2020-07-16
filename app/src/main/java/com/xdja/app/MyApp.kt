package com.xdja.app

import com.xdja.app.di.MvpModule
import com.xdja.easymvp.base.BaseApplication
import com.xdja.easymvp.di.EasyMvpModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * @author yuanwanli
 * @des
 * @date 2020/6/29
 */
class MyApp : BaseApplication() {
    override fun onCreate() {
        startKoin {
            //logger
            androidLogger(Level.DEBUG)
            //android context
            androidContext(this@MyApp)
            //读取配置文件
            androidFileProperties(koinPropertyFile = "address.properties")
            //modules
            modules(MvpModule)
        }
        //启动startKoin 后 可以使用
        loadKoinModules(EasyMvpModule.theLibModule)
        super.onCreate()


    }
}