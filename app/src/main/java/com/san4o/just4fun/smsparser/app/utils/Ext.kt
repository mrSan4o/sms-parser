package com.san4o.just4fun.smsparser.app.utils

import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.util.Log
import android.widget.Toast
import com.san4o.just4fun.smsparser.app.database.dao.SmsDao
import com.san4o.just4fun.smsparser.app.database.entities.Sms
import io.reactivex.Completable
import java.text.SimpleDateFormat
import java.util.*

private val longFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")

fun Date.longDefaultFormat(): String {
    return longFormat.format(this)
}

fun Cursor.getStringByName(name: String): String {
    return this.getString(this.getColumnIndex(name))
}

fun Long.toDate():Date{
    return Date(this)
}

fun Context.showToastShort(message : String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun SmsDao.insertNew(sms: Sms): Completable {
    return this.countByDate(sms.datetime)
        .doOnSuccess{ Log.d(this.javaClass.simpleName, "${sms.date()} : $it")}
        .flatMapCompletable { count ->
            if (count == 0) {
                return@flatMapCompletable Completable.fromAction { this.insert(sms) }
            }
            return@flatMapCompletable Completable.complete()
        }
}