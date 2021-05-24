package com.xdja.easymvp.di.module

import android.app.Application
import com.xdja.easymvp.http.BaseUrl
import com.xdja.easymvp.http.GlobalHttpHandler
import com.xdja.easymvp.http.imageloader.BaseImageLoaderStrategy
import com.xdja.easymvp.http.log.DefaultFormatPrinter
import com.xdja.easymvp.http.log.FormatPrinter
import com.xdja.easymvp.http.log.RequestInterceptor
import com.xdja.easymvp.integration.IRepositoryManager
import com.xdja.easymvp.integration.cache.Cache
import com.xdja.easymvp.integration.cache.CacheType
import com.xdja.easymvp.integration.cache.IntelligentCache
import com.xdja.easymvp.integration.cache.LruCache
import okhttp3.HttpUrl
import okhttp3.Interceptor
import java.io.File

/**
 * @author yuanwanli
 * @des   框架独创的建造者模式,可向框架中注入外部配置的自定义参数
 * @date 2020/7/3
 */
class GlobalConfigModule(builder: Builder) {
    private var mApiUrl: HttpUrl? = builder.mApiUrl
    private val mBaseUrl: BaseUrl? = builder.mBaseUrl
    private var mLoaderStrategy: BaseImageLoaderStrategy<*>? = builder.mLoaderStrategy
    private var mHandler: GlobalHttpHandler? = builder.mHandler
    private var mInterceptors: List<Interceptor>? = builder.mInterceptors
    private var mCacheFile: File? = builder.mCacheFile
    private var mRetrofitConfiguration: RetrofitConfiguration? =
        builder.mRetrofitConfiguration
    private var mOKHttpConfiguration: OKHttpConfiguration? =
        builder.mOKHttpConfiguration
    private var mGsonConfiguration: GsonConfiguration? = builder.mGsonConfiguration
    private var mPrintHttpLogLevel: RequestInterceptor.Level? = builder.mPrintHttpLogLevel
    private var mFormatPrinter: FormatPrinter? = builder.mFormatPrinter
    private var mCacheFactory: Cache.Factory? = builder.mCacheFactory
    private var mObtainServiceDelegate: IRepositoryManager.ObtainServiceDelegate? =
        builder.mObtainServiceDelegate

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }

    /**
     * 网络拦截器
     */
    fun providerInterceptors(): List<Interceptor>? = mInterceptors

    /**
     * 联网的BaseUrl
     */
    fun providerBaseUrl(): HttpUrl {
        if (mBaseUrl != null) {
            val url = mBaseUrl.url()
            if (url != null) {
                return url
            }
        }
        return (if (mApiUrl == null) HttpUrl.parse("https://api.github.com/") else mApiUrl)!!
    }

    /**
     * 图片加载器
     */
    fun provideImageLoaderStrategy(): BaseImageLoaderStrategy<*>? = mLoaderStrategy

    /**
     * 处理Http请求和响应结果的处理类
     */
    fun providerGlobalHttpHandler(): GlobalHttpHandler? = mHandler

    /**
     * 提供缓存文件
     */
    fun provideCacheFile(application: Application): File {
        return (if (mCacheFile == null) application.cacheDir else mCacheFile)!!
    }

    /**
     * 提供Retrofit配置
     */
    fun provideRetrofitConfiguration(): RetrofitConfiguration? = mRetrofitConfiguration

    /**
     * 提供OKHttp配置
     */
    fun provideOKHttpConfiguration(): OKHttpConfiguration? = mOKHttpConfiguration

    /**
     * 提供gson配置
     */
    fun provideGsonConfiguration(): GsonConfiguration? = mGsonConfiguration

    /**
     * 提供http打印级别
     */
    fun providePrintHttpLogLevel(): RequestInterceptor.Level {
        return if (mPrintHttpLogLevel == null) RequestInterceptor.Level.ALL else mPrintHttpLogLevel!!
    }

    /**
     * http日志打印类
     */
    fun provideFormatPrinter(): FormatPrinter {
        return (if (mFormatPrinter == null) DefaultFormatPrinter() else mFormatPrinter)!!
    }

    /**
     * 提供缓存工厂(根据CacheType提供缓存策略)
     */
    fun provideCacheFactory(application: Application): Cache.Factory {
        return if (mCacheFactory == null) {
            object : Cache.Factory {
                override fun build(type: CacheType): Cache<String, Any> = when (type.getCacheTypeId()) {
                    CacheType.EXTRAS_TYPE_ID,
                    CacheType.ACTIVITY_CACHE_TYPE_ID,
                    CacheType.FRAGMENT_CACHE_TYPE_ID -> IntelligentCache<Any>(
                        type.calculateCacheSize(
                            application
                        )
                    )
                    else -> LruCache<String, Any>(type.calculateCacheSize(application))
                }
            }
        } else {
            mCacheFactory!!
        }
    }

    /**
     * 提供
     */
    fun provideObtainServiceDelegate(): IRepositoryManager.ObtainServiceDelegate? =
        mObtainServiceDelegate

    class Builder {
        var mApiUrl: HttpUrl? = null
        val mBaseUrl: BaseUrl? = null
        var mLoaderStrategy: BaseImageLoaderStrategy<*>? = null
        var mHandler: GlobalHttpHandler? = null
        var mInterceptors: MutableList<Interceptor>? = null
        var mCacheFile: File? = null
        var mRetrofitConfiguration: RetrofitConfiguration? = null
        var mOKHttpConfiguration: OKHttpConfiguration? = null
        var mGsonConfiguration: GsonConfiguration? = null
        var mPrintHttpLogLevel: RequestInterceptor.Level? = null
        var mFormatPrinter: FormatPrinter? = null
        var mCacheFactory: Cache.Factory? = null
        var mObtainServiceDelegate: IRepositoryManager.ObtainServiceDelegate? = null

        fun baseUrl(baseUrl: String): Builder {
            if (baseUrl.isEmpty()) {
                throw NullPointerException("BaseUrl can not be empty")
            }
            this.mApiUrl = HttpUrl.parse(baseUrl)
            return this
        }

        fun imageLoaderStrategy(loaderStrategy: BaseImageLoaderStrategy<*>): Builder {
            this.mLoaderStrategy = loaderStrategy
            return this
        }

        fun globalHttpHandler(handler: GlobalHttpHandler): Builder {
            this.mHandler = handler
            return this
        }

        fun addInterceptor(interceptor: Interceptor): Builder {
            if (mInterceptors == null) {
                mInterceptors = mutableListOf()
            }
            this.mInterceptors!!.add(interceptor)
            return this
        }

        fun cacheFile(cacheFile: File): Builder {
            this.mCacheFile = cacheFile
            return this
        }

        fun retrofitConfiguration(retrofitConfiguration: RetrofitConfiguration): Builder {
            this.mRetrofitConfiguration = retrofitConfiguration
            return this
        }

        fun okHttpConfiguration(okHttpConfiguration: OKHttpConfiguration): Builder {
            this.mOKHttpConfiguration = okHttpConfiguration
            return this
        }

        fun gsonConfiguration(gsonConfiguration: GsonConfiguration): Builder {
            this.mGsonConfiguration = gsonConfiguration
            return this
        }

        fun printHttpLogLevel(printHttpLogLevel: RequestInterceptor.Level): Builder {
            this.mPrintHttpLogLevel = printHttpLogLevel
            return this
        }

        fun formatPrinter(formatPrinter: FormatPrinter): Builder {
            this.mFormatPrinter = formatPrinter
            return this
        }

        fun cacheFactory(cacheFactory: Cache.Factory): Builder {
            this.mCacheFactory = cacheFactory
            return this
        }

        fun obtainServiceDelegate(obtainServiceDelegate: IRepositoryManager.ObtainServiceDelegate): Builder {
            this.mObtainServiceDelegate = obtainServiceDelegate
            return this
        }

        fun build(): GlobalConfigModule {
            return GlobalConfigModule(this)
        }
    }
}