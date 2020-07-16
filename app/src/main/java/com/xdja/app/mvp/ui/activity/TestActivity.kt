package com.xdja.app.mvp.ui.activity

import android.os.Bundle
import com.xdja.app.R
import com.xdja.app.mvp.contract.TestContract
import com.xdja.app.mvp.presenter.TestPresenter
import com.xdja.easymvp.base.BaseActivity
import org.koin.androidx.scope.lifecycleScope

/**
 * 描述:
 * Create by yuanwanli
 * Date 2020/07/15
 */
class TestActivity : BaseActivity<TestPresenter>(), TestContract.View {
    override var mPresenter: TestPresenter? = lifecycleScope.get()

    override fun initView(savedInstanceState: Bundle?): Int = R.layout.activity_test


    override fun initData(savedInstanceState: Bundle?) {

    }
}
