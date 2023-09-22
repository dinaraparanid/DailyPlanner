package com.paranid5.daily_planner.domain.utils.ext

import kotlinx.datetime.LocalDateTime
import java.util.Calendar

fun Calendar.toLocalDateTime() = LocalDateTime(
    get(Calendar.YEAR),
    get(Calendar.MONTH),
    get(Calendar.DAY_OF_MONTH),
    get(Calendar.HOUR_OF_DAY),
    get(Calendar.MINUTE)
)