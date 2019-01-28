package com.san4o.just4fun.smsparser.app.list

import android.Manifest
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.san4o.just4fun.smsparser.app.R
import com.san4o.just4fun.smsparser.app.SmsType
import com.san4o.just4fun.smsparser.app.dagger.AppScopeMember
import com.san4o.just4fun.smsparser.app.dagger.viewmodel.ViewModelFactory
import com.san4o.just4fun.smsparser.app.database.dao.SmsDao
import com.san4o.just4fun.smsparser.app.database.entities.Sms
import com.san4o.just4fun.smsparser.app.utils.*
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.sms_list.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class SmsListActivity : AppCompatActivity(), AppScopeMember {

    private val LOG_TAG: String = SmsListActivity::class.java.simpleName

    private val PERMISSION_REQUEST_CODE: Int = 111

    private lateinit var adapter: SmsListAdapter

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory<SmsListViewModel>
    private lateinit var viewModel: SmsListViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sms_list)

        this.viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(SmsListViewModel::class.java)

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
