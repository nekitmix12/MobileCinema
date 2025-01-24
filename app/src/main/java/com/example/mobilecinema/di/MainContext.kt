package com.example.mobilecinema.di

import android.annotation.SuppressLint
import android.content.Context

class MainContext private constructor(val context: Context?) {

    fun provideContext() = context ?: throw IllegalStateException("Singleton is not initialized. Call init(context) first.")

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        var instance: MainContext? = null

        fun init(context: Context?){
          if (instance==null){
              synchronized(this){
                  if(instance==null){
                      instance = MainContext(context)
                  }
              }
          }
        }

        fun provideInstance(): MainContext {
            return instance ?: throw IllegalStateException("Singleton is not initialized. Call init(context) first.")
        }
    }

}