package com.san4o.just4fun.smsparser.app.dagger.viewmodel

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModel
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Provider


class ViewModelFactory<T : ViewModel> @Inject constructor(
    private val viewModel: Lazy<T>
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = viewModel.get() as T
}