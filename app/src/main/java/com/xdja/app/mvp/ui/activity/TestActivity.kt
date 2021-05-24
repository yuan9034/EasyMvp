package com.xdja.app.mvp.ui.activity

import android.os.Bundle
import com.blankj.utilcode.util.LogUtils
import com.xdja.app.R
import com.xdja.app.mvp.contract.TestContract
import com.xdja.app.mvp.presenter.TestPresenter
import com.xdja.easymvp.base.BaseActivity
import com.xdja.easymvp.base.BaseApplication
import com.xdja.easymvp.http.imageloader.ImageLoader
import com.xdja.easymvp.http.imageloader.glide.ImageConfigImpl
import com.xdja.easymvp.integration.cache.CacheType
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
        val appComponent = (application as BaseApplication).getAppComponent()
        val builder = ImageConfigImpl.Builder()
        builder.url="http://pic1.win4000.com/pic/c/cf/cdc983699c.jpg"
        builder.placeholder=R.mipmap.ic_launcher
        builder.imageView=ivTest
        appComponent.imageLoader.loadImage(this,builder.build())
        //这个extras是全局唯一的,单例实现
        val extras = appComponent.extras
        //这个cache是工厂模式实现,每次都会创建一个新的
        val build = appComponent.cacheFactory.build(CacheType.ACTIVITY_CACHE)
        build.put("a",11111)
        val get = build.get("a")
        LogUtils.e(get)
    }
}
