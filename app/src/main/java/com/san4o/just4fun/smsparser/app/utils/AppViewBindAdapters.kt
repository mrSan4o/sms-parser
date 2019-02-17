package com.san4o.just4fun.smsparser.app.utils

import android.databinding.BindingAdapter
import android.view.View

@BindingAdapter("app:visible")
fun visible(view : View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}