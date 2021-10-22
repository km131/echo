package com.example.echo_kt.api.kugou

import com.example.echo_kt.api.ProgressListener
import com.example.echo_kt.util.ProgressInterceptor
import io.reactivex.rxjava3.core.Observable
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface KuGouServer {
    @GET("/v1/kugou/song")
    suspend fun searchMusic(
        @Query("aid")
        aid: String,
        @Query("hash")
        hash: String
    ): SearchMusicDetails

    @GET("song")
    suspend fun searchMusicList(
        @Query("format")
        format: String,
        @Query("keyWord")
        keyWord: String,
        @Query("page")
        page: Int,
        @Query("pageSize")
        paceSize: Int,
        @Query("showtype")
        showtype: Int
    ): KuGouSearchBean

    /**
     * 用 suspend 修饰会导致报错，具体原因未知
     */
    @Streaming
    @GET
    fun downloadFile( @Url url: String): Observable<ResponseBody>

    companion object {
        //根据关键字搜索歌曲
        // http://mobilecdn.kugou.com/api/v3/search/song?
        // format=json
        // &keyword=%E7%8E%8B%E5%8A%9B%E5%AE%8F
        // &page=1
        // &pagesize=20
        // &showtype=1
        private const val BASE_URL = "http://mobilecdn.kugou.com/api/v3/search/"

        //获取歌曲详情，带MP3链接
        private const val SINGLE_URL = "http://iecoxe.top:5000/"

        /**
         * 搜索列表接口
         */
        fun create(): KuGouServer {

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
                .create(KuGouServer::class.java)
        }

        /**
         * 歌曲详情接口
         */
        fun create2(): KuGouServer {

            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(SINGLE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(KuGouServer::class.java)
        }

        /**
         * 下载歌曲接口
         */
        fun create3(listener: ProgressListener): KuGouServer {

            val interceptor = ProgressInterceptor(listener)
            val retrofitBuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
            return retrofitBuilder
                .client(
                    OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .retryOnConnectionFailure(true).build()
                ).baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(KuGouServer::class.java)
        }
    }
}