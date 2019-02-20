package com.dtikhonov.simpleserver.utils

import java.lang.StringBuilder


fun timeConversion(s: String): String {
    val t = s.split(':')
    val twelve = "12"
    if (t.size == 3) {
        val hours = t[0]
        val minutes = t[1]
        val secondsWith = t[2]
        val seconds = secondsWith.substring(0, 2)
        val dayMode = secondsWith.substring(2)
        val sb = StringBuilder()
        if (dayMode.equals("AM")) {
            if (hours.length == 1) {
                sb.append('0').append(hours)
            } else if (twelve.equals(hours)) {
                sb.append("00")
            } else {
                sb.append(hours)
            }
        } else {
            if (twelve.equals(hours)) {
                sb.append(twelve)
            }
            else {
                sb.append(Integer.parseInt(hours) + 12)
            }
        }
        return sb.append(":").append(minutes).append(":").append(seconds).toString()
    }
    else {
        return s
    }
}