package com.san4o.just4fun.smsparser.app.koin

import android.arch.persistence.room.Room
import com.san4o.just4fun.smsparser.app.database.AppDatabase
import com.san4o.just4fun.smsparser.app.model.MyObjectBox
import com.san4o.just4fun.smsparser.app.repository.*
import com.san4o.just4fun.smsparser.app.ui.list.SmsListViewModel
import io.objectbox.BoxStore
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import timber.log.Timber

val appModule = module {
    single { TestDataSmsDataSource(get()) as SmsDatasource}

//    single { SmsRoomStarage(get(), get()) as SmsStorage}
    single { SmsObjectBoxStorage(get()) as SmsStorage}

    single { SmsListAppRepository(get(), get()) as SmsListRepository}
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

val modelModule = module {
    single {
        val boxStore = MyObjectBox.builder()
            .androidContext(this.androidContext())
            .build()
        Timber.d("Using ObjectBox ${BoxStore.getVersion()} (${BoxStore.getVersionNative()})")
        return@single boxStore
    }
}