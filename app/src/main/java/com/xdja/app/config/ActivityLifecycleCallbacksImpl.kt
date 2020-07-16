package com.xdja.app.config

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.xdja.app.R
import timber.log.Timber
import java.util.*

/**
 * @author yuanwanli
 * @des   展示 [Application.ActivityLifecycleCallbacks]的用法
 * @date 2020/7/6
 */
class ActivityLifecycleCallbacksImpl : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(
        activity: Activity?,
        savedInstanceState: Bundle?
    ) {
        Timber.i("%s - onActivityCreated", activity)
    }

    override fun onActivityStarted(activity: Activity) {
        Timber.i("%s - onActivityStarted", activity)
        if (!activity.intent.getBooleanExtra("isInitToolbar", false)) {
            //由于加强框架的兼容性,故将 setContentView 放到 onActivityCreated 之后,onActivityStarted 之前执行
            //而 findViewById 必须在 Activity setContentView() 后才有效,所以将以下代码从之前的 onActivityCreated 中移动到 onActivityStarted 中执行
            activity.intent.putExtra("isInitToolbar", true)
            //这里全局给Activity设置toolbar和title,你想象力有多丰富,这里就有多强大,以前放到BaseActivity的操作都可以放到这里
            if (activity.findViewById<View?>(R.id.toolbar) != null) {
                if (activity is AppCompatActivity) {
                    activity.setSupportActionBar(activity.findViewById(R.id.toolbar))
                    Objects.requireNonNull(activity.supportActionBar)!!
                        .setDisplayShowTitleEnabled(false)
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        activity.setActionBar(activity.findViewById(R.id.toolbar))
                        Objects.requireNonNull(activity.actionBar)!!
                            .setDisplayShowTitleEnabled(false)
                    }
                }
            }
            if (activity.findViewById<View?>(R.id.toolbar_title) != null) {
                (activity.findViewById<View>(R.id.toolbar_title) as TextView).text = activity.title
            }
            if (activity.findViewById<View?>(R.id.toolbar_back) != null) {
                activity.findViewById<View>(R.id.toolbar_back)
                    .setOnClickListener { v: View? -> activity.onBackPressed() }
            }
        }
    }

    override fun onActivityResumed(activity: Activity?) {
        Timber.i("%s - onActivityResumed", activity)
    }

    override fun onActivityPaused(activity: Activity?) {
        Timber.i("%s - onActivityPaused", activity)
    }

    override fun onActivityStopped(activity: Activity?) {
        Timber.i("%s - onActivityStopped", activity)
    }

    override fun onActivitySaveInstanceState(
        activity: Activity?,
        outState: Bundle?
    ) {
        Timber.i("%s - onActivitySaveInstanceState", activity)
    }

    override fun onActivityDestroyed(activity: Activity) {
        Timber.i("%s - onActivityDestroyed", activity)
        //横竖屏切换或配置改变时, Activity 会被重新创建实例, 但 Bundle 中的基础数据会被保存下来,移除该数据是为了保证重新创建的实例可以正常工作
        activity.intent.removeExtra("isInitToolbar")
    }
}