package com.san4o.just4fun.smsparser.app.data

import com.san4o.just4fun.smsparser.app.tools.CardSmsParseItem
import com.san4o.just4fun.smsparser.app.tools.Payment
import com.san4o.just4fun.smsparser.app.tools.PaymentBuilder
import com.san4o.just4fun.smsparser.app.tools.SmsParseItem
import com.san4o.just4fun.smsparser.app.utils.longDefaultFormat
import com.san4o.just4fun.smsparser.app.utils.startsWith
import org.apache.commons.io.FileUtils
import org.junit.Assert.*
import org.junit.Test
import java.io.File
import java.nio.charset.Charset
import java.util.*

class SmsFlowTypeTest {


    @Test
    fun testRead() {
        val readLines = readLines()

        readLines
            .filter { it.contains("перевод 4202.50р OSB 1569 1628") }
            .forEach { println(it) }
    }

    @Test
    fun testParse() {

        val items = CardSmsParseItem.parse(readSmsParseItem())
        val texts = items
//            .filter { it.text.contains("Отмена покупки") }
//            .forEach { println("parse: ${it.text}") }

            .map { PaymentBuilder(it.text, it.date) }
            .filterNot { it.isExclude() }


        texts
//            .groupBy { it.tokens[0].toLowerCase() }

//            .map {
//                it.text
//                    .replace(cardRegex, "")
//                    .replace(longDateInTextRegex, "").trim()
//            }
//            .map {
//                println("convert $it")
//                convert(it)
//            }

//            .filter { it.startsWith("Александр Сергеевич") }
//            .filter { it.contains(transferRegex) }
//            .filter { convert(it).type == PaymentType.UNKNOWN }
//            .forEach {
//                println("${it.text} : '${convert(it)}'")
//            }

            .forEach {
                //                println()
//                println(it.tokens.joinToString(" ") + " : " + it.text)
//                println(it.key + " : " + it.value.size)
//                if (it.tokens[0].toLowerCase() == "выдача") {

//                printAround(texts, it, function)
                println("" +
                        "${it.type()}(${it.typeName()}), " +
                        "${it.price()} " +
                        "'${it.destination()}' " +
                        "balance:${it.balance()}, " +
                        "date:${it.date().longDefaultFormat()}, " +

                        ": ${it.sourceText()}")
//                }
            }
//            .map { convert(it) }
//            .forEach { println("${it.type} | ${it.sum} at ${it.date.longDefaultFormat()}") }
    }

    /**
    выдача : 90
    оплата : 49
    покупка : 460
    отмена : 4
    списание : 116
    зачисление : 63
    возврат : 7
    перевод : 5
    отказ : 8
    мобильный : 3
     * */

    private fun <T> printAround(list: List<T>, item: T, function: (T) -> String) {

        val index = list.indexOf(item)
        val previousIndex = index - 1
        if (previousIndex >= 0) {
            println(previousIndex.toString() + " : " + function(list[previousIndex]))
        }
        println(index.toString() + " : " + function(item))

        val nextIndex = index + 1
        if (list.size - 1 >= nextIndex) {
            println(nextIndex.toString() + " : " + function(list[nextIndex]))
        }
        println()

    }

    private fun readLines(): List<String> {
        val file = File("src\\main\\assets\\sms_inbox.txt")
        println(File("").absolutePath)
        assertTrue(file.exists())

        val readLines = FileUtils.readLines(file, Charset.forName("UTF-8"))
        println("read ${readLines.size} lines")
        return readLines ?: Collections.emptyList()
    }

    private fun readSmsParseItem(): List<SmsParseItem> {
        return SmsParseItem.parse(readLines())
            .sortedBy { it.time }
    }

    private fun cutShopName(text: String, priceText: String) =
        text.substringAfter("$priceText ").substringBefore(" Баланс: ")


    @Test
    fun testDecline() {
        assertTrue("ОТКАЗ (превышен лимит) выдача 86000р ATM 629979".matches(PaymentBuilder.declineRegex))
        assertTrue("ОТКАЗ (истек срок действия карты или срок указан неверно) покупка 343р KFC LENINGRADSKOE".matches(PaymentBuilder.declineRegex))
        assertTrue("ОТКАЗ покупка 252.50р GOSUSLUGI.RU".matches(PaymentBuilder.declineRegex))
        assertTrue("Отказ покупка 252.50р GOSUSLUGI.RU".matches(PaymentBuilder.declineRegex))
        assertTrue("отказ покупка 252.50р GOSUSLUGI.RU".matches(PaymentBuilder.declineRegex))
        assertFalse("1отказ покупка 252.50р GOSUSLUGI.RU".matches(PaymentBuilder.declineRegex))
    }
    @Test
    fun testServiceDecline() {
        assertTrue("услуга Копилка на \"Универсальный на 5 лет\" 5% от зачислений отключена.".matches(PaymentBuilder.serviceDeclineRegex))
        assertFalse("услуга Копилка на \"Универсальный на 5 лет\" 5% от зачислений отключена".matches(PaymentBuilder.serviceDeclineRegex))
        assertFalse("1услуга Копилка на \"Универсальный на 5 лет\" 5% от зачислений отключена.".matches(PaymentBuilder.serviceDeclineRegex))
    }
    @Test
    fun testPaymentText() {

        assertEquals(
            "списание 25000р Баланс: 64600.65р",
            PaymentBuilder("10:47 списание 25000р Баланс: 64600.65р", Date()).tokens.joinToString(" ")
        )

        assertEquals(
            "списание 25000р Баланс: 64600.65р",
            PaymentBuilder("12.12.2012 10:47 списание 25000р Баланс: 64600.65р", Date()).tokens.joinToString(" ")
        )

        assertEquals(
            "списание 25000р Баланс: 64600.65р",
            PaymentBuilder("12.12.2012 списание 25000р Баланс: 64600.65р", Date()).tokens.joinToString(" ")
        )

        assertEquals(
            "списание 25000р Баланс: 64600.65р",
            PaymentBuilder("списание 25000р Баланс: 64600.65р", Date()).tokens.joinToString(" ")
        )
    }

    @Test
    fun testTransferFormatRegex() {
        val text = ": перевод 275р. на карту получателя ЕЛЕНА АНАТОЛЬЕВНА К. выполнен. " +
                "Подробнее в выписке по карте http://sberbank.ru/sms/h2/ at 05.11.2017 06:14"
        assertTrue(text.contains(CardSmsParseItem.transferFormatRegex))
    }

    private val transferRegex = Regex("[А-Яа-я]+ [А-Яа-я]+ [А-Яа-я]. перевел[(]а[)] Вам [0-9]+[.][0-9]{2} [A-Z]{2,4}")
    @Test
    fun tesTransferRegex() {
        val text = "Сбербанк Онлайн. ЕКАТЕРИНА ОЛЕГОВНА Н. перевел(а) Вам 15000.00 RUB"
        assertTrue(text.contains(transferRegex))
        assertFalse("зачисление 15000р Баланс: 17484.37р".contains(transferRegex))
        println(transferRegex.find(text)?.value)
        val regexText = transferRegex.find(text)?.value ?: ""
        val split = regexText?.split(" ")
        val shop = split[0] + " " + split[1] + " " + split[2]
        val type = PaymentType.INFLOW
        val sum = textPrice(split[split.size - 2])

        assertEquals("ЕКАТЕРИНА ОЛЕГОВНА Н.", shop)
        assertEquals(15000.00, sum, 0.01)
    }

    private val mobBankRegex = Regex(" [м,М]обильн[а-я]{2,3} банк[а-я]{0,2} ")
    @Test
    fun testMobBankRegex() {
        assertTrue(
            "13.11.18 оплата Мобильного банка за 13/11/2018-12/12/2018 60р Баланс: 196607.48р".contains(
                mobBankRegex
            )
        )
        assertTrue("13.12.18 мобильный банк за 13.12-12.01 60р Баланс: 145174.01р".contains(mobBankRegex))
        assertTrue("13.12.18 мобильным банком за 13.12-12.01 60р Баланс: 145174.01р".contains(mobBankRegex))
        assertFalse("21.07.18 возврат покупки 137р LEROY MERLIN KHIMKI Баланс: 347102.58р".contains(mobBankRegex))
        assertFalse("12.01.19 покупка мобильные технологии 1405р. Баланс: 17551.12р".contains(mobBankRegex))
        assertFalse("12.01.19 покупка Сибирмобильный банк 1405р. Баланс: 17551.12р".contains(mobBankRegex))
    }

    @Test
    fun testIncoming() {
        assertTrue("VISA7065 20:54 зачисление 13000р Баланс: 22406.37р".contains(incomingRegex))
        assertFalse("VISA7065 20:54 зачисление зарплаты 13000р Баланс: 22406.37р".contains(incomingRegex))
    }

    private val priceRegex = Payment.priceRegex
    private val incomingRegex = Regex("зачисление ${priceRegex.pattern}")
    @Test
    fun testPrice() {
        val find = priceRegex.find("13.10.17 оплата Мобильного банка за 13/10/2017-12/11/2017 60р Баланс: 199892.32р")
        assertEquals("60р", find?.value)


        assertTrue("93458.65р".matches(priceRegex))
        assertTrue("12.65р".matches(priceRegex))
        assertTrue("12.50р".matches(priceRegex))
        assertTrue("12.05р".matches(priceRegex))
        assertTrue("12.1р".matches(priceRegex))
        assertTrue("1.65р".matches(priceRegex))
        assertTrue("0.65р".matches(priceRegex))
        assertTrue("60р".matches(priceRegex))

        val input = "Покупка 163.33р ALIEXPRESS Баланс: 93458.65р"
        val findAll = priceRegex.findAll(input)
        findAll.forEach { println("'${it.value}' : '${input.substringBefore(" " + it.value)}'") }
    }

    private val dateRegex = Payment.dateRegex
    private val timeRegex = Payment.timeRegex
    private val longDateInTextRegex = Payment.longDateInTextRegex

    @Test
    fun testOperation() {
        assertFalse(isStartWithDateOrTime("списание 25000р Баланс: 64600.65р at 17.02.2019 10:47"))
        assertTrue(isStartWithDateOrTime("10:47 списание 25000р Баланс: 64600.65р at 17.02.2019 10:47"))
        assertTrue(isStartWithDateOrTime("13.01.19 мобильный банк за 13.01-12.02 60р Баланс: 17288.74р at 14.01.2019 10:01"))
        assertTrue(isStartWithDateOrTime("12.10.18 14:17 Покупка 132р STOLOVAYA AVIATOR Баланс: 286534.96р at 12.10.2018 21:18"))

        assertTrue("09.08.18".matches(dateRegex))
        assertTrue("09.08.18 12:05".matches(longDateInTextRegex))
        assertTrue("12.12.18 12:35".matches(longDateInTextRegex))
        assertTrue("12:78".matches(timeRegex))
        assertFalse("dddd".matches(timeRegex))
        assertTrue("00:00".matches(timeRegex))
        assertTrue("2:01".matches(timeRegex))
        assertFalse("as:sa".matches(timeRegex))
        assertFalse("12:2".matches(timeRegex))
        assertTrue("13.01.19".matches(dateRegex))
        assertTrue("12.01.19".matches(dateRegex))
        assertFalse("Покупка".matches(dateRegex))
    }

    fun isStartWithDateOrTime(text: String): Boolean = text.startsWith(dateRegex, timeRegex)


    @Test
    fun testStart() {
        assertTrue("VISA7065 aaa".contains(CardSmsParseItem.cardRegex))
        assertTrue("VISA9129 aaa".contains(CardSmsParseItem.cardRegex))
        assertFalse("VISA aaa".contains(CardSmsParseItem.cardRegex))
        assertFalse("VISAaaa aaa".contains(CardSmsParseItem.cardRegex))
        assertFalse("asd VISA7065".contains(CardSmsParseItem.cardRegex))
        assertFalse("aaa".contains(CardSmsParseItem.cardRegex))
    }

    private fun textPrice(textPrice: String) =
        textPrice.substring(0, textPrice.length - 1).toDouble()


}