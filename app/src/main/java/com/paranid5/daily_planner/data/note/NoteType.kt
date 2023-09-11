package com.paranid5.daily_planner.data.note

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class NoteType : Parcelable { SIMPLE, DATED }