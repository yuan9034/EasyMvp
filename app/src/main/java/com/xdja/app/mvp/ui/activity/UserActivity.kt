package com.xdja.app.mvp.ui.activity

import android.os.Bundle
import com.blankj.utilcode.util.LogUtils
import com.xdja.app.R
import com.xdja.app.http.TestBean
import com.xdja.app.mvp.contract.UserContract
import com.xdja.app.mvp.presenter.UserPresenter
import com.xdja.easymvp.base.BaseActivity
import com.xdja.easymvp.integration.IRepositoryManager
import org.koin.android.ext.android.get
import org.koin.androidx.scope.lifecycleScope

/**
 * @author yuanwanli
 * @des
 * @date 2020/7/10
 */
class UserActivity : BaseActivity<UserPresenter>(), UserContract.View {
    override var mPresenter: UserPresenter? = lifecycleScope.get()

    override fun initView(savedInstanceState: Bundle?): Int = R.layout.activity_user

    override fun initData(savedInstanceState: Bundle?) {
        val get = get<IRepositoryManager>()
        LogUtils.e(get)
        mPresenter?.getTest1()
    }

    override fun showBean(bean: TestBean) {
        LogUtils.e(bean)
    }
}