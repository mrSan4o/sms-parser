package com.san4o.just4fun.smsparser.app

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.sms_list.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sms_list)

        smsList.adapter = SmsListAdapter()
    }

    class SmsListAdapter : RecyclerView.Adapter<SmsViewHolder>() {



        override fun getItemCount(): Int {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SmsViewHolder {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onBindViewHolder(p0: SmsViewHolder, p1: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    class SmsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            fun create(context: Context, viewGroup: ViewGroup) : SmsViewHolder {
                LayoutInflater.from(context).inflate(R.)
            }
        }
    }
}
