package com.example.echo_kt.api.kugou

import com.example.echo_kt.api.ProgressListener
import com.example.echo_kt.utils.ProgressInterceptor
import io.reactivex.rxjava3.core.Observable
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url


interface KuGouServer {
    /**
     * @param mid 32位随机字符，和cookie相似，不过这个是由客户端生成的（亲测只要长度大于零就行了）
     * 亲测除了r、hash、mid、album_id之外其他参数都可省略不影响返回值
     */
    @GET("yy/index.php")
    suspend fun searchMusic(
        @Query("r") r: String = "play/getdata",
        @Query("hash") hash: String,
        @Query("dfid") dfid: String = "4FIiZe4KfpR41bWuxj7RfBU3",
        @Query("appid") appid: String = "1014",
        @Query("mid") mid: String = "7a570de8defbf35f64463a0fe6f6e12b",
        @Query("platid") platid: String = "4",
        @Query("album_id") albumId: String
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
    fun downloadFile(@Url url: String): Observable<ResponseBody>

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
        private const val SINGLE_URL = "https://wwwapi.kugou.com/"

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