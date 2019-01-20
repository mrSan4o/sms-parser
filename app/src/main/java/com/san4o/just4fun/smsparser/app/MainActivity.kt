package com.san4o.just4fun.smsparser.app

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.san4o.just4fun.smsparser.app.dagger.AppScopeMember
import com.san4o.just4fun.smsparser.app.database.dao.SmsDao
import com.san4o.just4fun.smsparser.app.database.entities.Sms
import com.san4o.just4fun.smsparser.app.utils.*
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.sms_list.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), AppScopeMember {

    private val LOG_TAG: String = MainActivity::class.java.simpleName

    private val PERMISSION_REQUEST_CODE: Int = 111

    private lateinit var adapter: SmsListAdapter


    @Inject
    lateinit var smsDao: SmsDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sms_list)

        adapter = SmsListAdapter(this)
        smsList.adapter = adapter
        smsList.layoutManager = LinearLayoutManager(this)

        syncButton.setOnClickListener { onSync() }


        val disposable = smsDao.findAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { items -> adapter.refreshItems(items)},
                {e -> showToastShort("Error ${e.message}")}
            )
    }

    override fun onStart() {
        super.onStart()

        if (!isPermissionGranted(Manifest.permission.READ_SMS)) {
            requestPermissions(Manifest.permission.READ_SMS, PERMISSION_REQUEST_CODE)
            return
        }


    }

    fun onSync() {

        val sms = readSms()

        val completables = sms.map { smsDao.insertNew(it) }

        val disposable = Completable.concat(completables)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    showToastShort("${sms.size} Sms sync")
                },
                { e ->
                    showToastShort("Error ${e.message}")
                    Log.e(LOG_TAG, e.message, e)
                }
            )

    }



    private fun parseSms(sms: List<Sms>) {
        for (it in sms) {
            val content = it.content

            Log.i(LOG_TAG, "")
            Log.i(LOG_TAG, "date : ${it.datetime.toDate().longDefaultFormat()}")
            Log.i(LOG_TAG, "content : ${it.content}")
        }
    }

    private fun readSms(): List<Sms> {
        val items: MutableList<Sms> = ArrayList()

        val uri = Uri.parse("content://sms")
        val messageCursor =
            contentResolver.query(
                uri,
                arrayOf("date", "body"),
//                arrayOf("_id", "thread_id", "address", "date", "type", "body"),
                "address = ?",
                arrayOf("900"),
                "date DESC"
            )


        if (messageCursor != null && messageCursor.count > 0) {
            while (messageCursor.moveToNext()) {
                val dateTimeString = messageCursor.getStringByName("date")
//                val address = messageCursor.getStringByName("address")
//                val type = messageCursor.getStringByName("type")
                val body = messageCursor.getStringByName("body")
                val date = Date(dateTimeString.toLong())
                var type = SmsType.UNKNOWN
                try {
                    type = SmsType.valueOfBody(body)
                } catch (e: Exception) {
                    Log.d(LOG_TAG, "Error ${date.longDefaultFormat()} : $body")
                    continue
                }




                Log.d(LOG_TAG, ">>> [${type.title}] ${date.longDefaultFormat()} : $body")
                items.add(Sms(null, body, date.time))
            }
//            items.sortByDescending { it.date }


        } else {
            Log.i(LOG_TAG, "EMPTY RESULT")
        }
        Log.i(LOG_TAG, "result : ${messageCursor.count}")

        return items
    }

    private fun requestPermissions(perm: String, code: Int) {
        requestPermissions(arrayOf(perm), code)
    }

    private fun isPermissionGranted(perm: String): Boolean {
        return checkSelfPermission(perm) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (PERMISSION_REQUEST_CODE == requestCode) {
            if (grantResults.first() == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }

    class SmsListAdapter(val context: Context) : RecyclerView.Adapter<SmsViewHolder>() {

        private val items: MutableList<Sms> = ArrayList()

        fun refreshItems(items: List<Sms>) {
            this.items.clear()
            this.items.addAll(items)

            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return items.size
        }


        override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): SmsViewHolder {
            return SmsViewHolder.create(context, viewGroup)
        }

        override fun onBindViewHolder(vh: SmsViewHolder, i: Int) {
            val item = items.get(i)

            vh.setDate(item.date())
            vh.setContent(item.content)
        }
    }

    class SmsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            fun create(context: Context, viewGroup: ViewGroup): SmsViewHolder {
                return SmsViewHolder(LayoutInflater.from(context).inflate(R.layout.sms_list_item, viewGroup, false))
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
