package com.xdja.app.mvp.ui.activity

import android.os.Bundle
import com.xdja.app.R
import com.xdja.app.mvp.contract.TestContract
import com.xdja.app.mvp.presenter.TestPresenter
import com.xdja.easymvp.base.BaseActivity
import com.xdja.easymvp.base.BaseApplication
import com.xdja.easymvp.http.imageloader.ImageLoader
import com.xdja.easymvp.http.imageloader.glide.ImageConfigImpl
import kotlinx.android.synthetic.main.activity_test.*
import org.koin.androidx.scope.lifecycleScope

/**
 * 描述:
 * Create by yuanwanli
 * Date 2020/07/15
 */
class TestActivity : BaseActivity<TestPresenter>(), TestContract.View {
    override var mPresenter: TestPresenter? = lifecycleScope.get()
    lateinit var  imageLoader: ImageLoader<ImageConfigImpl>
    override fun initView(savedInstanceState: Bundle?): Int = R.layout.activity_test


    override fun initData(savedInstanceState: Bundle?) {
        val builder = ImageConfigImpl.Builder()
        builder.url="http://pic1.win4000.com/pic/c/cf/cdc983699c.jpg"
        builder.placeholder=R.mipmap.ic_launcher
        builder.imageView=ivTest
        imageLoader= (application as BaseApplication).getAppComponent().imageLoader
        imageLoader.loadImage(this,builder.build())
    }
}
