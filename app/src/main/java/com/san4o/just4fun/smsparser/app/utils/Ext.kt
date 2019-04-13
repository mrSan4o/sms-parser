package com.san4o.just4fun.smsparser.app.utils

import android.content.Context
import android.database.Cursor
import android.support.v4.app.Fragment
import android.widget.Toast
import com.san4o.just4fun.smsparser.app.database.dao.PaymentDao
import com.san4o.just4fun.smsparser.app.database.dao.SmsDao
import com.san4o.just4fun.smsparser.app.database.entities.Payment
import com.san4o.just4fun.smsparser.app.database.entities.Sms
import com.san4o.just4fun.smsparser.app.tools.Price
import io.reactivex.Completable
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

private val longFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
private val systemlongFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")

fun Date.longDefaultFormat(): String {
    return longFormat.format(this)
}

fun Date.systemlongFormat(): String {
    return systemlongFormat.format(this)
}

fun Cursor.getStringByName(name: String): String {
    val string = this.getString(this.getColumnIndex(name))
    return string ?: ""
}

fun Long.toDate(): Date {
    return Date(this)
}

fun Fragment.showToastShort(message: String) {
    this.context?.showToastShort(message)
}
fun Context.showToastShort(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun String.startsWith(vararg regexs: Regex): Boolean {
    return regexs.any { this.startsWith(it) }
}

fun String.isPrice(): Boolean {
    return Price.regex.matches(this)
}
fun String.toPrice(): Double {
    return Price.toPrice(this)
}
fun String.removeIfStart(string: String): String {
    if (this.startsWith(string)) {
        return this.substring(string.length)
    }
    return this
}

fun String.substringBeforeWithDelimeter(delimeter: String): String {

    val index = this.indexOf(delimeter)
    if (index == -1) {
        return this
    }
    return this.substring(0, index + delimeter.length)
}

fun String.startsWith(regex: Regex): Boolean {
    return Regex("^(${regex.pattern}).*").matches(this)
}

fun SmsDao.insertNew(sms: Sms): Completable {
    return this.countByDate(sms.datetime)
        .flatMapCompletable { count ->
            if (count == 0) {
                return@flatMapCompletable Completable.fromAction { this.insert(sms) }
                    .doOnComplete { Timber.d("      >>> insert  ${sms.date().longDefaultFormat()} : ${sms.content}") }
            }
            return@flatMapCompletable Completable.complete()
        }
}
fun PaymentDao.insertNew(payment: Payment): Completable {
    return this.countByDate(payment.datetime)
        .flatMapCompletable { count ->
            if (count == 0) {
                return@flatMapCompletable Completable.fromAction { this.insert(payment) }
                    .doOnComplete { Timber.d("insert  ${payment.date().longDefaultFormat()} : ${payment.typeKey()} ${payment.sum}") }
            }
            return@flatMapCompletable Completable.complete()
        }
}

