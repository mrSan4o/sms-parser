package com.san4o.just4fun.smsparser.app.ui.list

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean

import com.san4o.just4fun.smsparser.app.SmsItem
import com.san4o.just4fun.smsparser.app.repository.SmsListRepository
import com.san4o.just4fun.smsparser.app.repository.tools.SingleCallback
import com.san4o.just4fun.smsparser.app.utils.ListAdapter
import com.san4o.just4fun.smsparser.app.utils.SingleLiveEvent
import com.san4o.just4fun.smsparser.app.utils.SingleVoidLiveEvent
import javax.inject.Inject

class SmsListViewModel
@Inject constructor(private val repository: SmsListRepository)
    : ViewModel(), ListAdapter<SmsItem> {

    private val items = ArrayList<SmsItem>()
    val errorLoadingNotification = SingleVoidLiveEvent()
    val refreshItemsViewCommand = SingleVoidLiveEvent()
    val showLoading = ObservableBoolean()

    init {

        findAllSavedSms()
    }

    private fun findAllSavedSms() {
        showLoading.set(true)
        repository.fetchSms(object : SingleCallback<List<SmsItem>> {
            override fun onSuccess(data: List<SmsItem>) {
                items.addAll(data)
                refreshItemsViewCommand.call()
                showLoading.set(false)
            }

            override fun onError(e: Throwable) {
                errorLoadingNotification.call()
                showLoading.set(false)
            }


        })
    }

    fun readSms() {
        showLoading.set(true)
        repository.readSms(object : SingleCallback<List<SmsItem>>{
            override fun onSuccess(data: List<SmsItem>) {
                items.addAll(data)
                refreshItemsViewCommand.call()
                showLoading.set(false)
            }

            override fun onError(e: Throwable) {
                errorLoadingNotification.call()
                showLoading.set(false)
            }
        })
    }


    override fun getItem(i: Int): SmsItem  = items[i]

    override fun getSize(): Int  = items.size
}