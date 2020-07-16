package com.xdja.easymvp.http.log

import okhttp3.MediaType
import okhttp3.Request

/**
 * @author yuanwanli
 * @des   对OkHttp的请求和响应信心进行更规范和清晰的打印,开发者可根据自己的需求自行扩展打印格式
 * @see DefaultFormatPrinter
 * @see GlobalConfigModule.Builder#formatPrinter(FormatPrinter)
 * @date 2020/7/1
 */
interface FormatPrinter {
    /**
     * 打印网络请求,当网络请求[okhttp3.RequestBody]可以解析的情况
     */
    fun printJsonRequest(request: Request, bodyString: String)

    /**
     * 打印网络请求信息,当网络请求[okhttp3.RequestBody]为空或者不可解析的情况
     */
    fun printFileRequest(request: Request)

    /**
     * 打印网络响应信息,当网络响应[okhttp3.ResponseBody]可以解析的情况
     * @param chainMs      服务器响应耗时(单位毫秒)
     * @param isSuccessFul 请求是否成功
     * @param code         响应码
     * @param headers      请求头
     * @param contentType  服务器返回数据的数据类型
     * @param bodyString   服务器返回的数据(已解析)
     * @param segments     域名后面的资源地址
     * @param message      响应信息
     * @param responseUrl  请求地址
     */
    fun printJsonResponse(
        chainMs: Long,
        isSuccessFul: Boolean,
        code: Int,
        headers: String
        ,
        contentType: MediaType?,
        bodyString: String?,
        segments: List<String>,
        message: String,
        responseUrl: String
    )

    /**
     * 打印网络响应信息,当网络响应[okhttp3.ResponseBody]为空或不可解析的情况
     * @param chainMs      服务器响应耗时(单位毫秒)
     * @param isSuccessFul 请求是否成功
     * @param code         响应码
     * @param headers      请求头
     * @param segments     域名后面的资源地址
     * @param message      响应信息
     * @param responseUrl  请求地址
     */
    fun printFileResponse(
        chainMs: Long, isSuccessFul: Boolean, code: Int, headers: String,
        segments: List<String>, message: String, responseUrl: String
    )
}