package com.xdja.easymvp.http.imageloader.glide

import android.widget.ImageView
import com.xdja.easymvp.http.imageloader.ImageConfig

/**
 * @author yuanwanli
 * @des   这里存放图片请求的配置信息,可以一直扩展字段,如果外部调用时想让图片加载框架做一些操作
 * 比如清除缓存或者切换缓存策略,则可以定义一个int类型变量,内部根据int做不同的操作
 * @date 2020/7/7
 */
class ImageConfigImpl(builder: Builder) : ImageConfig() {
    /**
     * 0对应DiskCacheStrategy.all
     * 1对应DiskCacheStrategy.NONE
     * 2对应DiskCacheStrategy.SOURCE
     * 3对应DiskCacheStrategy.RESULT
     */
    @CacheStrategy.Strategy
    var cacheStrategy: Int? = null

    //请求url为空,则使用此图片作为占位符
    var fallback: Int? = null

    //图片每个圆角的大小
    var imageRadius: Int? = null

    //高斯模糊值,值越大模糊效果越大
    var blurValue: Int? = null
    var imageViews: List<ImageView>? = null

    //是否使用淡入淡出过度动画
    var isCrossFade: Boolean? = null

    //是否将图片剪切为CenterCrop
    var isCenterCrop: Boolean? = null

    //是否将图片剪切为圆形
    var isCircle: Boolean? = null

    //清理内存缓存
    var isClearMemory: Boolean? = null

    //清理本地缓存
    var isClearDiskCache: Boolean? = null

    init {
        this.url = builder.url
        this.imageView = builder.imageView
        this.placeholder = builder.placeholder
        this.errorPic = builder.errorPic
        this.fallback = builder.fallback
        this.cacheStrategy = builder.cacheStrategy
        this.imageRadius = builder.imageRadius
        this.blurValue = builder.blurValue
        this.imageViews = builder.imageViews
        this.isCrossFade = builder.isCrossFade
        this.isCenterCrop = builder.isCenterCrop
        this.isCircle = builder.isCircle
        this.isClearMemory = builder.isClearMemory
        this.isClearDiskCache = builder.isClearDiskCache
    }

    class Builder {
        var url: String? = null
        var imageView: ImageView? = null
        var placeholder: Int? = null
        var errorPic: Int? = null
        var fallback: Int? = null

        @CacheStrategy.Strategy
        var cacheStrategy: Int? = null
        var imageRadius: Int? = null
        var blurValue: Int? = null
        var imageViews: List<ImageView>? = null
        var isCrossFade: Boolean? = null
        var isCenterCrop: Boolean? = null
        var isCircle: Boolean? = null
        var isClearMemory: Boolean? = null
        var isClearDiskCache: Boolean? = null

        fun url(url: String): Builder {
            this.url = url
            return this
        }

        fun placeholder(placeholder: Int): Builder {
            this.placeholder = placeholder
            return this
        }

        fun errorPic(errorPic: Int): Builder {
            this.errorPic = errorPic
            return this
        }

        fun fallback(fallback: Int): Builder {
            this.fallback = fallback
            return this
        }

        fun imageView(imageView: ImageView): Builder {
            this.imageView = imageView
            return this
        }

        fun cacheStrategy(@CacheStrategy.Strategy cacheStrategy: Int): Builder {
            this.cacheStrategy = cacheStrategy
            return this
        }

        fun imageRadius(imageRadius: Int): Builder {
            this.imageRadius = imageRadius
            return this
        }

        fun blurValue(blurValue: Int): Builder {
            this.blurValue = blurValue
            return this
        }

        fun imageViews(vararg imageViews: ImageView): Builder {
            this.imageViews = imageViews.toList()
            return this
        }

        fun isCrossFade(isCrossFade: Boolean): Builder {
            this.isCrossFade = isCrossFade
            return this
        }

        fun isCenterCrop(isCenterCrop: Boolean): Builder {
            this.isCenterCrop = isCenterCrop
            return this
        }

        fun isCircle(isCircle: Boolean): Builder {
            this.isCircle = isCircle
            return this
        }

        fun isClearMemory(isClearMemory: Boolean): Builder {
            this.isClearMemory = isClearMemory
            return this
        }

        fun isClearDiskCache(isClearDiskCache: Boolean): Builder {
            this.isClearDiskCache = isClearDiskCache
            return this
        }

        fun build(): ImageConfigImpl {
            return ImageConfigImpl(this)
        }
    }
}