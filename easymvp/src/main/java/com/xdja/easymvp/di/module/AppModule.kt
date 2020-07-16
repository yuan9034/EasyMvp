package com.xdja.easymvp.di.module

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.google.gson.GsonBuilder
import com.xdja.easymvp.http.GlobalHttpHandler
import com.xdja.easymvp.http.imageloader.BaseImageLoaderStrategy
import com.xdja.easymvp.http.imageloader.ImageLoader
import com.xdja.easymvp.http.log.FormatPrinter
import com.xdja.easymvp.http.log.RequestInterceptor
import com.xdja.easymvp.integration.IRepositoryManager
import com.xdja.easymvp.integration.RepositoryManager
import com.xdja.easymvp.integration.cache.Cache
import com.xdja.easymvp.integration.cache.CacheType
import com.xdja.easymvp.integration.lifecycle.ActivityLifecycle
import com.xdja.easymvp.integration.lifecycle.FragmentLifecycle
import okhttp3.Interceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import java.io.File

/**
 * @author yuanwanli
 * @des
 * @date 2020/7/7
 */
interface GsonConfiguration {
    fun configGson(context: Context, builder: GsonBuilder)
}

val EasyMvpAppModule = module {
    //提供Gson单例
    single { (configuration: GsonConfiguration?) ->
        val builder = GsonBuilder()
        configuration?.apply {
            configGson(get(), builder)
        }
        return@single builder.create()
    }
    //提供Cache单例
    single(named("extras")) { (cacheFactory: Cache.Factory) ->
        return@single cacheFactory.build(CacheType.EXTRAS) as Cache<String, Any>
    }
    //提供List<FragmentManager.FragmentLifecycleCallbacks单例
    single<MutableList<FragmentManager.FragmentLifecycleCallbacks>> {
        return@single mutableListOf()
    }
    //提供IRepositoryManager单例
    single<IRepositoryManager> { (mRetrofit: Retrofit, mCacheFactory: Cache.Factory,
                                     mObtainServiceDelegate: IRepositoryManager.ObtainServiceDelegate?) ->
        return@single RepositoryManager(mRetrofit, get(), mCacheFactory, mObtainServiceDelegate)
    }
    //提供ImageLoader单例
    single<ImageLoader<*>>(named("ImageLoader")) { (mStrategy: BaseImageLoaderStrategy<*>) ->
        ImageLoader(mStrategy)
    }
    //提供全局缓存file
    single(named("CacheFile")) { (file: File) ->
        return@single file
    }
    single(named("CacheFactory")) { (factory: Cache.Factory) ->
        return@single factory
    }
    single<Interceptor> { (mHandler: GlobalHttpHandler, mPrinter: FormatPrinter, printLevel: RequestInterceptor.Level) ->
        return@single RequestInterceptor(mHandler, mPrinter, printLevel)
    }
    single<FragmentLifecycle> { FragmentLifecycle() }
    single<ActivityLifecycle> { (mExtras: Cache<String, Any>) ->
        val fragmentLifecycle = get<FragmentLifecycle>()
        val fragmentLifeCycles = get<MutableList<FragmentManager.FragmentLifecycleCallbacks>>()
        ActivityLifecycle(get(), mExtras, fragmentLifecycle, fragmentLifeCycles)
    }
}