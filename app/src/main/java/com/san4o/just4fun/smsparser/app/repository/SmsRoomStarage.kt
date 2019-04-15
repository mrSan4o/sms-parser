package com.san4o.just4fun.smsparser.app.repository

import com.san4o.just4fun.smsparser.app.SmsSourceItem
import com.san4o.just4fun.smsparser.app.database.dao.PaymentDao
import com.san4o.just4fun.smsparser.app.database.dao.SmsDao
import com.san4o.just4fun.smsparser.app.database.entities.Payment
import com.san4o.just4fun.smsparser.app.database.entities.Sms
import com.san4o.just4fun.smsparser.app.tools.PaymentItem
import com.san4o.just4fun.smsparser.app.utils.insertNew
import io.reactivex.Single

class SmsRoomStarage(
    private val smsDao: SmsDao,
    private val paymentDao: PaymentDao
) : SmsStorage {


    override fun findAll(): Single<List<PaymentItem>> =
            paymentDao.findAll().map { list -> list.map { PaymentItem.fromEntity(it) } }

    override fun updateSms(items: List<SmsSourceItem>){
        items.forEach{smsDao.insertNew(Sms(null, it.content, it.date.time))}
    }

    override fun updatePayments(items: List<PaymentItem>){
        items.forEach{
            paymentDao.insert(
                Payment(
                    null,
                    it.type.id,
                    it.typeName,
                    it.typeDescription,
                    it.destination,
                    it.sum,
                    it.balance,
                    it.date.time,

                    it.source
                )
            )
        }
    }
}