package com.san4o.just4fun.smsparser.app.repository

import com.san4o.just4fun.smsparser.app.SmsItem
import io.reactivex.Single

interface SmsDatasource {
    fun fetchSms() : Single<List<SmsItem>>
}