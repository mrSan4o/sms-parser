package com.san4o.just4fun.smsparser.app

import android.app.Activity
import android.app.Application
import com.san4o.just4fun.smsparser.app.dagger.AppComponent
import com.san4o.just4fun.smsparser.app.dagger.DaggerActivityLifecycleCallback
import com.san4o.just4fun.smsparser.app.dagger.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

class App : Application(), HasActivityInjector {

    var component : AppComponent? = null

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        component = DaggerAppComponent.builder()
            .app(this)
            .build().apply { inject(this@App) }

        registerActivityLifecycleCallbacks(DaggerActivityLifecycleCallback())

    }

    @Inject
    lateinit var activityInjector : DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

}