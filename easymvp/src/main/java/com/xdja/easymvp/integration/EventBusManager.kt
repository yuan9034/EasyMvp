package com.xdja.easymvp.integration

import com.xdja.easymvp.base.Platform
import org.greenrobot.eventbus.Subscribe
import org.simple.eventbus.EventBus
import java.lang.reflect.Method

/**
 * @author yuanwanli
 * @des   EventBus管理类
 * @date 2020/6/23
 */
object EventBusManager {
    /**
     * 注册订阅者, 允许在项目中同时依赖两个 EventBus, 只要您喜欢
     */
    public fun register(any: Any) {
        if (Platform.DEPENDENCY_ANDROID_EVENTBUS) {
            EventBus.getDefault().register(any)
        }
        if (Platform.DEPENDENCY_EVENTBUS) {
            if (haveAnnotation(any)) {
                org.greenrobot.eventbus.EventBus.getDefault().register(any)
            }
        }
    }

    /**
     * 注销订阅者, 允许在项目中同时依赖两个 EventBus, 只要您喜欢
     */
    fun unregister(any: Any) {
        if (Platform.DEPENDENCY_ANDROID_EVENTBUS) {
            EventBus.getDefault().unregister(any)
        }
        if (Platform.DEPENDENCY_EVENTBUS) {
            if (haveAnnotation(any)) {
                org.greenrobot.eventbus.EventBus.getDefault().unregister(any)
            }
        }
    }

    /**
     * 清除订阅者和事件的缓存, 如果您在项目中同时依赖了两个 EventBus, 请自己使用想使用的 EventBus 的 Api 清除订阅者和事件的缓存
     */
    fun clear() {
        if (Platform.DEPENDENCY_ANDROID_EVENTBUS) {
            EventBus.getDefault().clear()
        } else if (Platform.DEPENDENCY_EVENTBUS) {
            org.greenrobot.eventbus.EventBus.clearCaches()
        }
    }

    /**
     * [org.greenrobot.eventbus.EventBus] 要求注册之前, 订阅者必须含有一个或以上声明 [org.greenrobot.eventbus.Subscribe]
     * 注解的方法, 否则会报错, 所以如果要想完成在基类中自动注册, 避免报错就要先检查是否符合注册资格
     *
     * @param subscriber 订阅者
     * @return 返回 `true` 则表示含有 [org.greenrobot.eventbus.Subscribe] 注解, `false` 为不含有
     */
    private fun haveAnnotation(subscriber: Any): Boolean {
        var skipSuperClasses = false
        var clazz: Class<*>? = subscriber.javaClass
        //查找类中符合注册要求的方法, 直到Object类
        while (clazz != null && !isSystemClass(clazz.name) && !skipSuperClasses) {
            var allMethods: Array<Method> = try {
                clazz.declaredMethods
            } catch (th: Throwable) {
                try {
                    clazz.methods
                } catch (th2: Throwable) {
                    continue
                } finally {
                    skipSuperClasses = true
                }
            }
            for (method in allMethods) {
                val parameterTypes = method.parameterTypes
                //查看该方法是否含有 Subscribe 注解
                if (method.isAnnotationPresent(Subscribe::class.java) && parameterTypes.size == 1) {
                    return true
                }
            } //end for
            //获取父类, 以继续查找父类中符合要求的方法
            clazz = clazz.superclass
        }
        return false
    }

    private fun isSystemClass(name: String): Boolean {
        return name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.")
    }
}