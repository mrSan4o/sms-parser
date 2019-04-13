package com.san4o.just4fun.smsparser.app.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.san4o.just4fun.smsparser.app.R
import com.san4o.just4fun.smsparser.app.ui.list.SmsListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: SmsListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavigation()
    }

    private fun setupNavigation() {
        val navController = Navigation.findNavController(this, R.id.mainNavigationFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)

        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }

    override fun onSupportNavigateUp() =
        Navigation.findNavController(this, R.id.mainNavigationFragment).navigateUp()


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.readSmsMenuItem -> viewModel.readSms()
        }

        return super.onOptionsItemSelected(item)

    }
}
