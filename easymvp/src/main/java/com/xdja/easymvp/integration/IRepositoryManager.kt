package com.xdja.easymvp.integration

import android.content.Context
import com.xdja.easymvp.mvp.IModel
import retrofit2.Retrofit

/**
 * @author yuanwanli
 * @des   用来管理网络请求层,以及数据库请求层
 * 提供给[IModel]必要的Api做数据处理
 * @date 2020/6/29
 */
interface IRepositoryManager {
    /**
     * 根据传入的Class获取对应的Retrofit service
     */
    fun <T> obtainRetrofitService(service: Class<T>): T

    /**
     * 获取[android.content.Context]
     */
    fun getContext(): Context

    interface ObtainServiceDelegate {
        fun <T> createRetrofitService(retrofit: Retrofit, serviceClass: Class<T>): T
    }
}