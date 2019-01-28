package com.san4o.just4fun.smsparser.app.utils

class SingleVoidLiveEvent : AbstractSingleLiveEvent<Void>() {

    fun call() = set(null)


    fun callPost() = post(null)

}