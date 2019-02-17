package com.san4o.just4fun.smsparser.app.ui.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.san4o.just4fun.smsparser.app.R
import com.san4o.just4fun.smsparser.app.SmsType
import com.san4o.just4fun.smsparser.app.utils.longDefaultFormat

class SmsListAdapter(
    private val context: Context,
    private val viewModel: SmsListViewModel
) : RecyclerView.Adapter<SmsListAdapter.SmsViewHolder>() {

    override fun getItemCount(): Int {
        return viewModel.getSize()
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): SmsViewHolder {
        return SmsViewHolder.create(context, viewGroup)
    }

    override fun onBindViewHolder(vh: SmsViewHolder, i: Int) {
        val item = viewModel.getItem(i)

        vh.dateView.text = item.date.longDefaultFormat()
        vh.contentView.text = item.content
        vh.typeView.text = SmsType.valueOfBody(item.content).title.toUpperCase()
    }

    class SmsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            fun create(context: Context, viewGroup: ViewGroup): SmsViewHolder {
                return SmsViewHolder(
                    LayoutInflater.from(context).inflate(
                        R.layout.sms_list_item,
                        viewGroup,
                        false
                    )
                )
            }
        }

        val dateView: TextView = itemView.findViewById(R.id.sms_date)
        val contentView: TextView = itemView.findViewById(R.id.sms_item_content)
        val typeView: TextView = itemView.findViewById(R.id.sms_type)

    }
}