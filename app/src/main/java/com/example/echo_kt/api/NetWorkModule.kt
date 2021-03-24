package com.example.echo_kt.api

class NetWorkModule {
    fun provideKuGouService(): KuGouServer {
        return KuGouServer.create()
    }
}