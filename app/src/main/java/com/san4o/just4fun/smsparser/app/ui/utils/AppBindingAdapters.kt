package com.san4o.just4fun.smsparser.app.ui.utils

import android.databinding.BindingAdapter
import android.view.View
import android.widget.TextView
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat

val df = DecimalFormat("#,###")
    .apply {
        roundingMode = RoundingMode.HALF_UP
    }
val nf = NumberFormat.getNumberInstance()
    .apply {
        maximumFractionDigits = 0
        roundingMode = RoundingMode.HALF_UP
        isGroupingUsed = true
    }


@BindingAdapter("app:bind_text")
fun bindTextTextView(view : TextView, text : String){
    view.text = text
}
@BindingAdapter("app:bind_text")
fun bindDoubleTextView(view : TextView, text : Double){
    view.text = text.toString()
}
@BindingAdapter("app:bind_price")
fun bindPriceTextView(view : TextView, d : Double){
    val format = nf.format(d).replace(",", " ")
    view.text = "$format p."
}
@BindingAdapter("app:visible")
fun visible(view : View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}