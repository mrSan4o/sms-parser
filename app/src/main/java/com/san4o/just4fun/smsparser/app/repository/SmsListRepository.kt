package com.san4o.just4fun.smsparser.app.repository

import com.san4o.just4fun.smsparser.app.SmsSourceItem
import com.san4o.just4fun.smsparser.app.repository.tools.SingleCallback
import com.san4o.just4fun.smsparser.app.tools.PaymentItem
import io.reactivex.disposables.Disposable

interface SmsListRepository {
    fun fetchSms(callback: SingleCallback<List<PaymentItem>>) : Disposable

    fun readSms(callback: ReadSmsCallback) : Disposable
}