package com.xdja.easymvp.base.delegate

import android.app.Activity
import android.os.Bundle
import com.xdja.easymvp.integration.EventBusManager

/**
 * @author yuanwanli
 * @des [ActivityDelegate] 默认实现类
 * @date 2020/6/23
 */
class ActivityDelegateImpl(mActivity: Activity?) : ActivityDelegate {
    private var mActivity = mActivity
    private var iActivity: IActivity? = mActivity as IActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        //如果要使用EventBus 请将此方法返回true
        iActivity?.apply {
            if (useEventBus()) {
                //注册到事件主线
                EventBusManager.register(mActivity!!)
            }
        }
    }

    override fun onStart() {

    }

    override fun onResume() {

    }

    override fun onPause() {

    }

    override fun onStop() {

    }

    override fun onSaveInstanceState(outState: Bundle) {

    }

    override fun onDestroy() {
        //如果要使用EventBus 请将此方法返回true
        if (iActivity != null && iActivity!!.useEventBus()) {
            EventBusManager.unregister(mActivity!!)
        }
        this.iActivity = null
        this.mActivity = null
    }
}