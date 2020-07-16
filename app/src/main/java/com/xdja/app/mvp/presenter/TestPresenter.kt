package com.xdja.app.mvp.presenter

import com.xdja.app.mvp.contract.TestContract
import com.xdja.easymvp.mvp.BasePresenter

/**
 * 描述:
 * Create by yuanwanli
 * Date 2020/07/15
 */
class TestPresenter(model: TestContract.Model, rootView: TestContract.View) :
    BasePresenter<TestContract.Model, TestContract.View>(model, rootView) {

}
