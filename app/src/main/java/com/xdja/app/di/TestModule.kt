package com.xdja.app.di

import com.xdja.app.mvp.contract.TestContract
import com.xdja.app.mvp.model.TestModel
import com.xdja.app.mvp.presenter.TestPresenter
import com.xdja.app.mvp.ui.activity.TestActivity
import org.koin.dsl.module

val TestModule = module {
    scope<TestActivity> {
        scoped<TestContract.Model> { TestModel(get()) }
        scoped {
            TestPresenter(get(), this.getSource())
        }
    }
}
