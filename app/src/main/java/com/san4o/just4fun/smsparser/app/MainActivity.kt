package com.san4o.just4fun.smsparser.app

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.san4o.just4fun.smsparser.app.utils.getStringByName
import com.san4o.just4fun.smsparser.app.utils.longDefaultFormat
import kotlinx.android.synthetic.main.sms_list.*
import java.util.*

class MainActivity : AppCompatActivity() {
    
    private val LOG_TAG : String = MainActivity::class.java.simpleName

    private val PERMISSION_REQUEST_CODE: Int = 111

    private lateinit var adapter: RecyclerView.Adapter<SmsViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sms_list)

        adapter = SmsListAdapter(this)
        smsList.adapter = adapter
        smsList.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()

        if (!isPermissionGranted(Manifest.permission.READ_SMS)){
            requestPermissions(Manifest.permission.READ_SMS, PERMISSION_REQUEST_CODE)
            return
        }

        refreshSms()
    }

    private fun refreshSms() {
        val uri = Uri.parse("content://sms")
        val messageCursor =
            contentResolver.query(
                uri,
                arrayOf("_id", "thread_id", "address", "date", "type", "body"),
                null,
                null,
                "date" + " ASC"
            )

        if (messageCursor != null && messageCursor.count > 0) {
            while (messageCursor.moveToNext()) {
                val date = messageCursor.getStringByName("date")
                val address = messageCursor.getStringByName("address")
                val type = messageCursor.getStringByName("type")
                val body = messageCursor.getStringByName("body")

                Log.i(LOG_TAG,"------------------------------------")
                Log.i(LOG_TAG,"type: $type")
                Log.i(LOG_TAG,"address: $address")
                Log.i(LOG_TAG,"date: $date")
                Log.i(LOG_TAG,"body: $body")
                Log.i(LOG_TAG,"------------------------------------")

            }
        }else{
            Log.i(LOG_TAG,"EMPTY RESULT")
        }
    }

    private fun requestPermissions(perm: String, code: Int) {
        requestPermissions(arrayOf(perm), code)
    }

    private fun isPermissionGranted(perm: String): Boolean {
        return checkSelfPermission(perm) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (PERMISSION_REQUEST_CODE == requestCode){
            if (grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                refreshSms()
            }
        }
    }

    class SmsListAdapter(val context: Context) : RecyclerView.Adapter<SmsViewHolder>() {

        private val items: List<SmsItem> = ArrayList()

        override fun getItemCount(): Int {
            return items.size
        }


        override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): SmsViewHolder {
            return SmsViewHolder.create(context, viewGroup)
        }

        override fun onBindViewHolder(vh: SmsViewHolder, i: Int) {
            val item = items.get(i)

            vh.setDate(item.date)
            vh.setContent(item.content)
        }
    }

    class SmsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            fun create(context: Context, viewGroup: ViewGroup): SmsViewHolder {
                return SmsViewHolder(LayoutInflater.from(context).inflate(R.layout.sms_list_item, viewGroup))
            }
        }

        private val dateView: TextView = itemView.findViewById(R.id.sms_date)
        private val contentView: TextView = itemView.findViewById(R.id.sms_item_content)

        fun setDate(date: Date) {
            dateView.text = date.longDefaultFormat()
        }

        fun setContent(text: String) {
            contentView.text = text
        }
    }
}
