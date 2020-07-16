package com.xdja.app.di

import com.xdja.app.mvp.contract.UserContract
import com.xdja.app.mvp.model.UserModel
import com.xdja.app.mvp.presenter.UserPresenter
import com.xdja.app.mvp.ui.activity.UserActivity
import org.koin.dsl.module

/**
 * @author yuanwanli
 * @des
 * @date 2020/7/11
 */
val MvpModule = module {
    scope<UserActivity> {
        scoped<UserContract.Model> { UserModel(get()) }
        scoped {
            UserPresenter(get(), this.getSource())
        }
    }
}