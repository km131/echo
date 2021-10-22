package com.example.echo_kt.api

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import okio.ForwardingSource

import okio.BufferedSource


class ProgressResponseBody internal constructor(
    private val url: String,
    private val responseBody: ResponseBody,
    private val progressListener: ProgressListener
) :
    ResponseBody() {
    private var bufferedSource: BufferedSource? = null
    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = source(responseBody.source()).buffer()
        }
        return bufferedSource as BufferedSource
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead = 0L

            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                // 返回已读的字节数，如果源耗尽则返回-1
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                progressListener.update(
                    url,
                    totalBytesRead,
                    responseBody.contentLength(),
                    bytesRead == -1L
                )
                return bytesRead
            }
        }
    }
}
interface ProgressListener {
    fun update(url: String, bytesRead: Long, contentLength: Long, done: Boolean)
}