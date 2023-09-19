package com.paranid5.daily_planner.data.utils.ext

inline val String.filledToTimeFormat
    get() = if (length < 2) "${"0".repeat(2 - length)}$this" else this