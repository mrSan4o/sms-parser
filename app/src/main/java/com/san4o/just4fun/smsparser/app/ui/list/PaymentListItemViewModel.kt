package com.san4o.just4fun.smsparser.app.ui.list

import com.san4o.just4fun.smsparser.app.tools.PaymentItem
import com.san4o.just4fun.smsparser.app.utils.longDefaultFormat

class PaymentListItemViewModel(
    val type : String,
    val typeName : String,
    val date : String,
    val sum : Double,
    val dest : String,
    val balance : Double
) {

    companion object {
        fun create(items : List<PaymentItem>) = items.map { create(it) }

        fun create(item : PaymentItem) : PaymentListItemViewModel{
            return PaymentListItemViewModel(
                item.type.title,
                item.typeName,
                item.date.longDefaultFormat(),
                item.sum,
                item.destination,
                item.balance
            )
        }
    }
}