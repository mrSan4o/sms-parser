package com.san4o.just4fun.smsparser.app.ui.list

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import com.san4o.just4fun.smsparser.app.repository.ReadSmsCallback
import com.san4o.just4fun.smsparser.app.repository.SmsListRepository
import com.san4o.just4fun.smsparser.app.repository.tools.SingleCallback
import com.san4o.just4fun.smsparser.app.tools.PaymentItem
import com.san4o.just4fun.smsparser.app.utils.ListAdapter
import com.san4o.just4fun.smsparser.app.utils.SingleVoidLiveEvent
import timber.log.Timber

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
        Timber.d("findAllSavedSms")
        showLoading.set(true)
        repository.fetchSms(object : SingleCallback<List<PaymentItem>> {
            override fun onSuccess(data: List<PaymentItem>) {
                refreshItems(data)
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
                refreshItems(data)
                showLoading.set(false)
            }

            override fun onError(e: Throwable) {
                errorLoadingNotification.call()
                showLoading.set(false)
            }
        })
    }

    private fun refreshItems(data: List<PaymentItem>) {
        Timber.d("refreshItems %s", data.size)

        items.addAll(data)
        items.sortByDescending { it.date }
        refreshItemsViewCommand.call()
    }


    override fun getItem(i: Int): PaymentItem  = items[i]

    override fun getSize(): Int  = items.size
}