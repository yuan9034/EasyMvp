package com.xdja.app.config

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import timber.log.Timber

/**
 * @author yuanwanli
 * @des   [FragmentManager.FragmentLifecycleCallbacks]的用法
 * @date 2020/7/6
 */
class FragmentLifecycleCallbacksImpl :
    FragmentManager.FragmentLifecycleCallbacks() {
    override fun onFragmentAttached(
        fm: FragmentManager,
        f: Fragment,
        context: Context
    ) {
        Timber.i("%s - onFragmentAttached", f.toString())
    }

    override fun onFragmentCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        Timber.i("%s - onFragmentCreated", f.toString())
        // 在配置变化的时候将这个 Fragment 保存下来,在 Activity 由于配置变化重建时重复利用已经创建的 Fragment。
        // https://developer.android.com/reference/android/app/Fragment.html?hl=zh-cn#setRetainInstance(boolean)
        // 如果在 XML 中使用 <Fragment/> 标签,的方式创建 Fragment 请务必在标签中加上 android:id 或者 android:tag 属性,否则 setRetainInstance(true) 无效
        // 在 Activity 中绑定少量的 Fragment 建议这样做,如果需要绑定较多的 Fragment 不建议设置此参数,如 ViewPager 需要展示较多 Fragment
        f.retainInstance = true
    }

    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
        Timber.i("%s - onFragmentViewCreated", f.toString())
    }

    override fun onFragmentActivityCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        Timber.i("%s - onFragmentActivityCreated", f.toString())
    }

    override fun onFragmentStarted(
        fm: FragmentManager,
        f: Fragment
    ) {
        Timber.i("%s - onFragmentStarted", f.toString())
    }

    override fun onFragmentResumed(
        fm: FragmentManager,
        f: Fragment
    ) {
        Timber.i("%s - onFragmentResumed", f.toString())
    }

    override fun onFragmentPaused(
        fm: FragmentManager,
        f: Fragment
    ) {
        Timber.i("%s - onFragmentPaused", f.toString())
    }

    override fun onFragmentStopped(
        fm: FragmentManager,
        f: Fragment
    ) {
        Timber.i("%s - onFragmentStopped", f.toString())
    }

    override fun onFragmentSaveInstanceState(
        fm: FragmentManager,
        f: Fragment,
        outState: Bundle
    ) {
        Timber.i("%s - onFragmentSaveInstanceState", f.toString())
    }

    override fun onFragmentViewDestroyed(
        fm: FragmentManager,
        f: Fragment
    ) {
        Timber.i("%s - onFragmentViewDestroyed", f.toString())
    }

    override fun onFragmentDestroyed(
        fm: FragmentManager,
        f: Fragment
    ) {
        Timber.i("%s - onFragmentDestroyed", f.toString())
    }

    override fun onFragmentDetached(
        fm: FragmentManager,
        f: Fragment
    ) {
        Timber.i("%s - onFragmentDetached", f.toString())
    }
}