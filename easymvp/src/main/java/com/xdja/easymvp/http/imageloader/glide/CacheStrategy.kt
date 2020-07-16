package com.xdja.easymvp.http.imageloader.glide

import androidx.annotation.IntDef
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * @author yuanwanli
 * @des   Glide缓存
 * @date 2020/7/7
 */
open interface CacheStrategy {
    @IntDef(
        ALL,
        NONE,
        RESOURCE,
        DATA,
        AUTOMATIC
    )
    @Retention(RetentionPolicy.SOURCE)
    annotation class Strategy
    companion object {
        const val ALL = 0
        const val NONE = 1
        const val RESOURCE = 2
        const val DATA = 3
        const val AUTOMATIC = 4
    }
}