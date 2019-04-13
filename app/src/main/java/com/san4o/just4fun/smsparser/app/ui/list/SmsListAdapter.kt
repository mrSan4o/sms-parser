package com.san4o.just4fun.smsparser.app.ui.list

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.san4o.just4fun.smsparser.app.R
import com.san4o.just4fun.smsparser.app.SmsType
import com.san4o.just4fun.smsparser.app.databinding.PaymentListItemBinding
import com.san4o.just4fun.smsparser.app.utils.longDefaultFormat

class SmsListAdapter(
    private val context: Context,
    private val viewModel: SmsListViewModel
) : RecyclerView.Adapter<SmsListAdapter.SmsViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): SmsViewHolder {
        return SmsViewHolder.create(context, viewGroup)
    }

    override fun getItemCount(): Int {
        return viewModel.getSize()
    }

    override fun onBindViewHolder(vh: SmsViewHolder, i: Int) {
        val item = viewModel.getItem(i)

        vh.binding.model = PaymentListItemViewModel.create(item)
    }

    class SmsViewHolder(val binding: PaymentListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun create(context: Context, viewGroup: ViewGroup): SmsViewHolder {
                val binding: PaymentListItemBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.payment_list_item,
                    viewGroup,
                    false
                )
                return SmsViewHolder(binding)
            }
        }



    }
}