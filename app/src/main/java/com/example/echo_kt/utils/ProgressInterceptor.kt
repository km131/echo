package com.example.echo_kt.utils

import com.example.echo_kt.api.ProgressListener
import com.example.echo_kt.api.ProgressResponseBody
import okhttp3.Interceptor
import okhttp3.Response


class ProgressInterceptor(private val progressListener: ProgressListener) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse: Response = chain.proceed(chain.request())
        return originalResponse.newBuilder()
            .body(
                ProgressResponseBody(
                    chain.request().url.toUrl().toString(),
                    originalResponse.body!!,
                    progressListener
                )
            )
            .build()
    }
}