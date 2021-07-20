package com.sergiom.madtivities.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Utils {

    fun parseDate(date: String): List<String> {
        val input = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")
        val output = SimpleDateFormat("dd-MM-yyyy hh:mm")

        var d: Date? = null
        try {
            d = input.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val formatted: String = output.format(d)

        return formatted.split(" ")
    }
}