package com.xdja.app.di

import com.xdja.app.mvp.contract.UserContract
import com.xdja.app.mvp.model.UserModel
import com.xdja.app.mvp.presenter.UserPresenter
import com.xdja.app.mvp.ui.activity.UserActivity
import org.koin.dsl.module

val UserModule = module {
    scope<UserActivity> {
        scoped<UserContract.Model> { UserModel(get()) }
        scoped {
            UserPresenter(get(), this.getSource())
        }
    }
}
