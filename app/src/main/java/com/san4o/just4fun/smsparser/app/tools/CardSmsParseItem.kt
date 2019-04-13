package com.san4o.just4fun.smsparser.app.tools

import com.san4o.just4fun.smsparser.app.utils.*
import java.util.*

data class CardSmsParseItem(val card: String, val text: String, val date: Date, val source: SmsParseItem) {

    fun string(source: Boolean = false): String {
        var text = "${date.systemlongFormat()} : [$card] $text"
        if (source){
            text += " ${this.source.text}"
        }
        return text
    }

    companion object {

        val transferFormatRegex =
            Regex("перевод [0-9]+р. на карту получателя [А-Яа-я]+ [А-Яа-я]+ [А-Яа-я]. выполнен.")
        val balanceRegex = Regex("Баланс: ${Price.regex.pattern}")
        val cardRegex = Regex("^(VISA[0-9]{4})")

        fun isValid(text: String): Boolean = text.startsWith(cardRegex)

        fun from(item: SmsParseItem): CardSmsParseItem {
            val input = item.text
            val card = cardRegex.find(input)!!.value
            return CardSmsParseItem(
                card,
                input.replace(card, "").trim().removeIfStart(":").trim(),
                item.time.toDate(),
                item
            )
        }

//        fun parse(parseItems: List<SmsParseItem>): List<CardSmsParseItem> {
//            return parseItems
//                .filter { isValid(it.text) }
//                .map { from(it) }
//
//        }
        fun parse(parseItems: List<SmsParseItem>): List<CardSmsParseItem> {
            val items = parseItems
                .filter { isValid(it.text) }
                .map { from(it) }
//                .filterNot {
//                    it.text.contains("пополнение Копилки на")
//                            || it.text.contains("услуга Копилка на")
//                }
            return items
                .map {
                    if (!it.text.contains(transferFormatRegex)) {
                        return@map it
                    }
                    val index = items.indexOf(it)
                    if (index == items.size - 1) {
                        return@map it
                    }
                    val nextItem = items[index + 1]
                    val nextText = nextItem.text
                    val balanceMatchResult = balanceRegex.find(nextText) ?: return@map it

                    val transformMatchResult = transferFormatRegex.find(it.text) ?: return@map it
                    val transformText = transformMatchResult.value
                    val cuttedtext = it.text.substringBeforeWithDelimeter(transformText)
                    return@map CardSmsParseItem(
                        it.card,
                        cuttedtext + " " + balanceMatchResult.value,
                        it.date,
                        SmsParseItem(
                            it.source.text + "\n" + nextText,
                            it.source.time
                        )
                    )
                }

        }

    }
}