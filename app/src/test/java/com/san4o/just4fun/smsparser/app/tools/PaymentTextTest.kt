package com.san4o.just4fun.smsparser.app.tools

import com.san4o.just4fun.smsparser.app.data.PaymentType
import org.apache.commons.io.FileUtils
import org.junit.Assert.*
import org.junit.Test
import java.io.File
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

class PaymentTextTest {

    @Test
    fun testParse() {

        val items = CardSmsParseItem.parse(readSmsParseItem())
        items.forEach { println(it.text) }
    }

    @Test
    fun testParse1() {
        assertParsePayment(
            "16:34 Покупка 3198р IKEA Баланс: 89600.65р",
            PaymentType.OUTFLOW, 3198.0, 89600.65, "IKEA", TimeText("16:34")
        )
        assertParsePayment(
            "10:47 списание 25000р Баланс: 64600.65р",
            PaymentType.OUTFLOW, 25000.0, 64600.65, "", TimeText("10:47")
        )
        assertParsePayment(
            "13.02.19 мобильный банк за 13.02-12.03 60р Баланс: 93398.65р",
            PaymentType.OUTFLOW, 60.0, 93398.65, "", DateText("13.02.19")
        )
        assertParsePayment(
            "14:34 Отмена покупки 10р Баланс: 95803.02р",
            PaymentType.INFLOW, 10.0, 95803.02, "", TimeText("14:34")
        )
        assertParsePayment(
            "13:06 зачисление зарплаты 250285р Баланс: 251325.13р",
            PaymentType.INFLOW, 250285.0, 251325.13, "", TimeText("13:06")
        )
        assertParsePayment(
            "22:15 зачисление 2000р со вклада Баланс: 4450.03р",
            PaymentType.INFLOW, 2000.0, 4450.03, "со вклада", TimeText("22:15")
        )
        assertParsePayment(
            "12:21 Оплата 200р MEGAFON Баланс: 6300.93р",
            PaymentType.OUTFLOW, 200.0, 6300.93, "MEGAFON", TimeText("12:21")
        )
        assertParsePayment(
            "16:45 Выдача 15000р ATM 60015819 Баланс: 2484.37р",
            PaymentType.OUTFLOW, 15000.0, 2484.37, "ATM 60015819", TimeText("16:457")
        )
    }

    private fun assertParsePayment(
        text: String,
        paymentType: PaymentType,
        sum: Double,
        balance: Double,
        destination: String,
        formatDate: ForamtDate
        ) {

        val date = Date()
        val parse = PaymentItem.parse(PaymentText(text), date)

        assertEquals(text, paymentType, parse.type)
        assertEquals(text, sum, parse.sum, 0.01)
        assertEquals(text, balance, parse.balance, 0.01)
        assertEquals(text, destination, parse.destination)
        assertEqualsDateUntilMinute(text, formatDate.toDate(), parse.date)

        println("$text - OK")
    }


    private fun assertEqualsDateUntilMinute(message: String, date1: Date, date2: Date) {
        val calendarExpected = Calendar.getInstance().apply { time = date1 }
        val calendarActual = Calendar.getInstance().apply { time = date2 }

        cutUntilMinute(calendarExpected)
        cutUntilMinute(calendarActual)

        assertEquals(message, calendarExpected, calendarExpected)
    }

    private fun cutUntilMinute(calendar: Calendar){
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
    }

    private fun readSmsParseItem(): List<SmsParseItem> {
        return SmsParseItem.parse(readLines())
            .sortedBy { it.time }
    }

    private fun readLines(): List<String> {
        val file = File("src\\main\\assets\\sms_inbox.txt")
        println(File("").absolutePath)
        assertTrue(file.exists())

        val readLines = FileUtils.readLines(file, Charset.forName("UTF-8"))
        println("read ${readLines.size} lines")
        return readLines ?: Collections.emptyList()
    }

    private class DateTimeText(val text: String) : ForamtDate {
        private val dateForamt = SimpleDateFormat("dd.MM.yy HH:mm")

        override fun toDate(): Date = dateForamt.parse(text)

    }
    private class DateText(val text: String) : ForamtDate {
        private val dateForamt = SimpleDateFormat("dd.MM.yy")

        override fun toDate(): Date = dateForamt.parse(text)

    }

    private class TimeText(val text: String): ForamtDate{
        private val timeForamt = SimpleDateFormat("HH:mm")

        override fun toDate(): Date = timeForamt.parse(text)


    }

    interface ForamtDate{
        fun toDate():Date
    }
}