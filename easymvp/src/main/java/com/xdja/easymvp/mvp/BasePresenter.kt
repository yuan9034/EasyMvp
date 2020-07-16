package com.xdja.easymvp.mvp

import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.LogUtils
import com.xdja.easymvp.integration.EventBusManager
import kotlinx.coroutines.*

/**
 * @author yuanwanli
 * @des   基类Presenter
 * @date 2020/6/28
 */
open class BasePresenter<M : IModel, V : IView>(var mModel: M?, var mRootView: V?) :
    IPresenter {
    protected val TAG = this.javaClass.simpleName
    open val failBlock: suspend CoroutineScope.(Throwable) -> Unit = { throwable ->
        LogUtils.e(throwable)
        mRootView?.showMessage(throwable.message.toString())
    }
    open val finallyBlock: suspend CoroutineScope.() -> Unit = {
        mRootView?.hideLoading()
    }
    private val mainScope: CoroutineScope by lazy {
        CoroutineScope(SupervisorJob() + Dispatchers.Main + CoroutineName(TAG))
    }

    override fun onStart(lifecycleOwner: LifecycleOwner) {
        if (useEventBus()) {
            EventBusManager.register(this)
        }
    }

    override fun onDestroy(lifecycleOwner: LifecycleOwner) {
        if (useEventBus()) {
            EventBusManager.unregister(this)
        }
        mainScope.cancel()
        if (mModel != null) {
            mModel?.onDestroy()
        }
        this.mModel = null
        lifecycleOwner.lifecycle.removeObserver(this)
        this.mRootView = null
    }

    fun useEventBus() = true

    protected fun launch(
        failBlock: suspend CoroutineScope.(Throwable) -> Unit = this.failBlock,
        finallyBlock: suspend CoroutineScope.() -> Unit = this.finallyBlock,
        block: suspend CoroutineScope.() -> Unit
    ) {
        mainScope.launch {
            try {
                block()
            } catch (e: Throwable) {
                failBlock(e)
            } finally {
                finallyBlock()
            }
        }
    }
}