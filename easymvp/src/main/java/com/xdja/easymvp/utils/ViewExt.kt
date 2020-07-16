package com.xdja.easymvp.utils

import android.view.View

/**
 * @author yuanwanli
 * @des  针对view的扩展
 * @date 2019/7/30
 */
/*
    扩展点击事件，参数为方法
 */
fun View.onClick(method: () -> Unit): View {
    setOnClickListener { method() }
    return this
}

/*
    扩展视图可见性
 */
fun View.setVisible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}