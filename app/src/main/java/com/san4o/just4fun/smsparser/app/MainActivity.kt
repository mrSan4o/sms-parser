package com.san4o.just4fun.smsparser.app

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.sms_list.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sms_list)

        smsList.adapter = SmsListAdapter()
    }

    class SmsListAdapter(val context: Context) : RecyclerView.Adapter<SmsViewHolder>() {

        private val items : List<SmsItem>  = ArrayList()

        override fun getItemCount(): Int {
            return items.size
        }



        override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): SmsViewHolder {
            return SmsViewHolder.create(context, viewGroup)
        }

        override fun onBindViewHolder(vh: SmsViewHolder, i: Int) {
            val item = items.get(i)


        }
    }

    class SmsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            fun create(context: Context, viewGroup: ViewGroup) : SmsViewHolder {
                return SmsViewHolder(LayoutInflater.from(context).inflate(R.layout.sms_list_item, viewGroup))
            }
        }
        private val dateView : TextView

        private val contentView: TextView

        init {
            dateView = itemView.findViewById(R.id.sms_date)
            contentView = itemView.findViewById(R.id.sms_item_content)
        }

        fun setDate(date: Date){
            dateView.text = date.format
        }
    }
}
