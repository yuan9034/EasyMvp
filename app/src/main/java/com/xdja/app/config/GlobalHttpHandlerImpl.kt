package com.xdja.app.config

import android.content.Context
import com.xdja.easymvp.http.GlobalHttpHandler
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * @author yuanwanli
 * @des   [GlobalHttpHandler]的用法
 * @date 2020/7/6
 */
class GlobalHttpHandlerImpl(context: Context) : GlobalHttpHandler {
    private var context = context
    override fun onHttpResultResponse(
        httpResult: String,
        chain: Interceptor.Chain,
        response: Response
    ): Response {
        var builder = chain.request().newBuilder()
        builder.addHeader("pubSn", "ceshi")
        return response
    }

    override fun onHttpRequestBefore(chain: Interceptor.Chain, request: Request): Request {
        return chain.request().newBuilder().header("token", "ceshi")
            .build();
    }
}