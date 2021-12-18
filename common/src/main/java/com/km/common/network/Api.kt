package com.km.common.network

import com.km.common.bean.request.GetMetroCardRequest
import com.km.common.bean.request.LoginRequest
import com.km.common.bean.request.ResetPwdRequest
import com.km.common.bean.response.EmptyResponse
import com.km.common.bean.response.LoginResponse
import com.km.common.bean.response.NearSellerResponse
import com.km.common.bean.response.QueryMetroCardResponse
import com.km.common.bean.response.UserInfoResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    /**
     * 登录
     */
    @POST("prod-api/api/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
    /**
     * 注销
     */
    @POST("logout")
    fun logout(@Header("Authorization") auth:String): Call<EmptyResponse>

    /**
     * 查询个人信息
     */
    @GET("prod-api/api/common/user/getInfo")
    fun getUserInfo(@Header("Authorization") auth:String): Call<UserInfoResponse>

    /**
     * 修改密码
     */
    @PUT("prod-api/api/common/user/resetPwd")
    fun resetPwd(@Header("Authorization") auth:String, @Body resetPwdRequest: ResetPwdRequest): Call<EmptyResponse>

    /**
     * 领取乘车卡
     */
    @POST("prod-api/api/metro/card")
    fun getMetroCard(@Header("Authorization") auth:String, @Body response: GetMetroCardRequest): Call<EmptyResponse>
    /**
     * 注销乘车卡
     */
    @DELETE("prod-api/api/metro/card/{id}")
    fun deleteMetroCard(@Header("Authorization") auth:String, @Path("id")id:String): Call<EmptyResponse>

    /**
     * 查询乘车卡
     */
    @GET("prod-api/api/metro/card")
    fun queryMetroCard(@Header("Authorization") auth:String): Call<QueryMetroCardResponse>

    /**
     * 查询附近外卖商家
     */
    @GET("prod-api/api/takeout/seller/near")
    fun queryNearSeller(@Header("Authorization") auth:String, @Query("pageNum") pageNum:Int, @Query("pageSize")pageSize:Int): Call<NearSellerResponse>
}