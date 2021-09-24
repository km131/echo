package com.example.echo_kt.di

import com.example.echo_kt.api.kugou.KuGouServer
import com.example.echo_kt.api.migu.MiguMusicServer
import com.example.echo_kt.api.qqmusic.QQMusicServer
import com.example.echo_kt.api.wyymusic.WyyMusicServer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideKuGouListService(): KuGouServer {
        return KuGouServer.create()
    }
    @Singleton
    @Provides
    fun provideQQListService(): QQMusicServer {
        return QQMusicServer.create()
    }
    @Singleton
    @Provides
    fun provideMiGuListService(): MiguMusicServer {
        return MiguMusicServer.create(0)
    }
    @Singleton
    @Provides
    fun provideWyyListService(): WyyMusicServer {
        return WyyMusicServer.create()
    }
}
