package com.san4o.just4fun.smsparser.app.repository

import com.san4o.just4fun.smsparser.app.SmsSourceItem
import com.san4o.just4fun.smsparser.app.model.Payment
import com.san4o.just4fun.smsparser.app.model.Payment_
import com.san4o.just4fun.smsparser.app.model.Sms
import com.san4o.just4fun.smsparser.app.model.Sms_
import com.san4o.just4fun.smsparser.app.tools.PaymentItem
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import io.objectbox.kotlin.query
import io.objectbox.query.Query
import io.reactivex.Single

class SmsObjectBoxStorage(private val store: BoxStore) : SmsStorage {

    val paymentBox = store.boxFor<Payment>()
    val smsBox = store.boxFor<Sms>()

    val findAllQuery = paymentBox.query {
        orderDesc(Payment_.date)
    }

    override fun findAll(): Single<List<PaymentItem>> {
        return Single.fromCallable { findAllQuery.find().map { PaymentItem.fromModel(it) } }
    }

    override fun updateSms(items: List<SmsSourceItem>) {
        val insertSmses = items.filter { smsQuery(it).count() == 0L }
            .map { Sms(content = it.content, date = it.date) }

        smsBox.put(insertSmses)
    }

    override fun updatePayments(items: List<PaymentItem>) {
        val insertPayments = items.filter { paymentQuery(it).count() == 0L }
            .map { mapPayment(it) }

        paymentBox.put(insertPayments)
    }

    private fun mapPayment(it: PaymentItem): Payment {
        return Payment(
            date = it.date,
            type = it.type.id,
            typeName = it.typeName,
            typeDescription = it.typeDescription,
            destination = it.destination,
            sum = it.sum,
            balance = it.balance,
            source = it.source
        )
    }

    private fun smsQuery(item: SmsSourceItem): Query<Sms> {
        val query = smsBox.query {
            equal(Sms_.date, item.date)
            equal(Sms_.content, item.content)
        }
        return query
    }

    private fun paymentQuery(item: PaymentItem): Query<Payment> {
        val query = paymentBox.query {
            equal(Payment_.date, item.date)
            equal(Payment_.sum, item.sum, 0.1)
            equal(Payment_.destination, item.destination)
        }
        return query
    }
}