package com.example.mobilecinema.domain.use_case.auth_use_case

class DataConverter {
    fun convertDateFormat(date: String): String {

        var newString = ""

        for (i in date.indices) {
            if (i < 4)
                newString += date[i + 6]
            else if (i == 4)
                newString += "-"
            else if (i < 7)
                newString += date[i - 3]
            else if (i == 7)
                newString += "-"
            else
                newString += date[i - 8]
        }

        return newString + "T00:00:00.000Z"
    }
}