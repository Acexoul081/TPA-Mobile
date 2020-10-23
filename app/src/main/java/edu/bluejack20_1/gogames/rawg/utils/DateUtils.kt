package edu.bluejack20_1.gogames.rawg.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.currentFormated():String{
    val current = Calendar.getInstance().time
    return current.formatedDate(current)
}

fun Date.formatedDate(date: Date):String{
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    return formatter.format(date)
}