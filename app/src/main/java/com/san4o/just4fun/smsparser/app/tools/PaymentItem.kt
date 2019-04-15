package com.san4o.just4fun.smsparser.app.tools

import com.san4o.just4fun.smsparser.app.data.PaymentType
import com.san4o.just4fun.smsparser.app.database.entities.Payment
import java.util.*

class PaymentItem(
    val type: PaymentType,
    val typeName: String,
    val typeDescription: String,
    val sum: Double,
    val balance: Double,
    val destination: String,
    val date: Date,
    val source: String
) {
    companion object {
        fun fromEntity(payment: Payment): PaymentItem{
            return PaymentItem(
                payment.typeKey(),
                payment.typeName,
                payment.typeDescription,
                payment.sum,
                payment.balance,
                payment.destination,
                payment.date(),
                payment.source
            )
        }
        fun fromModel(payment: com.san4o.just4fun.smsparser.app.model.Payment): PaymentItem {
            return PaymentItem(
                payment.typeKey(),
                payment.typeName,
                payment.typeDescription,
                payment.sum,
                payment.balance,
                payment.destination,
                payment.date,
                payment.source
            )
        }

        fun parse(paymentText: PaymentText, date: Date) : PaymentItem {
            val calendarDate = CalendarDate(date)
            paymentText.parseDate(calendarDate)


            val balance = paymentText.parseBalance()

            val parseFlowType = paymentText.parseFlowType()

            val sum = paymentText.parseSum()

            val destination = paymentText.parseDestination()

            val type = parseFlowType.type
            val typeName = parseFlowType.name
            val typeDescription = parseFlowType.description


            return PaymentItem(type, typeName, typeDescription, sum, balance, destination, calendarDate.getDate(), paymentText.text)
        }

    }
}