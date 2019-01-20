package com.san4o.just4fun.smsparser.app.database.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "sms")
data class Sms(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val content: String,
    val datetime: Long


){
    fun date() : Date = Date(datetime)
}