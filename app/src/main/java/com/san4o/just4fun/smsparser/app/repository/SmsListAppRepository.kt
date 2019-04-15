package com.san4o.just4fun.smsparser.app.repository

import com.san4o.just4fun.smsparser.app.SmsSourceItem
import com.san4o.just4fun.smsparser.app.repository.tools.SingleCallback
import com.san4o.just4fun.smsparser.app.tools.CardSmsParseItem
import com.san4o.just4fun.smsparser.app.tools.PaymentItem
import com.san4o.just4fun.smsparser.app.tools.PaymentText
import com.san4o.just4fun.smsparser.app.tools.SmsParseItem
import io.reactivex.disposables.Disposable
import timber.log.Timber


class SmsListAppRepository(
    private val phoneDatasourse: SmsDatasource,
    private val storage: SmsStorage
)
    : AbstractRepository(), SmsListRepository {


    override fun fetchSms(callback: SingleCallback<List<PaymentItem>>): Disposable =
        disposable(
            "fetchSms",
            storage.findAll(),
            callback
        )


    override fun readSms(callback: ReadSmsCallback): Disposable {
        val single = phoneDatasourse.fetchSms()
            .map { convertToPaymentItems(it, callback) }
            .doOnSuccess { Timber.d("Update ${it.size} items complete!") }


        return disposable("readSms", single, callback)
    }

    private fun convertToPaymentItems(
        items: List<SmsSourceItem>,
        callback: ReadSmsCallback
    ): List<PaymentItem> {
        Timber.d("fetch ${items.size}")
        if (items.isEmpty()) {
            callback.onNoItemsFound()
            return emptyList()
        }

        storage.updateSms(items)

        val paymentItems = CardSmsParseItem.parse(items.map { SmsParseItem(it.content, it.date.time) })
            .map { PaymentItem.parse(PaymentText(it.text), it.date) }

      storage.updatePayments(paymentItems)

        Timber.d("Parse %s payment items", paymentItems.size)

        return paymentItems
    }
}