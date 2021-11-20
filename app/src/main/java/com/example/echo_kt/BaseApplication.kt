package com.example.echo_kt

import android.app.Application
import android.content.Context
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class BaseApplication: Application(){
    companion object{
        private lateinit var baseApplication: BaseApplication
        fun getContext():Context{
            Log.e("BaseApplication1", "onCreate:${System.currentTimeMillis()}")
            return baseApplication
        }
    }
    override fun onCreate() {
        super.onCreate()
        baseApplication=this
        Log.e("BaseApplication2", "onCreate:${System.currentTimeMillis()}")
    }
}