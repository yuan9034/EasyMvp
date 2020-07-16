package com.xdja.easymvp.http.log

import com.blankj.utilcode.util.JsonUtils
import com.xdja.easymvp.http.GlobalHttpHandler
import com.xdja.easymvp.utils.UrlEncoderUtils
import com.xdja.easymvp.utils.ZipHelper
import okhttp3.*
import okio.Buffer
import timber.log.Timber
import java.io.StringReader
import java.io.StringWriter
import java.net.URLDecoder
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.TimeUnit
import javax.xml.transform.OutputKeys
import javax.xml.transform.Source
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

/**
 * @author yuanwanli
 * @des   解析框架中的网络请求和响应结果,并以日志形式输出,调试神器
 * 可使用 [GlobalConfigModule.Builder#printHttpLogLevel(Level)]控制或关闭日志
 * @date 2020/7/1
 */
class RequestInterceptor(
    val mHandler: GlobalHttpHandler,
    val mPrinter: FormatPrinter,
    val printLevel: Level
) : Interceptor {
    /**
     * 解析请求服务器的请求参数
     * @param request [Request]
     * @return 解析后的请求信息
     */
    fun parseParams(request: Request): String {
        try {
            val body = request.newBuilder().build().body() ?: return ""
            val requestBuffer = Buffer()
            body.writeTo(requestBuffer)
            var charset = Charset.forName("UTF-8")
            val contentType = body.contentType()
            contentType?.let {
                charset = it.charset(charset)!!
            }
            var json = requestBuffer.readString(charset)
            if (UrlEncoderUtils.hasUrlEncoded(json)) {
                json = URLDecoder.decode(json, convertCharset(charset))
            }
            return JsonUtils.formatJson(json)
        } catch (e: Exception) {
            e.printStackTrace()
            return "{\"error\": \"" + e.message + "\"}"
        }
    }

    private fun convertCharset(charset: Charset): String {
        val s = charset.toString()
        val i = s.indexOf("[")
        return if (i == -1) {
            s
        } else s.substring(i + 1, s.length - 1)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val logRequest =
            printLevel == Level.ALL || (printLevel != Level.NONE && printLevel == Level.REQUEST)
        if (logRequest) {
            //打印请求信息
            if (request.body() != null && isParseAble(request.body()!!.contentType())) {
                mPrinter.printJsonRequest(request, parseParams(request))
            } else {
                mPrinter.printFileRequest(request)
            }
        }
        val logResponse =
            printLevel == Level.ALL || (printLevel != Level.NONE && printLevel == Level.RESPONSE)
        val t1 = if (logResponse) System.nanoTime() else 0
        var originalResponse: Response
        try {
            originalResponse = chain.proceed(request)
        } catch (e: Throwable) {
            Timber.w(e.message, "Http Error: %s")
            throw  e
        }
        val t2 = if (logResponse) System.nanoTime() else 0

        val responseBody = originalResponse.body()
        //打印响应结果
        var bodyString: String? = null
        if (responseBody != null && isParseAble(responseBody.contentType())) {
            bodyString = printResult(request, originalResponse, logResponse)
        }
        if (logResponse) {
            val segmentList =
                request.url().encodedPathSegments()
            val header = originalResponse.headers().toString()
            val code = originalResponse.code()
            val isSuccessful = originalResponse.isSuccessful
            val message = originalResponse.message()
            val url = originalResponse.request().url().toString()
            if (responseBody != null && isParseAble(responseBody.contentType())) {
                mPrinter.printJsonResponse(
                    TimeUnit.NANOSECONDS.toMillis(t2 - t1), isSuccessful,
                    code, header,
                    responseBody.contentType()!!,
                    bodyString!!, segmentList, message, url
                )
            } else {
                mPrinter.printFileResponse(
                    TimeUnit.NANOSECONDS.toMillis(t2 - t1),
                    isSuccessful, code, header, segmentList, message, url
                )
            }
        }

        return mHandler?.onHttpResultResponse(bodyString!!, chain, originalResponse)
            ?: originalResponse
    }

    /**
     * 打印响应结果
     * @param request     [Request]
     * @param response    [Response]
     * @param logResponse 是否打印响应结果
     * @return 解析后的响应结果
     */
    private fun printResult(request: Request, response: Response, logResponse: Boolean): String? {
        return try {
            //读取服务器返回的结果
            val responseBody = response.newBuilder().build().body()
            val source = responseBody?.source()
            source?.request(Long.MAX_VALUE)
            val buffer = source?.buffer()
            //获取content的压缩类型
            val encoding = response.headers().get("Content-Encoding")
            val clone = buffer?.clone()
            //解析response content
            parseContent(responseBody, encoding, clone!!)
        } catch (e: Throwable) {
            e.printStackTrace()
            "{\"error\": \"" + e.message + "\"}"
        }
    }

    /**
     * 解析服务器响应内容
     * @param responseBody [ResponseBody]
     * @param encoding     编码类型
     * @param clone        克隆后的服务器响应内容
     * @return 解析后的响应结果
     */
    private fun parseContent(
        responseBody: ResponseBody?,
        encoding: String?,
        clone: Buffer
    ): String? {
        var charset = Charset.forName("UTF-8")
        val contentType = responseBody?.contentType()
        if (contentType != null) charset = contentType.charset(charset)!!
        //content 使用 gzip 压缩
        return when {
            "gzip".equals(encoding, ignoreCase = true) -> {
                //解压
                ZipHelper.decompressForGzip(
                    clone.readByteArray(),
                    convertCharset(charset)
                )
            }
            "zlib".equals(encoding, ignoreCase = true) -> {
                //content 使用 zlib 压缩
                ZipHelper.decompressToStringForZlib(
                    clone.readByteArray(),
                    convertCharset(charset)
                )
            }
            else -> {
                //content 没有被压缩, 或者使用其他未知压缩方式
                clone.readString(charset)
            }
        }
    }

    /**
     * 是否可以解析
     * @param mediaType [MediaType]
     * @return `true` 为可以解析
     */
    private fun isParseAble(mediaType: MediaType?): Boolean {
        return if (mediaType?.type() == null) {
            false
        } else mediaType.isText() || mediaType.isPlain()
                || mediaType.isJson() || mediaType.isForm()
                || mediaType.isHtml() || mediaType.isXml()
    }

    /**
     * MediaType是否为text
     */
    private fun MediaType.isText(): Boolean = "text" == type()

    /**
     * MediaType是否为plain
     */
    private fun MediaType.isPlain(): Boolean =
        subtype().toLowerCase(Locale.getDefault()).contains("plain")


    /**
     * MediaType是否为html
     */
    private fun MediaType.isHtml(): Boolean =
        subtype().toLowerCase(Locale.getDefault()).contains("html")

    /**
     * MediaType是否为form
     */
    private fun MediaType.isForm(): Boolean =
        subtype().toLowerCase(Locale.getDefault()).contains("x-www-form-urlencoded")

    enum class Level {
        /**
         * 不打印log
         */
        NONE,

        /**
         * 只打印请求信息
         */
        REQUEST,

        /**
         * 只打印响应信息
         */
        RESPONSE,

        /**
         * 所有数据全部打印
         */
        ALL
    }
}

/**
 * MediaType是否为json
 */
fun MediaType?.isJson(): Boolean = if (this == null) false else
    subtype().toLowerCase(Locale.getDefault()).contains("json")

/**
 * MediaType是否为xml
 */
fun MediaType?.isXml(): Boolean = if (this == null) false else
    subtype().toLowerCase(Locale.getDefault()).contains("xml")

/**
 * xml格式化
 */
fun String?.xmlFormat(): String? {
    if (this.isNullOrEmpty()) {
        return "Empty/Null xml content"
    }
    val message: String?
    message = try {
        val xmlInput: Source =
            StreamSource(StringReader(this))
        val xmlOutput =
            StreamResult(StringWriter())
        val transformer =
            TransformerFactory.newInstance().newTransformer()
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
        transformer.transform(xmlInput, xmlOutput)
        xmlOutput.writer.toString().replaceFirst(">".toRegex(), ">\n")
    } catch (e: TransformerException) {
        this
    }
    return message
}