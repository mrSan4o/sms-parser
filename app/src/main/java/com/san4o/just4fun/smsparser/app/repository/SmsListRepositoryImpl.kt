package com.san4o.just4fun.smsparser.app.repository

import com.san4o.just4fun.smsparser.app.SmsItem
import com.san4o.just4fun.smsparser.app.database.dao.SmsDao
import com.san4o.just4fun.smsparser.app.database.entities.Sms
import com.san4o.just4fun.smsparser.app.repository.tools.SingleCallback
import com.san4o.just4fun.smsparser.app.utils.insertNew
import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import org.apache.commons.io.FileUtils
import timber.log.Timber
import java.io.File
import java.lang.RuntimeException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SmsListRepositoryImpl @Inject constructor() : SmsListRepository, AbstractRepository() {

    @Inject
    lateinit var phoneDatasourse: SmsDatasource

    @Inject
    lateinit var smsDao: SmsDao

    override fun fetchSms(callback: SingleCallback<List<SmsItem>>): Disposable =
        disposable(
            "fetchSms",
            smsDao.findAll().map { it.map { item -> SmsItem(item.content, item.date()) } },
            callback
        )


    override fun readSms(callback: SingleCallback<List<SmsItem>>): Disposable {
        val single = phoneDatasourse.fetchSms()
            .flatMap { items ->
                Timber.d("fetch ${items.size}")

                return@flatMap Completable.concat(
                    items.map { item -> smsDao.insertNew(Sms(null, item.content, item.date.time)) }
                )
                    .doOnComplete { Timber.d("Update ${items.size} items complete!") }
                    .toSingle { items }
            }
        return disposable("readSms", single, callback)
    }

}