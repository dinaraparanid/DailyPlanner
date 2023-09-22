package com.paranid5.daily_planner.domain.utils.ext

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

inline val currentTime
    get() = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())