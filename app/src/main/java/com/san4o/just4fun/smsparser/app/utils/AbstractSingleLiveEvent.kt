package com.san4o.just4fun.smsparser.app.utils

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.util.Log
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

open class AbstractSingleLiveEvent<T> {

    private val pending = AtomicBoolean()
    private val liveData:MutableLiveData<T> = MutableLiveData()

    fun observe(owner : LifecycleOwner, observer : Observer<T>){

        if (liveData.hasActiveObservers()){
            Timber.w("LiveData must have only one observer. Found active observers")
        }

        liveData.observe(owner, object : Observer<T> {
            override fun onChanged(t: T?) {
                if (pending.compareAndSet(true, false)){
                    observer.onChanged(t)
                }
            }
        })
    }

    protected fun set(t:T?){
        liveData.value = t
        pending.set(true)
    }
    protected  fun post(t:T?){
        liveData.postValue(t)
        pending.set(true)
    }
}