package com.san4o.just4fun.smsparser.app.utils

class SingleLiveEvent<T> : AbstractSingleLiveEvent<T>() {

    fun call(t : T) = set(t)
    fun callPost(t : T) = post(t)
}