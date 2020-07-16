package com.xdja.easymvp.base.delegate

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.xdja.easymvp.integration.EventBusManager

/**
 * @author yuanwanli
 * @des [FragmentDelegate]默认实现类
 * @date 2020/6/23
 */
class FragmentDelegateImpl(fragmentManager: FragmentManager?, fragment: Fragment?) :
    FragmentDelegate {
    private var mFragmentManager = fragmentManager
    private var mFragment = fragment
    private var iFragment: IFragment? = fragment as IFragment

    override fun onAttach(context: Context) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        iFragment?.apply {
            if (useEventBus()) {
                EventBusManager.register(mFragment!!)
            }
        }
    }

    override fun onCreateView(view: View, savedInstanceState: Bundle?) {

    }

    override fun onActivityCreate(savedInstanceState: Bundle?) {
        iFragment?.initData(savedInstanceState)
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

    override fun onDestroyView() {

    }

    override fun onDestroy() {
        if (iFragment != null && iFragment!!.useEventBus()) {
            EventBusManager.unregister(mFragment!!)
        }
        this.mFragmentManager = null
        this.mFragment = null
        this.iFragment = null
    }

    override fun onDetach() {

    }

    override fun isAdded(): Boolean {
        return mFragment != null && mFragment!!.isAdded
    }
}