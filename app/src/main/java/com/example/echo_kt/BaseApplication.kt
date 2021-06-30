package com.example.echo_kt

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class BaseApplication: Application(){
    companion object{
        private lateinit var baseApplication: BaseApplication
        fun getContext():Context{
            return baseApplication
        }
    }
    override fun onCreate() {
        super.onCreate()
        baseApplication=this
    }
}