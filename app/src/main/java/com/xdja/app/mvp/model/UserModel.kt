package com.xdja.app.mvp.model

import com.xdja.app.http.ServerApi
import com.xdja.app.http.TestBean
import com.xdja.app.mvp.contract.UserContract
import com.xdja.easymvp.integration.IRepositoryManager
import com.xdja.easymvp.mvp.BaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

/**
 * @author yuanwanli
 * @des
 * @date 2020/7/10
 */
class UserModel(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager),
    UserContract.Model {
    override suspend fun getTest(): TestBean {
        return withContext(Dispatchers.IO) {
            mRepositoryManager!!.obtainRetrofitService(ServerApi::class.java)
                .getServer("https://wanandroid.com/wxarticle/chapters/json", 2)
        }
    }

    override fun getTest1(): Flow<TestBean> = flow {
        emit(
            mRepositoryManager!!.obtainRetrofitService(ServerApi::class.java)
                .getServer("https://wanandroid.com/wxarticle/chapters/json", 2)
        )
    }
}