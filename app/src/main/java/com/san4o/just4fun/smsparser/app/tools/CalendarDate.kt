package com.san4o.just4fun.smsparser.app.tools

import java.util.*

class CalendarDate(private val date: Date) {
    private val calendar: Calendar = Calendar.getInstance()

    init {
        calendar.time = date
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
    }

    fun setDate(date: Date) {
        val dateCalendar = Calendar.getInstance()
        dateCalendar.time  = date
        calendar.set(Calendar.DAY_OF_MONTH, dateCalendar.get(Calendar.DAY_OF_MONTH))
        calendar.set(Calendar.MONTH, dateCalendar.get(Calendar.MONTH))
        calendar.set(Calendar.YEAR, dateCalendar.get(Calendar.YEAR))
    }

    fun setTime(time: Date) {
        val timeCalendar = Calendar.getInstance()
        timeCalendar.time = time
        calendar.set(Calendar.HOUR, timeCalendar.get(Calendar.HOUR))
        calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))
    }

    fun getDate(): Date = calendar.time
}