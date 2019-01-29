package com.san4o.just4fun.smsparser.app.repository

import com.san4o.just4fun.smsparser.app.SmsItem
import com.san4o.just4fun.smsparser.app.database.dao.SmsDao
import com.san4o.just4fun.smsparser.app.database.entities.Sms
import com.san4o.just4fun.smsparser.app.repository.tools.SingleCallback
import com.san4o.just4fun.smsparser.app.utils.insertNew
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SmsListRepositoryImpl @Inject constructor() : SmsListRepository {

    @Inject
    lateinit var phoneDatasourse : SmsPhoneDatasource

    @Inject
    lateinit var smsDao : SmsDao

    override fun fetchSms(callback: SingleCallback<List<SmsItem>>) : Disposable =
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
                    .doOnComplete{ Timber.d("Update ${items.size} items complete!")}
                    .toSingle { items }
            }
        return disposable("readSms", single, callback)
    }

    private fun <T> disposable(name:String, single:Single<T>, callback: SingleCallback<T>) : Disposable {
        return single
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<T>(){
                override fun onSuccess(t: T) {
                    Timber.d("[$name] onSuccess")
                    callback.onSuccess(t)
                }


                override fun onError(e: Throwable)  {
                    Timber.e(e, ">>> [$name] Error : ${e.message}")
                    callback.onError(e)
                }
            })
    }
    private fun <T> disposable(name:String, flowable:Flowable<T>, callback: SingleCallback<T>) : Disposable {
        return flowable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSubscriber<T>(){

                override fun onComplete() {
                    Timber.d("[$name] onComplete")
                }

                override fun onNext(t: T) {
                    Timber.d("[$name] onNext")
                    callback.onSuccess(t)
                }


                override fun onError(e: Throwable)  {
                    Timber.e(e, ">>> Error $name : ${e.message}")
                    callback.onError(e)
                }
            })
    }

}