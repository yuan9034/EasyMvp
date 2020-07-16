package com.xdja.app.http

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Url


interface ServerApi {
    /**
     * @return 测试
     */
    @POST
    @FormUrlEncoded
    suspend fun getServer(@Url url: String, @Field("pageSize") pageSize: Int): TestBean
}