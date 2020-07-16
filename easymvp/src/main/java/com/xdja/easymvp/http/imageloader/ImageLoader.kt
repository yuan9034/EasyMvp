package com.xdja.easymvp.http.imageloader

import android.content.Context
import com.bumptech.glide.util.Preconditions


/**
 * @author yuanwanli
 * @des 使用策略模式和建造者模式,可以动态切换图片请求框架(比如说切换成 Picasso )
 * 当需要切换图片请求框架或图片请求框架升级后变更了 Api 时
 * 这里可以将影响范围降到最低,所以封装 [ImageLoader] 是为了屏蔽这个风险
 * @date 2020/6/30
 */
class ImageLoader<T : ImageConfig>(var mStrategy: BaseImageLoaderStrategy<T>) {
    /**
     * 加载图片
     * @param context
     * @param config
     * @param T
     */
    fun loadImage(context: Context, imageConfig: T) {
        Preconditions.checkNotNull(
            mStrategy,
            "Please implement BaseImageLoaderStrategy and call GlobalConfigModule.Builder#imageLoaderStrategy(BaseImageLoaderStrategy) in the applyOptions method of ConfigModule"
        )
        this.mStrategy.loadImage(context, imageConfig)
    }

    fun clear(context: Context, imageConfig: T) {
        Preconditions.checkNotNull(
            mStrategy,
            "Please implement BaseImageLoaderStrategy and call GlobalConfigModule.Builder#imageLoaderStrategy(BaseImageLoaderStrategy) in the applyOptions method of ConfigModule"
        )
        this.mStrategy.clear(context, imageConfig)
    }

    fun getLoadImgStrategy(): BaseImageLoaderStrategy<T> = mStrategy
    fun setLoadImgStrategy(strategy: BaseImageLoaderStrategy<T>) {
        Preconditions.checkNotNull(strategy, "strategy == null")
        this.mStrategy = strategy
    }
}
