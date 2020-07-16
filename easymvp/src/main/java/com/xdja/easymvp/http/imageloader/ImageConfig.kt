package com.xdja.easymvp.http.imageloader

import android.widget.ImageView

/**
 * @author yuanwanli
 * @des   这里是图片加载配置信息的基类,定义一些所有图片加载框架都可以用的通用参数
 * 每个[BaseImageLoaderStrategy]应该对应一个[ImageConfig]实现类
 * @date 2020/6/30
 */
open class ImageConfig {
    var url: String? = null
    var imageView: ImageView? = null

    //占位符
    var placeholder: Int? = null

    //错误占位符
    var errorPic: Int? = null
}