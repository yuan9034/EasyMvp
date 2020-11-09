package com.xdja.app.mvp.contract

import com.xdja.app.http.TestBean
import com.xdja.easymvp.mvp.IModel
import com.xdja.easymvp.mvp.IView
import kotlinx.coroutines.flow.Flow

/**
 * @author yuanwanli
 * @des
 * @date 2020/7/10
 */
interface UserContract {
    interface View : IView {
        fun showBean(bean: TestBean)
    }

    interface Model : IModel {
        fun getTest1(): Flow<TestBean>
        suspend fun getTest(): TestBean
    }
}