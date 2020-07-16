package com.xdja.easymvp.http

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * @author yuanwanli
 * @des   处理Http请求和响应结果的处理类
 * @date 2020/7/1
 */
interface GlobalHttpHandler {
    /**
     * 这里可以先客户端一步拿到每一次Http请求的结果,可以先解析成Json,再做一些操作
     * 如检测到token过期后,重新请求taken,并重新执行请求
     * @param httpResult 服务器返回的结果(已被框架自动转换为字符串)
     * @param chain [okhttp3.Interceptor.Chain]
     * @param response [Response]
     * @return [Response]
     */
    fun onHttpResultResponse(
        httpResult: String,
        chain: Interceptor.Chain,
        response: Response
    ): Response

    /**
     * 这里可以在请求服务器之前拿到[Request],做一些操作
     * 比如给[Request]统一添加token或者header以及一些参数加密等操作
     * @param chain [okhttp3.Interceptor.Chain]
     * @param request [Request]
     * @return [Request]
     */
    fun onHttpRequestBefore(chain: Interceptor.Chain, request: Request): Request
}