package com.san4o.just4fun.smsparser.app

import android.app.Application
import com.san4o.just4fun.smsparser.app.koin.appModule
import com.san4o.just4fun.smsparser.app.koin.databaseModule
import com.san4o.just4fun.smsparser.app.koin.modelModule
import com.san4o.just4fun.smsparser.app.koin.viewModelModule
import com.san4o.just4fun.smsparser.app.model.MyObjectBox
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application(){



    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())



        startKoin {
            // use AndroidLogger as Koin Logger - default Level.INFO
            androidLogger()

            // use the Android context given there
            androidContext(this@App)

            // load properties from assets/koin.properties file
//            androidFileProperties()

            // module list
            modules(appModule, databaseModule, viewModelModule, modelModule)
        }
    }
}