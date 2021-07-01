package com.example.echo_kt.api.wyymusic

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface WyyMusicServer {

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @POST("weapi/cloudsearch/get/web")
    @FormUrlEncoded
    suspend fun searchList(
        @Field("params") params: String,
        @Field("encSecKey") encSecKey: String
    ): WyySearchListBean

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @POST("weapi/song/enhance/player/url/v1")
    @FormUrlEncoded
    suspend fun searchPath(
        @Field("params") params: String,
        @Field("encSecKey") encSecKey: String
    ): WyyPathBean

    companion object {
        private const val BASE_URL = "https://music.163.com/"

        /**
         * 搜索列表和请求地址参数接口
         */
        fun create(): WyyMusicServer {

            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WyyMusicServer::class.java)
        }
    }
}