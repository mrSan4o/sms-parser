package com.san4o.just4fun.smsparser.app.utils

interface ListAdapter<T> {
    fun getItem(i: Int): T
    fun getSize(): Int
}