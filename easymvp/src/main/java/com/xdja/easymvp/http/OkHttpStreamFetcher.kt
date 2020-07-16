package com.xdja.easymvp.http

import android.util.Log
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.HttpException
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.util.ContentLengthInputStream
import com.bumptech.glide.util.Preconditions
import okhttp3.Call
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException
import java.io.InputStream

/**
 * @author yuanwanli
 * @des   获取okhttp的[InputStream]
 * @date 2020/7/1
 */
class OkHttpStreamFetcher(val client: Call.Factory, val url: GlideUrl) : DataFetcher<InputStream>,
    okhttp3.Callback {
    private val TAG: String = "OkHttpFetcher"
    private var stream: InputStream? = null
    private var responseBody: ResponseBody? = null
    private var callback: DataFetcher.DataCallback<in InputStream>? = null

    @Volatile
    private var call: Call? = null

    override fun getDataClass(): Class<InputStream> = InputStream::class.java

    override fun cleanup() {
        try {
            stream?.close()
        } catch (e: IOException) {

        }
        responseBody?.close()
        callback = null
    }

    override fun getDataSource(): DataSource = DataSource.REMOTE

    override fun cancel() {
        call?.cancel()
    }

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        val requestBuilder =
            Request.Builder().url(url.toStringUrl())
        for ((key, value) in url.headers) {
            requestBuilder.addHeader(key, value)
        }
        val request = requestBuilder.build()
        this.callback = callback

        call = client.newCall(request)
        call?.enqueue(this)
    }

    override fun onFailure(call: Call, e: IOException) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "OkHttp failed to obtain result", e)
        }

        callback!!.onLoadFailed(e)
    }

    override fun onResponse(call: Call, response: Response) {
        responseBody = response.body()
        if (response.isSuccessful) {
            val contentLength =
                Preconditions.checkNotNull(responseBody)
                    .contentLength()
            stream = ContentLengthInputStream.obtain(responseBody!!.byteStream(), contentLength)
            callback!!.onDataReady(stream)
        } else {
            callback!!.onLoadFailed(
                HttpException(
                    response.message(),
                    response.code()
                )
            )
        }
    }
}