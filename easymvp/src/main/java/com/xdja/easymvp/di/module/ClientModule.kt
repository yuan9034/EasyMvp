package com.xdja.easymvp.di.module

import android.content.Context
import com.google.gson.Gson
import com.xdja.easymvp.http.GlobalHttpHandler
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author yuanwanli
 * @des
 * @date 2020/7/7
 */
/**
 * [Retrofit]自定义配置接口
 */
interface RetrofitConfiguration {
    fun configRetrofit(context: Context, builder: Retrofit.Builder)
}

/**
 * [OkHttpClient]自定义配置接口
 */
interface OKHttpConfiguration {
    fun configOKHttp(context: Context, builder: OkHttpClient.Builder)
}

private const val TIME_OUT = 10L
val EasyMvpClientModule = module {
    //提供OkHttpClient.Builder单例
    single<OkHttpClient.Builder> {
        OkHttpClient.Builder()
    }
    //提供Retrofit.Builder单例
    single<Retrofit.Builder> {
        Retrofit.Builder()
    }
    //提供OkHttpClient单例
    single<OkHttpClient> { (configuration: OKHttpConfiguration?, interceptor: Interceptor
                                   , interceptors: List<Interceptor>?, handler: GlobalHttpHandler?) ->
        val builder = get<OkHttpClient.Builder>()
        builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .addNetworkInterceptor(interceptor)
        handler?.let {
            builder.addInterceptor { chain ->
                chain.proceed(handler.onHttpRequestBefore(chain, chain.request()))
            }
        }
        interceptors?.forEach {
            builder.addInterceptor(it)
        }
        configuration?.apply {
            configOKHttp(get(), builder)
        }
        return@single builder.build()
    }
    //提供Retrofit单例
    single<Retrofit> { (configuration: RetrofitConfiguration, httpUrl: HttpUrl) ->
        val builder = get<Retrofit.Builder>()
        val client = get<OkHttpClient>()
        val gson = get<Gson>()
        builder.baseUrl(httpUrl)
            .client(client)
        configuration?.apply {
            configRetrofit(get(), builder)
        }
        builder.addConverterFactory(GsonConverterFactory.create(gson))
        builder.build()
    }
}
