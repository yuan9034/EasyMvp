package com.xdja.easymvp.mvp

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 * @author yuanwanli
 * @des   框架要求每个Presenter都需要实现此类,以满足规范
 * @date 2020/6/24
 */
interface IPresenter : LifecycleObserver {
    /**
     * 借助于LifecycleObserver，做一些初始化操作
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart(lifecycleOwner: LifecycleOwner)

    /**
     * 在框架中[android.app.Activity.onDestroy]时会默认调用[IPresenter.onDestroy]
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(lifecycleOwner: LifecycleOwner)
}