package com.example.mobilecinema.data

import okhttp3.Response
import okhttp3.ResponseBody

interface ErrorsConverter {
    fun invokeMethod(responseBody: ResponseBody?,responseCode:Int):String
}