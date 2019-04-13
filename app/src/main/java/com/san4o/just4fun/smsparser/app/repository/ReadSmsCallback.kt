package com.san4o.just4fun.smsparser.app.repository

import com.san4o.just4fun.smsparser.app.repository.tools.SingleCallback
import com.san4o.just4fun.smsparser.app.tools.PaymentItem

interface ReadSmsCallback :SingleCallback<List<PaymentItem>> {
    fun onNoItemsFound()
}