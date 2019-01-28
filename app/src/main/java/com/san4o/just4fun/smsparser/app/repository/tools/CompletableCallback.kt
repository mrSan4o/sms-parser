package com.san4o.just4fun.smsparser.app.repository.tools

interface CompletableCallback {
    fun onSuccess()
    fun onError(e :Throwable)
}