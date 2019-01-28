package com.san4o.just4fun.smsparser.app.repository.tools

interface SingleCallback<T> {
    fun onSuccess(data : T)
    fun onError(e :Throwable)
}