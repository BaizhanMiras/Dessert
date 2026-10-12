package com.example.dessert.utils


object TimeConvertor {

    fun floatToTime(float: Float): String {
        var seconds = 0
        var minutes = 0
        var hours = 0
        seconds = float.toInt()
        minutes = seconds / 60
        seconds %= 60
        hours = minutes / 60
        minutes %= 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}
