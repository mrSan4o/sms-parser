package com.san4o.just4fun.smsparser.app.tools

import com.san4o.just4fun.smsparser.app.data.PaymentType
import java.text.SimpleDateFormat
import java.util.*

data class Payment(
    val type: PaymentType,
    val card: String,
    val sum: Double,
    val date: Date,
    val shop: String,
    val balance: Double,
    val source: CardSmsParseItem
) {
    companion object {
        val textDateFormat = SimpleDateFormat("dd.MM.yy")
        val textTimeFormat = SimpleDateFormat("HH:mm")

        val dateRegex = Regex("([0-9]{2}.[0-9]{2}.[0-9]{2,4})")
        val timeRegex = Regex("([0-9]{1,2}):([0-9]{2})")
        val longDateInTextRegex = Regex("(${dateRegex.pattern})[ ](${timeRegex.pattern})")
        val priceRegex = Regex("[0-9]+[.]?([0-9]{0,2})?р")

        fun parse(cardItems: List<CardSmsParseItem>): List<Payment> {
            return cardItems.map { parse(it) }
        }

        fun parse(cardItem: CardSmsParseItem): Payment {
            val card = cardItem.card
            val text = cardItem.text
            var  date = cardItem.date

            val type = PaymentType.UNKNOWN
            val sum = 0.0
            val balance = 0.0
            val shop = "shop"

            val tokens = text.split(" ")
            if (tokens[0].matches(dateRegex)){
                val calendarDate = CalendarDate(date)
                val tokenDate = textDateFormat.parse(tokens[0])
                if (tokenDate!=null) {
                    calendarDate.setDate(tokenDate)
                    if (tokens[1].matches(timeRegex)) {
                        val time = textDateFormat.parse(tokens[1])
                        if (time!=null) {
                            calendarDate.setTime(time)
                        }
                    }
                    date = calendarDate.getDate()
                }
            }


            return Payment(type, card, sum, date, shop, balance, cardItem)
        }
    }
}