package com.xdja.app.config

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.google.gson.GsonBuilder
import com.xdja.app.BuildConfig
import com.xdja.easymvp.base.delegate.AppLifeCycles
import com.xdja.easymvp.di.module.GlobalConfigModule
import com.xdja.easymvp.di.module.GsonConfiguration
import com.xdja.easymvp.di.module.OKHttpConfiguration
import com.xdja.easymvp.di.module.RetrofitConfiguration
import com.xdja.easymvp.http.imageloader.glide.GlideImageLoaderStrategy
import com.xdja.easymvp.http.log.RequestInterceptor
import com.xdja.easymvp.integration.ConfigModule
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * @author yuanwanli
 * @des   App 的全局配置信息在此配置, 需要将此实现类声明到 AndroidManifest 中
 * @date 2020/7/6
 */
class GlobalConfiguration : ConfigModule {
    override fun applyOptions(context: Context, builder: GlobalConfigModule.Builder) {
        if (!BuildConfig.DEBUG) {
            builder.printHttpLogLevel(RequestInterceptor.Level.NONE)
        }
        builder.baseUrl("https://www.wanandroid.com")
            //自定义应用缓存目录
//            .cacheFile()
            //自定义缓存策略
//            .cacheFactory()
            //自定义打印
//            .formatPrinter()

            .imageLoaderStrategy(GlideImageLoaderStrategy())
            .globalHttpHandler(GlobalHttpHandlerImpl(context))
            .gsonConfiguration(object : GsonConfiguration {
                override fun configGson(context: Context, builder: GsonBuilder) {
                    //这里可以自己自定义配置 Gson 的参数
                    builder.serializeNulls() //支持序列化值为 null 的参数
                        .enableComplexMapKeySerialization() //支持将序列化 key 为 Object 的 Map, 默认只能序列化 key 为 String 的 Map
                }
            })
            .retrofitConfiguration(object : RetrofitConfiguration {
                override fun configRetrofit(context: Context, builder: Retrofit.Builder) {
                    //这里可以自己自定义配置 Retrofit 的参数
                }
            })
            .okHttpConfiguration(object : OKHttpConfiguration {
                override fun configOKHttp(context: Context, builder: OkHttpClient.Builder) {
                    //这里可以自己自定义配置 Okhttp 的参数
                    builder.writeTimeout(10, TimeUnit.SECONDS)
                }
            })
    }

    override fun injectAppLifecycle(context: Context, lifeCycles: MutableList<AppLifeCycles>) {
        //AppLifecycles 中的所有方法都会在基类 Application 的对应生命周期中被调用
        //所以在对应的方法中可以扩展一些自己需要的逻辑
        lifeCycles.add(AppLifeCyclesImpl())
    }

    override fun injectActivityLifecycle(
        context: Context,
        lifeCycles: MutableList<Application.ActivityLifecycleCallbacks>
    ) {
        /**
         * ActivityLifecycleCallbacks 中的所有方法都会在 Activity (包括三方库) 的对应生命周期中被调用
         * 所以在对应的方法中可以扩展一些自己需要的逻辑,可以根据不同的逻辑添加多个实现类
         */
        lifeCycles.add(ActivityLifecycleCallbacksImpl())
    }

    override fun injectFragmentLifecycle(
        context: Context,
        lifeCycles: MutableList<FragmentManager.FragmentLifecycleCallbacks>
    ) {
        /**
         * FragmentLifecycleCallbacks 中的所有方法都会在 Fragment (包括三方库) 的对应生命周期中被调用
         * 所以在对应的方法中可以扩展一些自己需要的逻辑,可以根据不同的逻辑添加多个实现类
         */
        lifeCycles.add(FragmentLifecycleCallbacksImpl())
    }
}