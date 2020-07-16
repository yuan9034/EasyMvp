package com.xdja.easymvp.mvp

/**
 * @author yuanwanli
 * @des   框架要求每个Model都需要实现此类,以满足规范
 * @date 2020/6/28
 */
interface IModel {
    /**
     *在框架中[BasePresenter.onDestroy] 会默认调用[IModel.onDestroy]
     */
    fun onDestroy()
}