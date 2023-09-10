package com.paranid5.daily_planner.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

interface StateChangedCallback<H : UIHandler?, T> {
    data class State<T>(var isChanged: Boolean, var value: T?) {
        constructor(value: T) : this(isChanged = false, value = value)
        constructor(isChanged: Boolean) : this(isChanged, value = null)
        constructor() : this(isChanged = false, value = null)
    }

    fun onStateChanged(handler: H, value: T?)

    fun observe(owner: LifecycleOwner, state: LiveData<State<T>>, handler: H) =
        state.observe(owner) { (isChanged, value) ->
            if (isChanged) onStateChanged(handler, value)
        }
}