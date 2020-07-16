package com.xdja.easymvp.http.imageloader.glide

import android.content.Context
import com.blankj.utilcode.util.FileUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.xdja.easymvp.http.OkHttpUrlLoader
import com.xdja.easymvp.utils.AppComponentUtils
import java.io.File
import java.io.InputStream

/**
 * @author yuanwanli
 * @des   [AppGlideModule]的默认实现类
 * 用于配置缓存文件夹,切换图片请求框架等操作
 * @date 2020/6/30
 */
@GlideModule(glideName = "GlideArms")
class GlideConfiguration : AppGlideModule() {
    private val IMAGE_DISK_CACHE_MAX_SIZE: Long = 100 * 1024 * 1024 //图片缓存文件最大值为100Mb
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val appComponentFromContext = AppComponentUtils.getAppComponentFromContext(context)
        builder.setDiskCache {
            val file = File(appComponentFromContext.cacheFile, "Glide")
            FileUtils.createOrExistsDir(file)
            DiskLruCacheWrapper.create(file, IMAGE_DISK_CACHE_MAX_SIZE)
        }
        val calculator = MemorySizeCalculator.Builder(context).build()

        val customMemoryCacheSize = calculator.memoryCacheSize * 1.2
        val customBitmapPoolSize = calculator.bitmapPoolSize * 1.2
        builder.setMemoryCache(LruResourceCache(customMemoryCacheSize.toLong()))
        builder.setBitmapPool(LruBitmapPool(customBitmapPoolSize.toLong()))
        //将配置 Glide 的机会转交给 GlideImageLoaderStrategy,如你觉得框架提供的 GlideImageLoaderStrategy
        //并不能满足自己的需求,想自定义 BaseImageLoaderStrategy,那请你最好实现 GlideAppliesOptions
        //因为只有成为 GlideAppliesOptions 的实现类,这里才能调用 applyGlideOptions(),让你具有配置 Glide 的权利
        val loadImgStrategy = appComponentFromContext.imageLoader.mStrategy
        if (loadImgStrategy is GlideAppliesOptions) {
            (loadImgStrategy as GlideAppliesOptions).applyGlideOptions(context, builder)
        }
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        //Glide默认使用HttpUrlConnection 做网络请求,在这里切换成okHttp
        val appComponent = AppComponentUtils.getAppComponentFromContext(context)
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(appComponent.okHttpClient)
        )

        val loadImgStrategy = appComponent.imageLoader.mStrategy
        if (loadImgStrategy is GlideAppliesOptions) {
            (loadImgStrategy as GlideAppliesOptions).registerComponents(context, glide, registry)
        }
    }

    override fun isManifestParsingEnabled(): Boolean = false
}