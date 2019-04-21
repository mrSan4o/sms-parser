package com.san4o.just4fun.smsparser.app.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
class Shop(
    @Id var id: Long = 0,
    val name: String,
    val title: String
) {

    companion object {
        fun newInstance(name: String): Shop = Shop(
            name = name,
            title = name
        )
    }
}