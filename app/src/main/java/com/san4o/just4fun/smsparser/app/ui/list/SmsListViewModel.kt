package com.san4o.just4fun.smsparser.app.ui.list

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableDouble
import android.databinding.ObservableField
import com.san4o.just4fun.smsparser.app.data.PaymentType
import com.san4o.just4fun.smsparser.app.repository.ReadSmsCallback
import com.san4o.just4fun.smsparser.app.repository.SmsListRepository
import com.san4o.just4fun.smsparser.app.repository.tools.SingleCallback
import com.san4o.just4fun.smsparser.app.tools.PaymentItem
import com.san4o.just4fun.smsparser.app.utils.ListAdapter
import com.san4o.just4fun.smsparser.app.utils.SingleVoidLiveEvent
import com.san4o.just4fun.smsparser.app.utils.longDefaultFormat
import com.san4o.just4fun.smsparser.app.utils.shortDefaultFormat
import timber.log.Timber
import java.util.*

class SmsListViewModel(private val repository: SmsListRepository) : ViewModel(), ListAdapter<PaymentItem> {

    private val items = ArrayList<PaymentItem>()
    val errorLoadingNotification = SingleVoidLiveEvent()
    val noItemsNotification = SingleVoidLiveEvent()
    val refreshItemsViewCommand = SingleVoidLiveEvent()
    val showLoading = ObservableBoolean()
    val inflowSummary = ObservableDouble()
    val outflowSummary = ObservableDouble()
    val balanceSummary = ObservableDouble()
    val periodText = ObservableField<String>("")

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
        repository.readSms(object : ReadSmsCallback {

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

        val maxByDatePayment = data.maxBy { it.date }

        val date1 = data.minBy { it.date }?.date
        val date2 = maxByDatePayment?.date
        periodText.set(buildPeriodText(date1, date2))

        val inflow = data.filter { it.type == PaymentType.INFLOW }.sumByDouble { it.sum }
        val outflow = data.filter { it.type == PaymentType.OUTFLOW }.sumByDouble { it.sum }
        inflowSummary.set(inflow)
        outflowSummary.set(outflow)

        balanceSummary.set(maxByDatePayment?.balance ?: 0.0)

        items.clear()
        items.addAll(data)
        items.sortByDescending { it.date }
        refreshItemsViewCommand.call()
    }

    private fun buildPeriodText(date1: Date?, date2: Date?) =
        "${date1?.shortDefaultFormat()} - ${date2?.shortDefaultFormat()}"

    override fun getItem(i: Int): PaymentItem = items[i]

    override fun getSize(): Int = items.size
}