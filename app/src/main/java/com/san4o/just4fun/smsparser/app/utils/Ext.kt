package com.san4o.just4fun.smsparser.app.utils

import android.database.Cursor
import java.text.SimpleDateFormat
import java.util.*

private val longFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")

fun Date.longDefaultFormat(): String {
    return longFormat.format(this)
}

fun Cursor.getStringByName(name: String): String {
    return this.getString(this.getColumnIndex(name))
}