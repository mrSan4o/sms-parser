package com.san4o.just4fun.smsparser.app.ui.list

import android.Manifest
import android.arch.lifecycle.Observer
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.san4o.just4fun.smsparser.app.R
import com.san4o.just4fun.smsparser.app.databinding.SmsListBinding
import com.san4o.just4fun.smsparser.app.utils.showToastShort
import kotlinx.android.synthetic.main.sms_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class SmsListActivity : AppCompatActivity() {

    private val LOG_TAG: String = SmsListActivity::class.java.simpleName

    private val PERMISSION_REQUEST_CODE: Int = 111

    private lateinit var adapter: SmsListAdapter


    private val viewModel: SmsListViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<SmsListBinding>(this, R.layout.sms_list)


        binding.model = viewModel

        viewModel.errorLoadingNotification.observe(this, Observer { showToastShort("Error Loading") })
        viewModel.noItemsNotification.observe(this, Observer { showToastShort("No Items FOUND") })
        viewModel.refreshItemsViewCommand.observe(this, Observer { adapter.notifyDataSetChanged() })

        adapter = SmsListAdapter(this, viewModel)
        smsList.adapter = adapter
        smsList.layoutManager = LinearLayoutManager(this)

        syncButton.setOnClickListener { onSync() }

    }

    override fun onStart() {
        super.onStart()

        if (!isPermissionGranted(Manifest.permission.READ_SMS)) {
            requestPermissions(Manifest.permission.READ_SMS, PERMISSION_REQUEST_CODE)
            return
        }
    }

    fun onSync() {
        viewModel.readSms()
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


}
