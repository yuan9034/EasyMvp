package com.xdja.easymvp.integration

import android.app.Application
import android.content.Context
import com.bumptech.glide.util.Preconditions
import com.xdja.easymvp.integration.cache.Cache
import com.xdja.easymvp.integration.cache.CacheType
import com.xdja.easymvp.mvp.IModel
import retrofit2.Retrofit
import java.lang.reflect.Proxy

/**
 * @author yuanwanli
 * @des   用来管理网络请求层,数据缓存层,数据库请求层
 *提供给[IModel]层必要的Api做数据管理
 * @date 2020/7/3
 */
class RepositoryManager(
    val mRetrofit: Retrofit,
    val mApplication: Application,
    val mCacheFactory: Cache.Factory,
    val mObtainServiceDelegate: IRepositoryManager.ObtainServiceDelegate?
) : IRepositoryManager {
    private var mRetrofitServiceCache: Cache<String, Any>? = null
    private var mCacheServiceCache: Cache<String, Any>? = null

    /**
     * 根据传入的Class获取对应的Retrofit service
     */
    @Synchronized
    override fun <T> obtainRetrofitService(service: Class<T>): T {
        if (mRetrofitServiceCache == null) {
            mRetrofitServiceCache =
                mCacheFactory.build(CacheType.RETROFIT_SERVICE_CACHE) as Cache<String, Any>
        }
        Preconditions.checkNotNull(
            mRetrofitServiceCache,
            "Cannot return null from a Cache.Factory#build(int) method"
        )
        var retrofitService = mRetrofitServiceCache!!.get(service.canonicalName!!) as T?
        if (retrofitService == null) {
            if (mObtainServiceDelegate != null) {
                retrofitService = mObtainServiceDelegate.createRetrofitService(
                    mRetrofit, service
                )
            }
            if (retrofitService == null) {
                retrofitService = Proxy.newProxyInstance(
                    service.classLoader, arrayOf(service),
                    RetrofitServiceProxyHandler(mRetrofit, service)
                ) as T
            }
            mRetrofitServiceCache!!.put(service.canonicalName!!, retrofitService!!)
        }
        return retrofitService
    }

    override fun getContext(): Context = mApplication
}