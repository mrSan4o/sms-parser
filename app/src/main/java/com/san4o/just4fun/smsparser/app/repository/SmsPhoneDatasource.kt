package com.san4o.just4fun.smsparser.app.repository

import android.content.Context
import android.net.Uri
import com.san4o.just4fun.smsparser.app.SmsItem
import com.san4o.just4fun.smsparser.app.SmsType
import com.san4o.just4fun.smsparser.app.database.dao.SmsDao
import com.san4o.just4fun.smsparser.app.utils.getStringByName
import com.san4o.just4fun.smsparser.app.utils.longDefaultFormat
import io.reactivex.Single
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SmsPhoneDatasource @Inject constructor(): SmsDatasource {

    @Inject
    lateinit var context : Context



    override fun fetchSms(): Single<List<SmsItem>> = Single.fromCallable{ readSberbankSms()}

    private fun readSberbankSms(): List<SmsItem> {
        val items: MutableList<SmsItem> = ArrayList()

        val uri = Uri.parse("content://sms")
        val messageCursor =
            context.contentResolver.query(
                uri,
                arrayOf("date", "body"),
//                arrayOf("_id", "thread_id", "address", "date", "type", "body"),
                "address = ?",
                arrayOf("900"),
                "date DESC"
            )


        if (messageCursor != null && messageCursor.count > 0) {
            while (messageCursor.moveToNext()) {
                val dateTimeString = messageCursor.getStringByName("date")
//                val address = messageCursor.getStringByName("address")
//                val type = messageCursor.getStringByName("type")
                val body = messageCursor.getStringByName("body")
                val date = Date(dateTimeString.toLong())
                var type = SmsType.UNKNOWN
                try {
                    type = SmsType.valueOfBody(body)
                } catch (e: Exception) {
                    Timber.d(e, "Error ${date.longDefaultFormat()} : $body")
                    continue
                }




                Timber.d( ">>> [${type.title}] ${date.longDefaultFormat()} : $body")
                items.add(SmsItem(body, date))
            }
//            items.sortByDescending { it.date }


        } else {
            Timber.i( "EMPTY RESULT")
        }
        Timber.i("result : ${messageCursor.count}")

        return items
    }

}