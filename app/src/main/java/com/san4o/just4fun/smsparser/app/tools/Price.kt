package com.san4o.just4fun.smsparser.app.tools

class Price {
    companion object {
        val rub = "р"
        val regex = Regex("[0-9]+[.]?([0-9]{0,2})?$rub(.)?")

        fun toPrice(textPrice: String): Double {
            return textPrice.trim().substringBefore(rub).toDouble()
        }
    }
}