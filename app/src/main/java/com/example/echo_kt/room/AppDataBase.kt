package com.example.echo_kt.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.echo_kt.BaseApplication
import com.example.echo_kt.data.SongBean
import com.example.echo_kt.data.SongListBean
import com.example.echo_kt.ui.main.HistoryAudioBean


@Database(
    entities = [HistoryAudioBean::class, SongListBean::class, SongBean::class, PlaylistSongCrossRef::class],
    version = 9,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun historyAudioDao(): HistorySongListDao
    abstract fun customSongListDao(): CustomSongListDao
    abstract fun songDao(): SongDao

    companion object {
        @Volatile
        private var instance: AppDataBase? = null

        fun getInstance(): AppDataBase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDataBase(
                        BaseApplication.getContext()
                    )
                        .also { instance = it }
            }
        }

        private fun buildDataBase(context: Context): AppDataBase {
            return Room.databaseBuilder(
                context,
                AppDataBase::class.java, "echo_database"
            )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                    }
                })
                //清空数据库中的数据
                .fallbackToDestructiveMigration()
                //迁移数据库数据
                //.addMigrations(MIGRATION_8_9)
                .build()
        }

        private var MIGRATION_8_9: Migration = object : Migration(8, 9) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE custom_audio_list ADD COLUMN number LONG DEFAULT 0"
                )
            }
        }
    }

}
