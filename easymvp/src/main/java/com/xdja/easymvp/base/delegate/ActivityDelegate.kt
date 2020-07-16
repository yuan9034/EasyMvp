package com.xdja.easymvp.base.delegate

import android.app.Activity
import android.os.Bundle

/**
 * @author yuanwanli
 * @des   [Activity]代理类 ,用于框架内部在每个Activity的对应生命周期中插入需要的逻辑
 *@see ActivityDelegateImpl
 * @date 2020/6/22
 */
const val ACTIVITY_DELEGATE: String = "ACTIVITY_DELEGATE"

interface ActivityDelegate {
    fun onCreate(savedInstanceState: Bundle?)
    fun onStart()
    fun onResume()
    fun onPause()
    fun onStop()
    fun onSaveInstanceState(outState: Bundle)
    fun onDestroy()
}