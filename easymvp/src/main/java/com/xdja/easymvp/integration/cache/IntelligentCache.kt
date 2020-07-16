package com.xdja.easymvp.integration.cache

import com.bumptech.glide.util.Preconditions
import java.util.*

/**
 * @author yuanwanli
 * @des   可将数据永久存储在容器[mMap],当达到最大容量时可根据LRU算法抛弃不合规则的储存容器[mCache]
 * [IntelligentCache] 可根据传入的key智能的判断您需要将数据存储在哪个容器中,从而对数据的不同特性存储优化
 * @date 2020/7/2
 */
class IntelligentCache<V>(size: Int) : Cache<String, V> {
    val mMap: HashMap<String, V> by lazy {
        HashMap<String, V>()
    }
    val mCache: Cache<String, V> by lazy {
        LruCache<String, V>(size)
    }

    companion object {
        val KEY_KEEP = "Keep=="

        /**
         * 使用此方法返回的值作为key,可以将数据永久存储至内存中
         * @param key
         * @return Keep=+[key]
         */
        fun getKeyOfKeep(key: String): String {
            Preconditions.checkNotNull(key, "key == null")
            return KEY_KEEP + key
        }
    }

    /**
     * 将[mCache]和[mMap]的size相加后返回
     */
    override fun size(): Int = mMap.size + mCache.size()

    /**
     * 将[mMap]和[mCache]的maxSize相加后返回
     */
    override fun getMaxSize(): Int = mMap.size + mCache.getMaxSize()

    /**
     * 如果在[key]中使用[KEY_KEEP]作为前缀,则操作[mMap],否则操作[mCache]
     */
    @Synchronized
    override operator fun get(key: String): V? {
        if (key.startsWith(KEY_KEEP)) {
            return mMap[key]
        }
        return mCache.get(key)
    }

    /**
     * 如果[key]使用[KEY_KEEP]作为其前缀,则操作[mMap],否则操作[mCache]
     * @param key
     * @param value
     * @return 如果这个[key]在容器中已经存储有[value],则返回之前的[value],否则返回[null]
     */
    @Synchronized
    override fun put(key: String, value: V): V? {
        if (key.startsWith(KEY_KEEP)) {
            return mMap.put(key, value)
        }
        return mCache.put(key, value)
    }

    /**
     * 如果在[key]中使用[KEY_KEEP]作为前缀,则操作[mMap],否则操作[mCache]
     * @param key
     * @return 如果这个[key]在容器中已经存储有[value]并且删除成功则返回删除的[value],否则返回[null]
     */
    @Synchronized
    override fun remove(key: String): V? {
        if (key.startsWith(KEY_KEEP)) {
            return mMap.remove(key)
        }
        return mCache.remove(key)
    }

    /**
     * 如果在[key]中使用[KEY_KEEP]作为其前缀,则操作[mMap],否则操作[mCache]
     * @return true 为在容器中含有这个[key],否则为 false
     */
    override fun containsKey(key: String): Boolean {
        if (key.startsWith(KEY_KEEP)) {
            return mMap.containsKey(key)
        }
        return mCache.containsKey(key)
    }

    /**
     * 将[mMap]和[mCache]的[keySet]合并并返回
     */
    override fun keySet(): Set<String> {
        val keys = mMap.keys
        keys.addAll(mCache.keySet())
        return keys
    }

    /**
     * 清空[mCache]和[mMap]容器
     */
    override fun clear() {
        mCache.clear()
        mMap.clear()
    }
}