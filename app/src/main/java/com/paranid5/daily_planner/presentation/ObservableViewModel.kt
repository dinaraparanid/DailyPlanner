package com.paranid5.daily_planner.presentation

import androidx.lifecycle.ViewModel

abstract class ObservableViewModel<P : BasePresenter, H : UIHandler> : ViewModel() {
    abstract val presenter: P
    abstract val handler: H
}