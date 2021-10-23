package com.xdja.easymvp.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.impl.LoadingPopupView
import com.xdja.easymvp.base.delegate.IFragment
import com.xdja.easymvp.integration.cache.Cache
import com.xdja.easymvp.integration.cache.CacheType
import com.xdja.easymvp.mvp.IPresenter
import com.xdja.easymvp.mvp.IView
import com.xdja.easymvp.utils.AppComponentUtils

/**
 * @author yuanwanli
 * @des   因为单继承,所以如果需要用到继承特定[Fragment]的三方库,那你就需要自己自定义[Fragment]
 * 继承于这个特定的fragment,然后再按照[BaseFragment]的格式,将代码复制过去
 * 记住一定要实现[IFragment]
 * @date 2020/6/28
 */
abstract class BaseFragment<P : IPresenter> : Fragment(), IFragment,IView {
    protected open var mPresenter: P? = null
    protected var mCache: Cache<*, *>? = null
    protected var mContext: Context? = null
    private var loadingPopup: BasePopupView? = null
    override fun provideCache(): Cache<*, *>? {
        if (mCache == null) {
            mCache =
                AppComponentUtils.getAppComponentFromContext(requireActivity()).cacheFactory.build(
                    CacheType.FRAGMENT_CACHE
                )
        }
        return mCache
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun useEventBus(): Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = initView(inflater, container, savedInstanceState)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (mPresenter != null) {
            lifecycle.addObserver(mPresenter!!)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.mPresenter = null
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }
    override fun showLoading(message: String) {
        super.showLoading(message)
        if (loadingPopup == null) {
            loadingPopup = XPopup.Builder(requireContext())
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