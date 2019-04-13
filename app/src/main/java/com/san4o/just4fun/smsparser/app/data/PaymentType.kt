package com.san4o.just4fun.smsparser.app.data

enum class PaymentType(val id: Int, val title: String = "") {


    UNKNOWN(0),
    INFLOW(1, "Приход"),
    OUTFLOW(2, "Расход")
    ;

    companion object {
        val inflowWorlds = arrayOf("Покупка", "Отмена")
        val outflowWorlds = arrayOf("списание")

        fun parse(token: String): PaymentType {
            if (inflowWorlds.contains(token)){
                return INFLOW
            }
            if (outflowWorlds.contains(token)){
                return OUTFLOW
            }
            return UNKNOWN
        }
    }
}