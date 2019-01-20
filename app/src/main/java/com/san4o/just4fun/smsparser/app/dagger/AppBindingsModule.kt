package com.san4o.just4fun.smsparser.app.dagger

import com.san4o.just4fun.smsparser.app.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface AppBindingsModule {

    @ContributesAndroidInjector
    fun contributeMainActivityInjector() : MainActivity
}