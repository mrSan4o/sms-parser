package com.san4o.just4fun.smsparser.app.model

import com.san4o.just4fun.smsparser.app.data.PaymentType
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne
import java.util.*

@Entity
data class Payment(
    @Id var id: Long = 0,
    val type: Int,
    val typeName: String,
    val typeDescription: String,
    val sum: Double,
    val balance: Double,
    val date: Date,
    val source: String
) {
    constructor():this(
        type = 0,
        typeName = "",
        typeDescription = "",
        sum = 0.0,
        balance = 0.0,
        date = Date(),
        source = ""
    )

    lateinit var shop: ToOne<Shop>

    fun typeKey(): PaymentType = PaymentType.values().first { it.id == type }
}