package com.xdja.easymvp.http.imageloader

import android.content.Context

/**
 * @author yuanwanli
 * @des  图片加载策略实现 策略模式
 * 实现[BaseImageLoaderStrategy]并通过[ImageLoader.setLoadImgStrategy]配置后,才可进行图片请求
 * @date 2020/6/30
 */
interface BaseImageLoaderStrategy<T : ImageConfig> {
    /**
     * 加载图片
     * [ctc] 上下文.[config]图片加载配置
     */
    fun loadImage(ctx: Context, config: T)

    /**
     * 停止加载
     * [ctc] 上下文.[config]图片加载配置
     */
    fun clear(ctx: Context, config: T)
}
