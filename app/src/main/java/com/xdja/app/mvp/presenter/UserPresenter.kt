package com.xdja.app.mvp.presenter

import com.xdja.app.mvp.contract.UserContract
import com.xdja.easymvp.mvp.BasePresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author yuanwanli
 * @des
 * @date 2020/7/10
 */
class UserPresenter(model: UserContract.Model, rootView: UserContract.View) :
    BasePresenter<UserContract.Model, UserContract.View>(model, rootView) {

    fun getTest() {
        launch {
            var bean = withContext(Dispatchers.IO) {
                mModel!!.getTest()
            }
            mRootView!!.showBean(bean)
        }
    }

}