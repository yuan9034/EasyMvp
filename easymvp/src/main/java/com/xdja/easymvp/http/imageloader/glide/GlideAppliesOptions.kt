package com.xdja.easymvp.http.imageloader.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.xdja.easymvp.http.imageloader.BaseImageLoaderStrategy

/**
 * @author yuanwanli
 * @des   如果你想要具有配置[Glide]的权利,则需要让
 * [BaseImageLoaderStrategy]的实现类也必须实现[GlideAppliesOptions]
 * @date 2020/6/30
 */
interface GlideAppliesOptions {
    /**
     * 配置[Glide]的自定义参数,此方法在[Glide]初始化时执行([Glide]第一次被调用时初始化),只会执行一次
     */
    fun applyGlideOptions(context: Context, builder: GlideBuilder)

    /**
     * 注册{@link Glide}的组件，参考[com.bumptech.glide.module.LibraryGlideModule]
     */
    fun registerComponents(context: Context, glide: Glide, registry: Registry)
}