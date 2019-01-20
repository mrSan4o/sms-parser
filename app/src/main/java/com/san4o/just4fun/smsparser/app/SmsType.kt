package com.san4o.just4fun.smsparser.app

import android.util.Log
import java.lang.IllegalArgumentException
import java.lang.RuntimeException

enum class SmsType(val title:String) {

    UNKNOWN("Неизвестно"),
    TRANSFER("Перевод"),
    INFLOW("Зачисление"),
    OUTFLOW("Расход"),
    EXTRADITION("Выдача"),
    PAYMENT("Оплата"),
    WRITE_OFF("Списание"),

    ;



    companion object {

        fun valueOfBody(body: String): SmsType {
            if (!body.startsWith("VISA7065")) {
                throw RuntimeException("This is not operation body : $body")
            }
            if (body.contains("Покупка")
                || body.contains("покупка")
                || body.contains("ПОКУПКА")
            ) {
                return OUTFLOW
            }

            if (body.contains("Выдача")
                || body.contains("выдача")
                || body.contains("ВЫДАЧА")
            ) {
                return EXTRADITION
            }
            if (body.contains("Оплата")
                || body.contains("оплата")
                || body.contains("ОПЛАТА")
            ) {
                return PAYMENT
            }
            if (body.contains("списание")
                || body.contains("Списание")
                || body.contains("СПИСАНИЕ")
            ) {
                return WRITE_OFF
            }
            if (body.contains("зачисление")
                || body.contains("Зачисление")
                || body.contains("ЗАЧИСЛЕНИЕ")
            ) {
                return INFLOW
            }

            Log.d("SMS_TYPE", "Unknown : $body")

            return UNKNOWN
        }
    }
}