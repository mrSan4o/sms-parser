package com.san4o.just4fun.smsparser.app.ui.list

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import com.san4o.just4fun.smsparser.app.repository.ReadSmsCallback
import com.san4o.just4fun.smsparser.app.repository.SmsListRepository
import com.san4o.just4fun.smsparser.app.repository.tools.SingleCallback
import com.san4o.just4fun.smsparser.app.tools.PaymentItem
import com.san4o.just4fun.smsparser.app.utils.ListAdapter
import com.san4o.just4fun.smsparser.app.utils.SingleVoidLiveEvent

class SmsListViewModel(private val repository: SmsListRepository)
    : ViewModel(), ListAdapter<PaymentItem> {

    private val items = ArrayList<PaymentItem>()
    val errorLoadingNotification = SingleVoidLiveEvent()
    val noItemsNotification = SingleVoidLiveEvent()
    val refreshItemsViewCommand = SingleVoidLiveEvent()
    val showLoading = ObservableBoolean()

    init {

        findAllSavedSms()
    }

    private fun findAllSavedSms() {
        showLoading.set(true)
        repository.fetchSms(object : SingleCallback<List<PaymentItem>> {
            override fun onSuccess(data: List<PaymentItem>) {
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
        repository.readSms(object : ReadSmsCallback{

            override fun onNoItemsFound() {
                showLoading.set(false)
            }

            override fun onSuccess(data: List<PaymentItem>) {
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


    override fun getItem(i: Int): PaymentItem  = items[i]

    override fun getSize(): Int  = items.size
}