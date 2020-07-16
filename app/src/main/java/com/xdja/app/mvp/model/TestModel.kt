package com.xdja.app.mvp.model

import com.xdja.app.mvp.contract.TestContract
import com.xdja.easymvp.integration.IRepositoryManager
import com.xdja.easymvp.mvp.BaseModel

/**
 * 描述:
 * Create by yuanwanli
 * Date 2020/07/15
 */
class TestModel(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager),
    TestContract.Model {

}
