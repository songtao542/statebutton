package com.liabit.picker.datetime

import java.util.Calendar


/**
 * Author:         songtao
 * CreateDate:     2020/10/10 17:20
 */
data class DateTime(val year: Int, val month: Int, val day: Int, val hourOfDay: Int, val minute: Int) {
    val date: Date = Date(year, month, day)
    val time: Time = Time(hourOfDay, minute)
    val calendar: Calendar get() = Calendar.getInstance().apply { set(year, month, day, hourOfDay, minute) }

    companion object {
        internal fun from(date: Date, time: Time): DateTime {
            return DateTime(date.year, date.month, date.day, time.hourOfDay, time.minute)
        }
    }
}