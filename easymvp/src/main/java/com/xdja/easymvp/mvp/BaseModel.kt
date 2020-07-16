package com.xdja.easymvp.mvp

import com.xdja.easymvp.integration.IRepositoryManager

/**
 * @author yuanwanli
 * @des   基类 Model
 * @date 2020/6/28
 */
open class BaseModel(var mRepositoryManager: IRepositoryManager?) : IModel {
    /**
     * 在框架中[BasePresenter.onDestroy]会默认调用[IModel.onDestroy]
     */
    override fun onDestroy() {
        mRepositoryManager = null
    }
}