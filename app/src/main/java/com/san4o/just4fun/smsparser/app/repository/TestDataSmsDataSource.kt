package com.san4o.just4fun.smsparser.app.repository

import android.content.Context
import com.san4o.just4fun.smsparser.app.SmsSourceItem
import io.reactivex.Single
import org.apache.commons.io.IOUtils
import timber.log.Timber
import java.lang.Exception
import java.lang.RuntimeException
import java.util.*
import javax.inject.Inject

class TestDataSmsDataSource @Inject constructor() : SmsDatasource {

    companion object {
        fun parse(line: String): SmsSourceItem {
            Timber.d("Parse : $line")
            try {
                return SmsSourceItem(
                    line.substringAfter("\"").substringBeforeLast("\""),
                    Date(line.substringAfterLast(",").trim().toLong())
                )
            }catch (e : Exception){
                throw RuntimeException("Error parse : '$line'", e)

            }
        }

    }

    @Inject
    lateinit var context: Context

    override fun fetchSms(): Single<List<SmsSourceItem>> {

        return Single.defer {

            return@defer Single.fromCallable { context.assets.open("sms_inbox.txt") }
                .map { inputStream -> IOUtils.readLines(inputStream, "UTF-8") }
                .map { readLines -> readLines.mapNotNull { line -> parse(line) } }
        }
    }

}