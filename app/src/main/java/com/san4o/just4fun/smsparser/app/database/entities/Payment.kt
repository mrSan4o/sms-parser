package com.san4o.just4fun.smsparser.app.database.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.san4o.just4fun.smsparser.app.data.PaymentType
import java.util.*

@Entity(tableName = "payment")
data class Payment(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val type: Int,
    val typeName: String,
    val typeDescription: String,
    val destination: String,
    val sum: Double,
    val balance: Double,
    val datetime: Long,

    val source: String

) {
    fun date(): Date = Date(datetime)

    fun typeKey(): PaymentType = PaymentType.values().first { it.id == type }
}