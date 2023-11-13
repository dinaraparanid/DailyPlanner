package com.paranid5.daily_planner.domain.utils.ext

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Instant.toLocalDateTime() = toLocalDateTime(TimeZone.currentSystemDefault())