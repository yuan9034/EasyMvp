package com.xdja.easymvp.integration.lifecycle

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.util.Preconditions
import com.xdja.easymvp.base.delegate.FRAGMENT_DELEGATE
import com.xdja.easymvp.base.delegate.FragmentDelegate
import com.xdja.easymvp.base.delegate.FragmentDelegateImpl
import com.xdja.easymvp.base.delegate.IFragment
import com.xdja.easymvp.integration.cache.Cache
import com.xdja.easymvp.integration.cache.IntelligentCache

/**
 * @author yuanwanli
 * @des   [FragmentManager.FragmentLifecycleCallbacks]默认实现类
 *通过[FragmentDelegate]管理[Fragment]
 * @date 2020/7/3
 */
class FragmentLifecycle : FragmentManager.FragmentLifecycleCallbacks() {
    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        if (f is IFragment) {
            var fragmentDelegate = fetchFragmentDelegate(f)
            if (fragmentDelegate == null || !fragmentDelegate.isAdded()) {
                val cache = getCacheFromFragment(f)
                fragmentDelegate = FragmentDelegateImpl(fm, f)
                //使用 IntelligentCache.KEY_KEEP 作为 key 的前缀, 可以使储存的数据永久存储在内存中
                //否则存储在 LRU 算法的存储空间中, 前提是 Fragment 使用的是 IntelligentCache (框架默认使用)
                cache.put(IntelligentCache.getKeyOfKeep(FRAGMENT_DELEGATE), fragmentDelegate)
            }
            fragmentDelegate.onAttach(context)
        }
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onCreate(savedInstanceState)
    }

    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onCreateView(v, savedInstanceState)
    }

    override fun onFragmentActivityCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onActivityCreate(savedInstanceState)
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onStart()
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onResume()
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onPause()
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onStop()
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onDestroyView()
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onDestroy()
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onDetach()
    }

    private fun fetchFragmentDelegate(fragment: Fragment): FragmentDelegate? {
        var fragmentDelegate: FragmentDelegate? = null
        if (fragment is IFragment) {
            val cache = getCacheFromFragment(fragment)
            val fragmentCache = cache.get(IntelligentCache.getKeyOfKeep(FRAGMENT_DELEGATE))
            if (fragmentCache != null && fragmentCache is FragmentDelegate) {
                fragmentDelegate = fragmentCache
            }
        }
        return fragmentDelegate
    }

    private fun getCacheFromFragment(fragment: IFragment): Cache<String, Any> {
        val provideCache = fragment.provideCache()
        Preconditions.checkNotNull(
            provideCache,
            "${Cache::class.java.name} cannot be null on Fragment"
        )
        return provideCache!! as Cache<String, Any>
    }
}