package com.xdja.easymvp.integration.cache

import android.app.ActivityManager
import android.content.Context
import androidx.fragment.app.Fragment
import com.xdja.easymvp.di.component.AppComponent
import com.xdja.easymvp.integration.RepositoryManager

/**
 * @author yuanwanli
 * @des   构建[Cache]时,使用[CacheType]中声明的类型
 * 来区分不同的模块从而为不同的模块构建不同的缓存策略
 *@see [Cache.Factory#build]
 * @date 2020/6/22
 */
interface CacheType {
    companion object {
        var RETROFIT_SERVICE_CACHE_TYPE_ID: Int = 0
        var CACHE_SERVICE_CACHE_TYPE_ID = 1
        var EXTRAS_TYPE_ID = 2
        var ACTIVITY_CACHE_TYPE_ID = 3
        var FRAGMENT_CACHE_TYPE_ID = 4

        /**
         * [RepositoryManager]中存储Retrofit Service的容器
         */
        val RETROFIT_SERVICE_CACHE: CacheType = object : CacheType {
            val MAX_SIZE = 150
            val MAX_SIZE_MULTIPLIER = 0.002f
            override fun getCacheTypeId() = RETROFIT_SERVICE_CACHE_TYPE_ID

            override fun calculateCacheSize(context: Context): Int {
                val activityManager =
                    context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                val targetMemoryCacheSize = activityManager.memoryClass * MAX_SIZE_MULTIPLIER * 1024
                if (targetMemoryCacheSize.toInt() >= MAX_SIZE) {
                    return MAX_SIZE
                }
                return targetMemoryCacheSize.toInt()
            }

        }

        /**
         * [RepositoryManager]中存储Cache Service的容器
         */
        val CACHE_SERVICE_CACHE: CacheType = object : CacheType {
            val MAX_SIZE = 150
            val MAX_SIZE_MULTIPLIER = 0.002f
            override fun getCacheTypeId() = RETROFIT_SERVICE_CACHE_TYPE_ID

            override fun calculateCacheSize(context: Context): Int {
                val activityManager =
                    context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                val targetMemoryCacheSize = activityManager.memoryClass * MAX_SIZE_MULTIPLIER * 1024
                if (targetMemoryCacheSize.toInt() >= MAX_SIZE) {
                    return MAX_SIZE
                }
                return targetMemoryCacheSize.toInt()
            }

        }

        /**
         * [AppComponent]中的extras
         */
        val EXTRAS: CacheType = object : CacheType {
            val MAX_SIZE = 500
            val MAX_SIZE_MULTIPLIER = 0.005f
            override fun getCacheTypeId() = EXTRAS_TYPE_ID

            override fun calculateCacheSize(context: Context): Int {
                val activityManager =
                    context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                val targetMemoryCacheSize = activityManager.memoryClass * MAX_SIZE_MULTIPLIER * 1024
                if (targetMemoryCacheSize.toInt() >= MAX_SIZE) {
                    return MAX_SIZE
                }
                return targetMemoryCacheSize.toInt()
            }

        }

        /**
         * Activity中存储数据的容器
         */
        val ACTIVITY_CACHE: CacheType = object : CacheType {
            val MAX_SIZE = 80
            val MAX_SIZE_MULTIPLIER = 0.0008f
            override fun getCacheTypeId() = ACTIVITY_CACHE_TYPE_ID

            override fun calculateCacheSize(context: Context): Int {
                val activityManager =
                    context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                val targetMemoryCacheSize = activityManager.memoryClass * MAX_SIZE_MULTIPLIER * 1024
                if (targetMemoryCacheSize.toInt() >= MAX_SIZE) {
                    return MAX_SIZE
                }
                return targetMemoryCacheSize.toInt()
            }

        }

        /**
         * [Fragment]中存储数据的容器
         */
        val FRAGMENT_CACHE: CacheType = object : CacheType {
            val MAX_SIZE = 80
            val MAX_SIZE_MULTIPLIER = 0.0008f
            override fun getCacheTypeId() = FRAGMENT_CACHE_TYPE_ID
            override fun calculateCacheSize(context: Context): Int {

                val activityManager =
                    context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                val targetMemoryCacheSize = activityManager.memoryClass * MAX_SIZE_MULTIPLIER * 1024
                if (targetMemoryCacheSize.toInt() >= MAX_SIZE) {
                    return MAX_SIZE
                }
                return targetMemoryCacheSize.toInt()
            }

        }
    }

    /**
     * 返回框架内需要缓存的模块对应的[id]
     */
    public fun getCacheTypeId(): Int

    /**
     * 计算对应模块需要的缓存大小
     */
    public fun calculateCacheSize(context: Context): Int
}
