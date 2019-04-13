package com.san4o.just4fun.smsparser.app.koin

import android.arch.persistence.room.Room
import com.san4o.just4fun.smsparser.app.database.AppDatabase
import com.san4o.just4fun.smsparser.app.repository.SmsDatasource
import com.san4o.just4fun.smsparser.app.repository.SmsListRepository
import com.san4o.just4fun.smsparser.app.repository.SmsListRepositoryImpl
import com.san4o.just4fun.smsparser.app.repository.TestDataSmsDataSource
import com.san4o.just4fun.smsparser.app.ui.list.SmsListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { TestDataSmsDataSource(get()) as SmsDatasource}

    single { SmsListRepositoryImpl(get(), get(), get()) as SmsListRepository}
}

val databaseModule = module {

    single {
        return@single Room.databaseBuilder(this.androidContext(), AppDatabase::class.java, "sms_parser_db")
            .build()
    }

    single { get<AppDatabase>().provideSmsDao()}

    single { get<AppDatabase>().providePaymentDao() }

}

val viewModelModule = module {

    viewModel { SmsListViewModel(get()) }
}