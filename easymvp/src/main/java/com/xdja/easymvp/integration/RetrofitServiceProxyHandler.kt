package com.xdja.easymvp.integration

import retrofit2.Retrofit
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * @author yuanwanli
 * @des   优化Retrofit创建ApiService的执行线程
 * @date 2020/7/6
 */
class RetrofitServiceProxyHandler(
    retrofit: Retrofit,
    serviceClass: Class<*>
) :
    InvocationHandler {
    private val mRetrofit: Retrofit = retrofit
    private val mServiceClass: Class<*> = serviceClass
    private var mRetrofitService: Any? = null

    @Throws(Throwable::class)
    override fun invoke(
        proxy: Any,
        method: Method,
        args: Array<Any>?
    ): Any? {

        // 根据 https://zhuanlan.zhihu.com/p/40097338 对 Retrofit 进行的优化
//        if (method.returnType == Observable::class.java) {
//            // 如果方法返回值是 Observable 的话，则包一层再返回，
//            // 只包一层 defer 由外部去控制耗时方法以及网络请求所处线程，
//            // 如此对原项目的影响为 0，且更可控。
//            return Observable.defer({ method.invoke(getRetrofitService(), *args) as Observable })
//        }
        // 返回值不是 Observable 或 Single 的话不处理。
        if (args.isNullOrEmpty()) {
            return method.invoke(getRetrofitService())
        }
        return method.invoke(getRetrofitService(), *args)
    }

    private fun getRetrofitService(): Any {
        if (mRetrofitService == null) {
            mRetrofitService = mRetrofit.create(mServiceClass)
        }
        return mRetrofitService!!
    }
}