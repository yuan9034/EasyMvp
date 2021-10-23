package com.xdja.easymvp.mvp

import com.blankj.utilcode.util.ToastUtils

/**
 * @author yuanwanli
 * @des   框架要求每个View都需要实现此类,以满足规范
 * @date 2020/6/28
 */
interface IView {
    /**
     * 显示加载
     */
    fun showLoading(message:String="") {

    }

    /**
     * 隐藏加载
     */
    fun hideLoading() {

    }

    /**
     * [message] 消息内容,不能为空
     */
    fun showMessage(message: String) {
        ToastUtils.showShort(message)
    }

    /**
     * 结束自己
     */
    fun killMyself() {

    }
}