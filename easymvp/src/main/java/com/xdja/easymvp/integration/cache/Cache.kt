package com.xdja.easymvp.integration.cache

/**
 * @author yuanwanli
 * @des   用于缓存框架中所必需的组件,
 * 开发者可通过 {@link GlobalConfigModule.Builder#cacheFactory(Factory)} 为框架提供缓存策略
 * @date 2020/6/22
 */
interface Cache<K, V> {
    /**
     * 返回当前缓存已占用的总 size
     *
     * @return {@code size}
     */
    fun size(): Int

    /**
     * 返回当前缓存所能允许的最大 size
     *
     * @return {@code maxSize}
     */
    fun getMaxSize(): Int

    /**
     * 返回这个 {@code key} 在缓存中对应的 {@code value}, 如果返回 {@code null} 说明这个 {@code key} 没有对应的 {@code value}
     *
     * @param key {@code key}
     * @return {@code value}
     */
    fun get(key: K): V?

    /**
     * 将 {@code key} 和 {@code value} 以条目的形式加入缓存,如果这个 {@code key} 在缓存中已经有对应的 {@code value}
     * 则此 {@code value} 被新的 {@code value} 替换并返回,如果为 {@code null} 说明是一个新条目
     *
     * @param key   {@code key}
     * @param value {@code value}
     * @return 如果这个 {@code key} 在容器中已经储存有 {@code value}, 则返回之前的 {@code value} 否则返回 {@code null}
     */
    fun put(key: K, value: V): V?

    /**
     * 移除缓存中这个 {@code key} 所对应的条目,并返回所移除条目的 value
     * 如果返回为 {@code null} 则有可能时因为这个 {@code key} 对应的 value 为 {@code null} 或条目不存在
     *
     * @param key {@code key}
     * @return 如果这个 {@code key} 在容器中已经储存有 {@code value} 并且删除成功则返回删除的 {@code value}, 否则返回 {@code null}
     */
    fun remove(key: K): V?

    /**
     * 如果这个 {@code key} 在缓存中有对应的 value 并且不为 {@code null}, 则返回 {@code true}
     *
     * @param key {@code key}
     * @return {@code true} 为在容器中含有这个 {@code key}, 否则为 {@code false}
     */
    fun containsKey(key: K): Boolean

    /**
     * 返回当前缓存中含有的所有 {@code key}
     *
     * @return {@code keySet}
     */
    fun keySet(): Set<K>

    /**
     * 清除缓存中所有的内容
     */
    fun clear()
    interface Factory {
        /**
         * Returns a new cache
         *
         * @param type 框架中需要缓存的模块类型
         * @return {@link Cache}
         */
        fun build(type: CacheType): Cache<String, Any>
    }
}