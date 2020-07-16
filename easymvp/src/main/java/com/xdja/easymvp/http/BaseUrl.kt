package com.xdja.easymvp.http

import okhttp3.HttpUrl

/**
 * @author yuanwanli
 * @des   针对于BaseUrl在App启动时不能确定,需要请求服务器接口动态获取的应用场景
 * @date 2020/7/1
 */
interface BaseUrl {
    fun url(): HttpUrl
}