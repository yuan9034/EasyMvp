package com.xdja.easymvp.http

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoader.LoadData
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import okhttp3.Call
import okhttp3.OkHttpClient
import java.io.InputStream

/**
 * @author yuanwanli
 * @des   用于使用OkHttp在http/https上获取media
 * @date 2020/7/1
 */
class OkHttpUrlLoader(val client: Call.Factory) : ModelLoader<GlideUrl, InputStream> {

    override fun handles(url: GlideUrl): Boolean {
        return true
    }

    override fun buildLoadData(
        model: GlideUrl, width: Int, height: Int,
        options: Options
    ): LoadData<InputStream?>? {
        return LoadData(model, OkHttpStreamFetcher(client, model))
    }

    /**
     * The default factory for [OkHttpUrlLoader]s.
     */
    // Public API.
    class Factory
    /**
     * Constructor for a new Factory that runs requests using a static singleton client.
     */
    @JvmOverloads
    constructor(private val client: Call.Factory = internalClient!!) :
        ModelLoaderFactory<GlideUrl, InputStream> {
        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<GlideUrl, InputStream> {
            return OkHttpUrlLoader(client)
        }

        override fun teardown() {
            // Do nothing, this instance doesn't own the client.
        }

        companion object {
            @Volatile
            private var internalClient: Call.Factory? = null
                private get() {
                    if (field == null) {
                        synchronized(Factory::class.java) {
                            if (field == null) {
                                field = OkHttpClient()
                            }
                        }
                    }
                    return field
                }
        }
    }
}