package com.example.echo_kt.di

import android.content.Context
import com.example.echo_kt.room.AppDataBase
import com.example.echo_kt.room.CustomSongListDao
import com.example.echo_kt.room.HistorySongListDao
import com.example.echo_kt.room.SongDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDataBase {
        return AppDataBase.getInstance()
    }

    @Provides
    fun provideHistoryListDao(appDatabase: AppDataBase): HistorySongListDao {
        return appDatabase.historyAudioDao()
    }

    @Provides
    fun provideCustomListDao(appDatabase: AppDataBase): CustomSongListDao {
        return appDatabase.customSongListDao()
    }

    @Provides
    fun provideSongDao(appDatabase: AppDataBase): SongDao {
        return appDatabase.songDao()
    }
}
