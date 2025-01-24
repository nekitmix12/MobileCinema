package com.example.mobilecinema.data

import com.example.mobilecinema.di.MainContext

class StringHelper {
    private val quotes = """(".*?"|'.*?')""".toRegex()
    fun getString(resource: Int, vararg args: Any): String =
        MainContext.provideInstance().provideContext().getString(resource, args)

    fun removeTextInsideQuotes(input: String): String = input.replace(quotes, "")

    fun getTextInsideQuotes(input: String): String? = quotes.find(input)?.toString()
}