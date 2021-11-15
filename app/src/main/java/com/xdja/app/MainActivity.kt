package com.xdja.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.LogUtils
import com.google.android.material.tabs.TabLayoutMediator
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
import androidx.recyclerview.widget.RecyclerView
import com.xdja.app.mvp.ui.fragment.Test3Fragment


class MainActivity : AppCompatActivity() {
    private val tabTitles = arrayOf("接单", "配货中", "待付款","已完成")
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
        fragments.add(Test3Fragment.newInstance())
        viewPager.adapter = ViewPagerAdapter(fragments, this)
        //禁用预加载
        viewPager.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
        viewPager.isUserInputEnabled = false
        mTabLayout.addOnTabSelectedListener(object : CustomTabSelectedListener() {
            override fun onTabSelected(tabText: String) {
                when (tabText) {
                    getString(R.string.wait_pay) -> viewPager.setCurrentItem(0,false)
                    getString(R.string.already_pay) -> viewPager.setCurrentItem(1,false)
                    getString(R.string.be_voided) -> viewPager.setCurrentItem(2,false)
                }
            }
        })
    }
}