package com.san4o.just4fun.smsparser.app.dagger

import android.content.Context
import com.san4o.just4fun.smsparser.app.App
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun provideContext(app: App): Context = app
        
}