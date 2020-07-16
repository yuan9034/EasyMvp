package com.xdja.easymvp.di.component

import com.google.gson.Gson
import com.xdja.easymvp.di.module.GlobalConfigModule
import com.xdja.easymvp.http.imageloader.ImageLoader
import com.xdja.easymvp.integration.IRepositoryManager
import com.xdja.easymvp.integration.cache.Cache
import com.xdja.easymvp.integration.lifecycle.ActivityLifecycle
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import retrofit2.Retrofit
import java.io.File

/**
 * @author yuanwanli
 * @des   App全局注入向外提供类
 * @date 2020/6/22
 */
class AppComponent() : KoinComponent {
    lateinit var iRepositoryManager: IRepositoryManager
    lateinit var imageLoader: ImageLoader<*>
    lateinit var okHttpClient: OkHttpClient
    lateinit var gson: Gson
    lateinit var cacheFile: File
    lateinit var extras: Cache<String, Any>
    lateinit var cacheFactory: Cache.Factory
    lateinit var mActivityLifecycle: ActivityLifecycle
    fun inject(globalConfigModule: GlobalConfigModule) {
        gson = get { parametersOf(globalConfigModule.provideGsonConfiguration()) }
        cacheFactory =
            get(named("CacheFactory")) { parametersOf(globalConfigModule.provideCacheFactory(get())) }
        cacheFile =
            get(named("CacheFile")) { parametersOf(globalConfigModule.provideCacheFile(get())) }
        extras =
            get(named("extras")) { parametersOf(globalConfigModule.provideCacheFactory(get())) }
        imageLoader =
            get(named("ImageLoader")) { parametersOf(globalConfigModule.provideImageLoaderStrategy()) }
        val interceptor = get<Interceptor> {
            parametersOf(
                globalConfigModule.providerGlobalHttpHandler(),
                globalConfigModule.provideFormatPrinter(),
                globalConfigModule.providePrintHttpLogLevel()
            )
        }

        okHttpClient = get {
            parametersOf(
                globalConfigModule.provideOKHttpConfiguration(),
                interceptor
                ,
                globalConfigModule.providerInterceptors(),
                globalConfigModule.providerGlobalHttpHandler()
            )
        }
        val retrofit = get<Retrofit> {
            parametersOf(
                globalConfigModule.provideRetrofitConfiguration(),
                globalConfigModule.providerBaseUrl()
            )
        }
        mActivityLifecycle = get { parametersOf(extras) }
        iRepositoryManager = get { parametersOf(retrofit, cacheFactory, null) }
    }
}