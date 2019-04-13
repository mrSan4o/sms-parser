package com.san4o.just4fun.smsparser.app.repository

import com.san4o.just4fun.smsparser.app.SmsSourceItem
import com.san4o.just4fun.smsparser.app.database.dao.PaymentDao
import com.san4o.just4fun.smsparser.app.database.dao.SmsDao
import com.san4o.just4fun.smsparser.app.database.entities.Payment
import com.san4o.just4fun.smsparser.app.database.entities.Sms
import com.san4o.just4fun.smsparser.app.repository.tools.SingleCallback
import com.san4o.just4fun.smsparser.app.tools.CardSmsParseItem
import com.san4o.just4fun.smsparser.app.tools.PaymentItem
import com.san4o.just4fun.smsparser.app.tools.PaymentText
import com.san4o.just4fun.smsparser.app.tools.SmsParseItem
import com.san4o.just4fun.smsparser.app.utils.insertNew
import io.reactivex.disposables.Disposable
import timber.log.Timber


class SmsListRepositoryImpl
constructor(
    private val phoneDatasourse: SmsDatasource,
    private val smsDao: SmsDao,
    private val paymentDao: PaymentDao
)
    : SmsListRepository, AbstractRepository() {


    override fun fetchSms(callback: SingleCallback<List<PaymentItem>>): Disposable =
        disposable(
            "fetchSms",
            paymentDao.findAllFlow().map { it.map { item -> PaymentItem.fromEntity(item) } },
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

        items.forEach { smsDao.insertNew(Sms(null, it.content, it.date.time)) }

        val paymentItems = CardSmsParseItem.parse(items.map { SmsParseItem(it.content, it.date.time) })
            .map { PaymentItem.parse(PaymentText(it.text), it.date) }

        paymentItems.forEach { insertPaymentItem(it) }

        return paymentItems
    }

    private fun insertPaymentItem(paymentItem: PaymentItem) {
        paymentDao.insert(
            Payment(
                null,
                paymentItem.type.id,
                paymentItem.typeName,
                paymentItem.typeDescription,
                paymentItem.destination,
                paymentItem.sum,
                paymentItem.balance,
                paymentItem.date.time,

                paymentItem.source
            )
        )
    }

}