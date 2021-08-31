package com.xdja.easymvp.integration.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.util.Preconditions
import com.xdja.easymvp.base.delegate.ACTIVITY_DELEGATE
import com.xdja.easymvp.base.delegate.ActivityDelegate
import com.xdja.easymvp.base.delegate.ActivityDelegateImpl
import com.xdja.easymvp.base.delegate.IActivity
import com.xdja.easymvp.integration.ConfigModule
import com.xdja.easymvp.integration.cache.Cache
import com.xdja.easymvp.integration.cache.IntelligentCache

/**
 * @author yuanwanli
 * @des  [Application.ActivityLifecycleCallbacks] 默认实现类
 * 通过[ActivityDelegate] 管理[Activity]
 * @date 2020/7/2
 */
class ActivityLifecycle(
    val application: Application, val mExtras: Cache<String, Any>,
    val mFragmentLifecycle: FragmentManager.FragmentLifecycleCallbacks
    , val mFragmentLifecycles: MutableList<FragmentManager.FragmentLifecycleCallbacks>
) : Application.ActivityLifecycleCallbacks {

    override fun onActivityPaused(activity: Activity) {
        val activityDelegate = fetchActivityDelegate(activity)
        activityDelegate?.onPause()
    }

    override fun onActivityStarted(activity: Activity) {
        val activityDelegate = fetchActivityDelegate(activity)
        activityDelegate?.onStart()
    }

    override fun onActivityDestroyed(activity: Activity) {
        val activityDelegate = fetchActivityDelegate(activity)
        if (activityDelegate != null) {
            activityDelegate.onDestroy()
            getCacheFromActivity(activity as IActivity).clear()
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        val activityDelegate = fetchActivityDelegate(activity)
        activityDelegate?.onSaveInstanceState(outState)
    }

    override fun onActivityStopped(activity: Activity) {
        val activityDelegate = fetchActivityDelegate(activity)
        activityDelegate?.onStop()
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity is IActivity) {
            var activityDelegate = fetchActivityDelegate(activity)
            if (activityDelegate == null) {
                val cache = getCacheFromActivity(activity)
                activityDelegate = ActivityDelegateImpl(activity)
                //使用IntelligentCache.KEY_KEEP 作为key的前缀,可以使存储的数据永久储存在内存中
                //否则存储在LRU算法的存储空间中,前提是activity使用的是IntelligentCache (框架默认使用)
                cache.put(IntelligentCache.getKeyOfKeep(ACTIVITY_DELEGATE), activityDelegate)
            }
            activityDelegate.onCreate(savedInstanceState)
        }
        registerFragmentCallbacks(activity)
    }

    /**
     * 给每个Activity的所有Fragment设置监听生命周期,Activity可以通过[IActivity.useFragment]设置是否使用监听
     * 如果这个Activity返回false的话,这个Activity下面的所有Fragment将不能使用[Fragm]
     */
    private fun registerFragmentCallbacks(activity: Activity) {
        val useFragment = activity !is IActivity || activity.useFragment()
        if (activity is FragmentActivity && useFragment) {
            //mFragmentLifecycle为Fragment生命周期实现类,用于框架内部对每个Fragment的必要操作
            //如给每个Fragment配置FragmentDelegate注册框架内部已实现的Fragment生命周期逻辑
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                mFragmentLifecycle,
                true
            )
            if (mExtras.containsKey(IntelligentCache.getKeyOfKeep(ConfigModule::class.java.name))) {
                val modules =
                    mExtras.get(IntelligentCache.getKeyOfKeep(ConfigModule::class.java.name)) as List<ConfigModule>?
                modules?.forEach {
                    it.injectFragmentLifecycle(application, mFragmentLifecycles)
                }
            }
            mExtras.remove(IntelligentCache.getKeyOfKeep(ConfigModule::class.java.name))
            //注册框架外部,开发者扩展的Fragment生命周期逻辑
            mFragmentLifecycles.forEach {
                (activity as FragmentActivity).supportFragmentManager.registerFragmentLifecycleCallbacks(
                    it,
                    true
                )
            }
        }
    }

    override fun onActivityResumed(activity: Activity) {
        val activityDelegate = fetchActivityDelegate(activity)
        activityDelegate?.onResume()
    }

    private fun fetchActivityDelegate(activity: Activity): ActivityDelegate? {
        var activityDelegate: ActivityDelegate? = null
        if (activity is IActivity) {
            val cache = getCacheFromActivity(activity)
            val activityCache = cache.get(IntelligentCache.getKeyOfKeep(ACTIVITY_DELEGATE))
            if (activityCache != null && activityCache is ActivityDelegate) {
                activityDelegate = activityCache
            }
        }
        return activityDelegate
    }

    private fun getCacheFromActivity(activity: IActivity): Cache<String, Any> {
        val cache = activity.provideCache()
        Preconditions.checkNotNull(cache, "${Cache::class.java.name} cannot be null on Activity")
        return cache!!
    }
}