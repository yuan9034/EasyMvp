package com.xdja.easymvp.base

/**
 * @author yuanwanli
 * @des   判断是否接入相应的平台
 * @date 2020/6/28
 */
object Platform {
    var DEPENDENCY_SUPPORT_DESIGN = false
    var DEPENDENCY_GLIDE = false
    var DEPENDENCY_ANDROID_EVENTBUS = false
    var DEPENDENCY_EVENTBUS = false
    private fun findClassByClassName(className: String): Boolean {
        return try {
            Class.forName(className)
            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }

    init {
        DEPENDENCY_SUPPORT_DESIGN =
            findClassByClassName("com.google.android.material.snackbar.Snackbar")
        DEPENDENCY_GLIDE = findClassByClassName("com.bumptech.glide.Glide")
        DEPENDENCY_ANDROID_EVENTBUS =
            findClassByClassName("org.simple.eventbus.EventBus")
        DEPENDENCY_EVENTBUS =
            findClassByClassName("org.greenrobot.eventbus.EventBus")
    }
}