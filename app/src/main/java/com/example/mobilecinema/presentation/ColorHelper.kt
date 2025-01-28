package com.example.mobilecinema.presentation

import com.example.mobilecinema.R

class ColorHelper {
    val isOne = (R.color.isOne)
    val isSecond = (R.color.isTwo)
    val isThree = (R.color.isThree)
    val isFour = (R.color.isFour)
    val isFive = (R.color.isFive)
    val isSix = (R.color.isSix)
    val isSeven = (R.color.isSeven)
    val isEight = (R.color.isEight)
    val isNine = (R.color.isNine)
    val isTen = (R.color.isNine)

    companion object {
        fun getColor(colorRes: Int): Int {
            return when (colorRes) {
                1 -> ColorHelper().isOne
                2 -> ColorHelper().isSecond
                3 -> ColorHelper().isThree
                4 -> ColorHelper().isFour
                5 -> ColorHelper().isFive
                6 -> ColorHelper().isSix
                7 -> ColorHelper().isSeven
                8 -> ColorHelper().isEight
                9 -> ColorHelper().isNine
                10 -> ColorHelper().isTen
                else -> throw (Exception())
            }
        }
    }
}

