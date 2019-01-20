package com.san4o.just4fun.smsparser.app

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.san4o.just4fun.smsparser.app.database.entities.Sms
import com.san4o.just4fun.smsparser.app.utils.longDefaultFormat
import java.util.*

class SmsListAdapter(val context: Context) : RecyclerView.Adapter<SmsListAdapter.SmsViewHolder>() {

    private val items: MutableList<Sms> =
        ArrayList()

    fun refreshItems(items: List<Sms>) {
        this.items.clear()
        this.items.addAll(items)

        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): SmsListAdapter.SmsViewHolder {
        return SmsListAdapter.SmsViewHolder.create(context, viewGroup)
    }

    override fun onBindViewHolder(vh: SmsListAdapter.SmsViewHolder, i: Int) {
        val item = items.get(i)

        vh.dateView.text = item.date().longDefaultFormat()
        vh.contentView.text = item.content
        vh.typeView.text = SmsType.valueOfBody(item.content).title.toUpperCase()
    }

    class SmsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            fun create(context: Context, viewGroup: ViewGroup): SmsViewHolder {
                return SmsViewHolder(LayoutInflater.from(context).inflate(R.layout.sms_list_item, viewGroup, false))
            }
        }

        val dateView: TextView = itemView.findViewById(R.id.sms_date)
        val contentView: TextView = itemView.findViewById(R.id.sms_item_content)
        val typeView: TextView = itemView.findViewById(R.id.sms_type)

    }
}