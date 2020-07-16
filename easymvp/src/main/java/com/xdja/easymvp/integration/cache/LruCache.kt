package com.xdja.easymvp.integration.cache

import kotlin.math.roundToInt

/**
 * @author yuanwanli
 * @des   LRU 即Least Recently Used 最近最少使用,也就是说,当缓存满了,会优先淘汰那些最近最不常访问的数据
 * 此种缓存策略为框架默认提供,可自行实现其他缓存策略,如磁盘缓存,为框架或开发者提供缓存的功能
 *
 * @date 2020/7/2
 */
class LruCache<K, V>(var size: Int) : Cache<K, V> {
    private val cache: LinkedHashMap<K, V> by lazy {
        LinkedHashMap<K, V>()
    }
    private val initialMaxSize: Int = size
    private var maxSize: Int = size
    private var currentSize: Int = 0

    /**
     *设置一个系数应用于当前构造函数中所传入的size,从而得到一个新的[maxSize]
     * 并会调用[evict]开始清除满足条件的条目
     */
    fun setSizeMultiplier(multiplier: Float) {
        if (multiplier < 0) {
            throw IllegalArgumentException("Multiplier must be >= 0")
        }
        maxSize = (initialMaxSize * multiplier).roundToInt()
        evict()
    }

    /**
     * 当缓存中已占用的总size大于所能允许的最大size,会使用[trimToSize]开始清除满足条件的条目
     */
    private fun evict() {
        trimToSize(maxSize)
    }

    /**
     *当指定的size小于当前缓存已占用的总size时,会开始清除缓存中最近最少使用的条目
     */
    @Synchronized
    private fun trimToSize(maxSize: Int) {
        var last: Map.Entry<K, V>
        while (currentSize > size) {
            last = cache.entries.iterator().next()
            val toRemove: V = last.value
            currentSize -= getItemSize(toRemove)
            val key: K = last.key
            cache.remove(key)
            onItemEvicted(key, toRemove)
        }
    }

    /**
     * 当缓存中有被驱逐的条目时,会回调此方法,默认空实现,子类可以重写这个方法
     */
    private fun onItemEvicted(key: K, toRemove: V) {

    }

    private fun getItemSize(toRemove: V): Int = 1

    /**
     * 当前缓存已占用的总size
     */
    @Synchronized
    override fun size(): Int = currentSize

    /**
     * 返回当前缓存所能允许的最大size
     * @return [maxSize]
     */
    @Synchronized
    override fun getMaxSize(): Int = maxSize

    /**
     * 返回这个[key]在缓存中对应的value,如果返回null,说明这个[key]没有对应的value
     * @param key 用来映射的[key]
     * @return value
     */
    @Synchronized
    override fun get(key: K): V? = cache[key]

    /**
     *将[key]和[value]以条目的形式加入缓存,如果这个[key]在缓存中已经有对应的[value]
     * 则此[value]被新的[value]替换并返回,如果为null说明是一个新的条目
     * 如果返回的[getItemSize]返回的size大于等于缓存中所能允许的最大size,则不能向缓存中添加此条目
     * 此时会回调[onItemEvicted]通知此方法当前被驱逐的条目
     *@return 如果这个[key]在容器中已经存储有value,则返回之前的value,否则返回null
     */
    @Synchronized
    override fun put(key: K, value: V): V? {
        val itemSize = getItemSize(value)
        if (itemSize >= maxSize) {
            onItemEvicted(key, value)
            return null
        }
        val result = cache.put(key, value)
        if (value != null) {
            currentSize += getItemSize(value)
        }
        if (result != null) {
            currentSize -= getItemSize(result)
        }
        evict()
        return result
    }

    /**
     * 移除缓存中这个[key]所对应的条目,并返回所移除的value
     * 如果返回为null则有可能因为这个key对应的value为null或者条目不存在
     */
    @Synchronized
    override fun remove(key: K): V? {
        val value = cache.remove(key)
        if (value != null) {
            currentSize -= getItemSize(value)
        }
        return value
    }

    /**
     * 如果这个[key]在缓存中有对应的[value]并且不为空,则返回true
     * [key]用来映射的[key]
     * @return 为在这个容器中含有[key],否则为false
     */
    @Synchronized
    override fun containsKey(key: K): Boolean = cache.containsKey(key)

    /**
     * 返回当前缓存中含有的所有[key]
     */
    @Synchronized
    override fun keySet(): Set<K> = cache.keys

    /**
     * 清除缓存中所有的内容
     */
    override fun clear() = trimToSize(0)
}