package com.san4o.just4fun.smsparser.app.dagger

import com.san4o.just4fun.smsparser.app.SmsListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface AppBindingsModule {

    @ContributesAndroidInjector
    fun contributeMainActivityInjector() : SmsListActivity
}