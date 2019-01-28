package com.san4o.just4fun.smsparser.app.dagger

import com.san4o.just4fun.smsparser.app.list.SmsListActivity
import com.san4o.just4fun.smsparser.app.repository.SmsListRepository
import com.san4o.just4fun.smsparser.app.repository.SmsListRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import android.arch.lifecycle.ViewModel
import com.san4o.just4fun.smsparser.app.dagger.viewmodel.ViewModelFactory
import com.san4o.just4fun.smsparser.app.dagger.viewmodel.ViewModelKey
import com.san4o.just4fun.smsparser.app.list.SmsListViewModel
import dagger.multibindings.IntoMap



@Module
interface AppBindingsModule {


    @Binds
    @IntoMap
    @ViewModelKey(SmsListViewModel::class)
    fun bindSmsListViewModel(userViewModel: SmsListViewModel): ViewModel

    @Binds
    fun bind(impl:SmsListRepositoryImpl) : SmsListRepository

    @ContributesAndroidInjector
    fun contributeMainActivityInjector() : SmsListActivity
}