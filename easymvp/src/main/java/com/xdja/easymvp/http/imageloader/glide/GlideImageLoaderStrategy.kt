package com.xdja.easymvp.http.imageloader.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.util.Preconditions
import com.xdja.easymvp.http.imageloader.BaseImageLoaderStrategy
import com.xdja.easymvp.http.imageloader.ImageConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * @author yuanwanli
 * @des   此类只是简单的实现了Glide加载的策略,方便快速使用,
 * 但大部分情况需要应对复杂的场景,这时可自行实现[BaseImageLoaderStrategy]和[ImageConfig]替换现有策略
 * @date 2020/7/7
 */
class GlideImageLoaderStrategy : BaseImageLoaderStrategy<ImageConfigImpl>, GlideAppliesOptions {
    override fun loadImage(ctx: Context, config: ImageConfigImpl) {
        Preconditions.checkNotNull(ctx, "Context is required")
        Preconditions.checkNotNull(config, "ImageConfigImpl is required")
        Preconditions.checkNotNull(config.imageView, "ImageView is required")
        val request = GlideArms.with(ctx)
        var glideRequest = request.load(config.url)
        when (config.cacheStrategy) {
            CacheStrategy.NONE -> glideRequest.diskCacheStrategy(DiskCacheStrategy.NONE)
            CacheStrategy.RESOURCE -> glideRequest.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            CacheStrategy.DATA -> glideRequest.diskCacheStrategy(DiskCacheStrategy.DATA)
            CacheStrategy.AUTOMATIC -> glideRequest.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            else -> glideRequest.diskCacheStrategy(DiskCacheStrategy.ALL)
        }
        if (config.isCrossFade == true) {
            glideRequest = glideRequest.transition(DrawableTransitionOptions.withCrossFade())
        }
        if (config.isCenterCrop == true) {
            glideRequest = glideRequest.centerCrop()
        }
        if (config.isCircle == true) {
            glideRequest = glideRequest.circleCrop()
        }
        if (config.imageRadius != null && config.imageRadius!! > 0) {
            glideRequest = glideRequest.transform(RoundedCorners(config.imageRadius!!))
        }
        if (config.blurValue != null && config.blurValue!! > 0) {
            glideRequest = glideRequest.transform(BlurTransformation(config.blurValue!!))
        }
        if (config.placeholder != null && config.placeholder != 0) {
            glideRequest = glideRequest.placeholder(config.placeholder!!)
        }
        if (config.errorPic != null && config.errorPic != 0) {
            glideRequest = glideRequest.error(config.errorPic!!)
        }
        if (config.fallback != null && config.fallback != 0) {
            glideRequest = glideRequest.fallback(config.fallback!!)
        }
        glideRequest.into(config.imageView!!)
    }

    override fun clear(ctx: Context, config: ImageConfigImpl) {
        Preconditions.checkNotNull(ctx, "Context is required")
        Preconditions.checkNotNull(config, "ImageConfigImpl is required")
        if (config.imageView != null) {
            GlideArms.get(ctx).requestManagerRetriever.get(ctx).clear(config.imageView!!)
        }
        config.imageViews?.forEach {
            GlideArms.get(ctx).requestManagerRetriever.get(ctx).clear(it)
        }
        if (config.isClearDiskCache == true) {
            GlobalScope.launch(Dispatchers.IO) {
                Glide.get(ctx).clearDiskCache()
            }
        }
        if (config.isClearMemory == true) {
            GlobalScope.launch(Dispatchers.IO) {
                Glide.get(ctx).clearMemory()
            }
        }
    }

    override fun applyGlideOptions(context: Context, builder: GlideBuilder) {
        Timber.i("applyGlideOptions")
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        Timber.i("registerComponents")
    }
}