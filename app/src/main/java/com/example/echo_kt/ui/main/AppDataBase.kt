package com.example.echo_kt.ui.main

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.echo_kt.BaseApplication
import com.example.echo_kt.data.HistoryAudioBeanDao

@Database( entities = [HistoryAudioBean::class],version = 1 ,exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun historyAudioDao():HistoryAudioBeanDao

    companion object{
        @Volatile
        private var instance:AppDataBase?=null

        fun getInstance():AppDataBase{
            return instance?: synchronized(this){
                instance?:buildDataBase(BaseApplication.getContext()).also { instance=it }
            }
        }

        private fun buildDataBase(context : Context): AppDataBase {
            return Room.
            databaseBuilder(context,AppDataBase::class.java,"echo_database")
                .addCallback(object : RoomDatabase.Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                    }
                }).build()
        }
    }
}
