package com.san4o.just4fun.smsparser.app.model

import com.san4o.just4fun.smsparser.app.data.PaymentType
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.util.*

@Entity
data class Payment(
    @Id var id: Long = 0,
    val type: Int,
    val typeName: String,
    val typeDescription: String,
    val destination: String,
    val sum: Double,
    val balance: Double,
    val date: Date,

    val source: String

) {
    fun typeKey(): PaymentType = PaymentType.values().first { it.id == type }
}