package com.mj.compose_clean_architecture.common.ktx

import android.os.Build
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun String.applyDateFormat(): String {
    val inputFormatter = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
    val date = inputFormatter.parse(this)
    val outputFormatter = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.ENGLISH)
    outputFormatter.timeZone = TimeZone.getTimeZone("Asia/Seoul")
    return outputFormatter.format(date)
}