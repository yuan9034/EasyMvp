package com.xdja.easymvp.base

import com.xdja.easymvp.mvp.IPresenter

/**
 * @author yuanwanli
 * @des   配合viewpager2实现懒加载
 * @sample
 * viewPager2.adapter = NewsHomeAdapter(this@NewsHomeActivity, data)
 * TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
 * tab.text = data[position].name
 * }.attach()
 * @date 2020/6/28
 */
abstract class BaseLazyLoadFragment<P : IPresenter> : BaseFragment<P>() {
    private var isFirstLoad = true
    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            isFirstLoad = false
            initData(null)
        }
    }
}