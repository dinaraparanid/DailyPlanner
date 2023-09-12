package com.paranid5.daily_planner.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SimpleNotesState

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DatedNotesState