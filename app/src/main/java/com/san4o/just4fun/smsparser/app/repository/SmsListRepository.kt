package com.san4o.just4fun.smsparser.app.repository

import com.san4o.just4fun.smsparser.app.SmsSourceItem
import com.san4o.just4fun.smsparser.app.repository.tools.SingleCallback
import io.reactivex.disposables.Disposable

interface SmsListRepository {
    fun fetchSms(callback: SingleCallback<List<SmsSourceItem>>) : Disposable

    fun readSms(callback: SingleCallback<List<SmsSourceItem>>) : Disposable
}