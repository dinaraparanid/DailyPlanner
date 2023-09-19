package com.paranid5.daily_planner.presentation.utils

import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.paranid5.daily_planner.R
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

inline fun DatePicker(crossinline onPicked: (Long) -> Unit) =
    MaterialDatePicker.Builder.datePicker()
        .setTitleText(R.string.select_date)
        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        .setPositiveButtonText(R.string.ok)
        .setNegativeButtonText(R.string.cancel)
        .build()
        .apply { addOnPositiveButtonClickListener { onPicked(it) } }

inline fun TimePicker(crossinline onPicked: (Pair<Int, Int>) -> Unit): MaterialTimePicker {
    val now = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())

    return MaterialTimePicker.Builder()
        .setTitleText(R.string.select_time)
        .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
        .setTimeFormat(TimeFormat.CLOCK_24H)
        .setHour(now.hour)
        .setMinute(now.minute)
        .setPositiveButtonText(R.string.ok)
        .setNegativeButtonText(R.string.cancel)
        .build()
        .apply { addOnPositiveButtonClickListener { onPicked(hour to minute) } }
}