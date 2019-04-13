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

    private val viewModel: SmsListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<SmsListBinding>(this, R.layout.sms_list)

        val adapter = SmsListAdapter(this, viewModel)

        binding.model = viewModel

        viewModel.errorLoadingNotification.observe(this, Observer { showToastShort("Error Loading") })
        viewModel.noItemsNotification.observe(this, Observer { showToastShort("No Items FOUND") })
        viewModel.refreshItemsViewCommand.observe(this, Observer { adapter.notifyDataSetChanged() })

        smsList.adapter = adapter
        smsList.layoutManager = LinearLayoutManager(this)

        syncButton.setOnClickListener { onSync() }

    }


    fun onSync() {
        viewModel.readSms()
    }
}