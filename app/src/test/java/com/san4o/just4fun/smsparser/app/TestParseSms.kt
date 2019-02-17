package com.san4o.just4fun.smsparser.app

import com.san4o.just4fun.smsparser.app.repository.TestDataSmsDataSource
import org.apache.commons.io.FileUtils
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.File
import java.util.*

class TestParseSms {

    private val smsFile: File
        get() {
            val file = File("src\\main\\assets\\sms_inbox.txt")
            return file
        }

    lateinit var parsedSms : List<SmsSourceItem>

   @Before
    fun setUp(){


       parsedSms = FileUtils.readLines(smsFile, "UTF-8")
           .map { TestDataSmsDataSource.parse(it) }
    }

    @Test
    fun testParse(){
        assertTrue(parsedSms.isNotEmpty())
    }
    @Test
    fun testDetectSmsType(){
        val groupBy = parsedSms.groupBy { SmsType.valueOfBody(it.content) }

        val unknownContents = groupBy.getOrDefault(SmsType.UNKNOWN, Collections.emptyList())

        unknownContents.forEach { println("${it.content}") }

println()
        groupBy.entries
            .forEach{ println("${it.key} : ${it.value.size}")}

        assertTrue(unknownContents.isEmpty())
    }
}