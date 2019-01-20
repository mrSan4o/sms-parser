package com.san4o.just4fun.smsparser.app.dagger

import android.content.Context
import com.san4o.just4fun.smsparser.app.database.AppDatabase
import com.san4o.just4fun.smsparser.app.database.dao.SmsDao

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context) : AppDatabase =  AppDatabase.buildDatabase(context)

    @Singleton
    @Provides
    fun provideSmsdao(db : AppDatabase) : SmsDao = db.provideSmsDao()
}