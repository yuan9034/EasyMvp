package com.xdja.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.LogUtils
import com.xdja.app.adapter.ViewPagerAdapter
import com.xdja.app.mvp.ui.activity.TestActivity
import com.xdja.app.mvp.ui.activity.UserActivity
import com.xdja.app.mvp.ui.fragment.Test1Fragment
import com.xdja.app.mvp.ui.fragment.Test2Fragment
import com.xdja.easymvp.utils.onClick
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.e("onCreate")
        setContentView(R.layout.activity_main)
        btnClick.onClick {
            startActivity<UserActivity>()
        }
        var fragments= mutableListOf<Fragment>()
        fragments.add(Test1Fragment.newInstance())
        fragments.add(Test2Fragment.newInstance())
        viewpage.adapter=ViewPagerAdapter(fragments,supportFragmentManager)
        viewpage.currentItem=0
        viewpage.offscreenPageLimit=2
    }
}