package com.san4o.just4fun.smsparser.app.repository

import com.san4o.just4fun.smsparser.app.repository.tools.SingleCallback
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import timber.log.Timber

open class AbstractRepository {
    protected fun <T> disposable(name: String, single: Single<T>, callback: SingleCallback<T>): Disposable {
        return single
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<T>() {
                override fun onSuccess(t: T) {
                    Timber.d("[$name] onSuccess")
                    callback.onSuccess(t)
                }


                override fun onError(e: Throwable) {
                    Timber.e(e, ">>> [$name] Error : ${e.message}")
                    callback.onError(e)
                }
            })
    }

    protected fun <T> disposable(name: String, flowable: Flowable<T>, callback: SingleCallback<T>): Disposable {
        return flowable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSubscriber<T>() {

                override fun onComplete() {
                }

                override fun onNext(t: T) {
                    callback.onSuccess(t)
                }


                override fun onError(e: Throwable) {
                    Timber.e(e, ">>> Error $name : ${e.message}")
                    callback.onError(e)
                }
            })
    }
}