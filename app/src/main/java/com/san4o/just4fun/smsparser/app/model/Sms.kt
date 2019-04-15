package com.san4o.just4fun.smsparser.app.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.util.*

@Entity
data class Sms(
    @Id var id: Long = 0,
    val content: String,
    val date: Date
)