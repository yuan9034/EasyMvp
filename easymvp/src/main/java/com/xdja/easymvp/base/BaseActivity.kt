package com.xdja.easymvp.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.impl.LoadingPopupView
import com.xdja.easymvp.base.delegate.IActivity
import com.xdja.easymvp.integration.cache.Cache
import com.xdja.easymvp.integration.cache.CacheType
import com.xdja.easymvp.mvp.IPresenter
import com.xdja.easymvp.mvp.IView
import com.xdja.easymvp.utils.AppComponentUtils

/**
 * @author yuanwanli
 * @des   只能单继承,所以如果要用到继承特定[AppCompatActivity]的三方库,那你就需要自己自定义
 * 继承于这个特定的[AppCompatActivity],然后再按照[BaseActivity]的格式,将代码复制过去,记住一定要实现[IActivity]
 * @date 2020/6/24
 */
abstract class BaseActivity<P : IPresenter> : AppCompatActivity(), IActivity ,IView{
    protected val TAG = this.javaClass.simpleName
    private var loadingPopup: BasePopupView? = null
    //如果当前界面逻辑简单,Presenter可以为空
    protected open var mPresenter: P? = null
    private var mCache: Cache<String, Any>? = null
    override fun provideCache(): Cache<String, Any> {
        if (mCache == null) {
            mCache =
                AppComponentUtils.getAppComponentFromContext(this).cacheFactory.build(CacheType.ACTIVITY_CACHE) as Cache<String, Any>
        }
        return mCache!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutResId = initView(savedInstanceState)
        setContentView(layoutResId)
        if (mPresenter != null) {
            lifecycle.addObserver(mPresenter!!)
        }
        initData(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        this.mPresenter = null
    }

    override fun showLoading(message: String) {
        super.showLoading(message)
        if (loadingPopup == null) {
            loadingPopup = XPopup.Builder(this)
                .hasShadowBg(true)
                .dismissOnBackPressed(false)
                .asLoading(message)
                .show() as LoadingPopupView
        } else {
            loadingPopup?.toggle()
        }
    }

    override fun hideLoading() {
        super.hideLoading()
        loadingPopup?.dismiss()
    }
}