package com.san4o.just4fun.smsparser.app.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.san4o.just4fun.smsparser.app.R
import com.san4o.just4fun.smsparser.app.ui.list.SmsListActivity

class SplashActivity : AppCompatActivity() {

    private val permissions = arrayOf(
        android.Manifest.permission.READ_SMS,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private val requestCode = 11

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (permissions.all { checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED }){
            start()
        }else{
            requestPermissions(permissions, requestCode)
        }
    }

    private fun start() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == this.requestCode){
            val notGranted = grantResults.filter { it != PackageManager.PERMISSION_GRANTED }
            if (notGranted.isNotEmpty()){
                requestPermissions(permissions, requestCode)
            }else{
                start()
            }

        }
    }
}
