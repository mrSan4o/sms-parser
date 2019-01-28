package com.san4o.just4fun.smsparser.app.list

import android.arch.lifecycle.ViewModel

import com.san4o.just4fun.smsparser.app.SmsItem
import com.san4o.just4fun.smsparser.app.repository.SmsListRepository
import com.san4o.just4fun.smsparser.app.repository.tools.SingleCallback
import com.san4o.just4fun.smsparser.app.utils.ListAdapter
import com.san4o.just4fun.smsparser.app.utils.SingleVoidLiveEvent
import javax.inject.Inject

class SmsListViewModel @Inject constructor(
    private val repository: SmsListRepository
)
    : ViewModel(), ListAdapter<SmsItem> {

    private val items : MutableList<SmsItem> = ArrayList()
    val errorLoadingNotification = SingleVoidLiveEvent()

    init {

        fetchSms()
    }

    private fun fetchSms() {
        repository.fetchSms(object : SingleCallback<List<SmsItem>> {
            override fun onSuccess(data: List<SmsItem>) {
                items.addAll(data)
            }

            override fun onError(e: Throwable) {
                errorLoadingNotification.call()
            }


        })
    }

    fun readSms() {
        repository.readSms(object : SingleCallback<List<SmsItem>>{
            override fun onSuccess(data: List<SmsItem>) {

            }

            override fun onError(e: Throwable) {
                errorLoadingNotification.call()
            }
        })
    }


    override fun getItem(i: Int): SmsItem  = items[i]

    override fun getSize(): Int  = items.size
}