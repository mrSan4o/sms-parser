package com.san4o.just4fun.smsparser.app.repository

import android.content.Context
import com.san4o.just4fun.smsparser.app.SmsItem
import io.reactivex.Single
import org.apache.commons.io.IOUtils
import timber.log.Timber
import java.lang.Exception
import java.lang.RuntimeException
import java.util.*
import javax.inject.Inject

class TestDataSmsDataSource @Inject constructor() : SmsDatasource {

    @Inject
    lateinit var context: Context

    override fun fetchSms(): Single<List<SmsItem>> {

        return Single.defer {

            return@defer Single.fromCallable { context.assets.open("list.txt") }
                .map { inputStream -> IOUtils.readLines(inputStream, "UTF-8") }
                .map { readLines -> readLines.mapNotNull { line -> parse(line) } }
        }
    }

    private fun parse(line: String): SmsItem {
        Timber.d("Parse : $line")
        try {
            return SmsItem(
                line.substringAfter("\"").substringBeforeLast("\""),
                Date(line.substringAfterLast(",").trim().toLong())
            )
        }catch (e : Exception){
            throw RuntimeException("Error parse : '$line'", e)

        }
    }
}