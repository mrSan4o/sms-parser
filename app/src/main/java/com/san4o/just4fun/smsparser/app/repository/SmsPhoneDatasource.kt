package com.san4o.just4fun.smsparser.app.repository

import android.content.Context
import android.provider.Telephony
import com.san4o.just4fun.smsparser.app.SmsSourceItem
import com.san4o.just4fun.smsparser.app.SmsType
import com.san4o.just4fun.smsparser.app.utils.getStringByName
import io.reactivex.Single
import org.apache.commons.io.FileUtils
import timber.log.Timber
import java.io.File
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SmsPhoneDatasource @Inject constructor(): SmsDatasource {

    @Inject
    lateinit var context : Context



    override fun fetchSms(): Single<List<SmsSourceItem>> = Single.fromCallable{ readSberbankSms()}

    private val readSmsFields = arrayOf(
        Telephony.Sms.Inbox.BODY,
        Telephony.Sms.Inbox.DATE,
        Telephony.Sms.Inbox.SUBJECT,
        Telephony.Sms.Inbox.ADDRESS,
        Telephony.Sms.Inbox.DEFAULT_SORT_ORDER,
        Telephony.Sms.Inbox.PERSON,
        Telephony.Sms.Inbox.READ,
        Telephony.Sms.Inbox.STATUS,
        Telephony.Sms.Inbox.TYPE
        )

    private fun readSberbankSms(): List<SmsSourceItem> {
        val items: MutableList<SmsSourceItem> = ArrayList()


        val cursor =
            context.contentResolver.query(
                Telephony.Sms.Inbox.CONTENT_URI,
                readSmsFields,
//                arrayOf("_id", "thread_id", "address", "date", "type", "body"),
                "${Telephony.Sms.Inbox.ADDRESS} = ? "+
                "AND ${Telephony.Sms.Inbox.TYPE} = ${Telephony.Sms.Inbox.MESSAGE_TYPE_INBOX}",
                arrayOf("900"),
                Telephony.Sms.Inbox.DATE+" ASC"
            )


        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {
                val dateTimeString = cursor.getStringByName(Telephony.Sms.Inbox.DATE)
                val address = cursor.getStringByName(Telephony.Sms.Inbox.ADDRESS)
                val subject = cursor.getStringByName(Telephony.Sms.Inbox.SUBJECT)
                val body = cursor.getStringByName(Telephony.Sms.Inbox.BODY)
                val date = Date(dateTimeString.toLong())
                var type = cursor.getStringByName(Telephony.Sms.Inbox.TYPE)

                var person = cursor.getStringByName(Telephony.Sms.Inbox.PERSON)
                var read = cursor.getStringByName(Telephony.Sms.Inbox.READ)
                var status = cursor.getStringByName(Telephony.Sms.Inbox.STATUS)


                items.add(SmsSourceItem(body, date))
            }
//            items.sortByDescending { it.date }

            val file = File("/storage/emulated/0/Download/sms/sms_inbox.txt")
            if (!file.exists() || file.delete()) {
                if (!file.parentFile.exists() && !file.parentFile.mkdir()) {
                    throw RuntimeException("WTF")
                }

                FileUtils.writeLines(file, items.map { "\"${it.content}\", ${it.date.time}" })
            }

        } else {
            Timber.i( "EMPTY RESULT")
        }
        Timber.i("result : ${cursor.count}")

        return items
    }

}