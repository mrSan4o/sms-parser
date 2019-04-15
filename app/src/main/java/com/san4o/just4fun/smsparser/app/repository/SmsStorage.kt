package com.san4o.just4fun.smsparser.app.repository

import com.san4o.just4fun.smsparser.app.SmsSourceItem
import com.san4o.just4fun.smsparser.app.tools.PaymentItem
import io.reactivex.Single

interface SmsStorage {
    fun findAll(): Single<List<PaymentItem>>
    fun updateSms(items: List<SmsSourceItem>)
    fun updatePayments(items: List<PaymentItem>)
}