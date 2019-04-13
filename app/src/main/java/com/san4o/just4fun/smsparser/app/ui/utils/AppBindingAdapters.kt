package com.san4o.just4fun.smsparser.app.ui.utils

import android.databinding.BindingAdapter
import android.view.View
import android.widget.TextView

@BindingAdapter("app:bind_text")
fun bindTextTextView(view : TextView, text : String){
    view.text = text
}@BindingAdapter("app:bind_text")
fun bindTextTextView(view : TextView, text : Double){
    view.text = text.toString()
}
@BindingAdapter("app:visible")
fun visible(view : View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}