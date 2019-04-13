package com.san4o.just4fun.smsparser.app.repository

import android.content.Context
import com.san4o.just4fun.smsparser.app.SmsSourceItem
import io.reactivex.Single
import org.apache.commons.io.IOUtils
import timber.log.Timber
import java.util.*

class TestDataSmsDataSource(private val context: Context) : SmsDatasource {

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



    override fun fetchSms(): Single<List<SmsSourceItem>> {

        return Single.defer {

            return@defer Single.fromCallable { context.assets.open("sms_inbox.txt") }
                .map { inputStream -> IOUtils.readLines(inputStream, "UTF-8") }
                .doOnSuccess{ Timber.d("read %s lines", it.size)}
                .map { readLines -> readLines.mapNotNull { line -> parse(line) } }
        }
    }

}