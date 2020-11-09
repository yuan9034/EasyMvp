package com.xdja.app.mvp.presenter

import com.xdja.app.mvp.contract.UserContract
import com.xdja.easymvp.mvp.BasePresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn

/**
 * @author yuanwanli
 * @des
 * @date 2020/7/10
 */
class UserPresenter(model: UserContract.Model, rootView: UserContract.View) :
    BasePresenter<UserContract.Model, UserContract.View>(model, rootView) {
    fun getTest1() {
        launch {
            mModel!!.getTest1()
                .flowOn(Dispatchers.IO)
                .collect {
                    mRootView!!.showBean(it)
                }
        }
    }

    fun getTest() {
        launch {
            delay(2000)
            val bean = mModel!!.getTest()
            mRootView!!.showBean(bean)
        }
    }

}