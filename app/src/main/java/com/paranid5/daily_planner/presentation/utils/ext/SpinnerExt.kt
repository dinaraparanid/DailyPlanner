package com.paranid5.daily_planner.presentation.utils.ext

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.paranid5.daily_planner.R

internal inline fun Spinner.initTypeSpinner(
    context: Context,
    initialNoteType: Int,
    crossinline setNoteType: (Int) -> Unit
) {
    adapter = TypeSpinnerAdapter(context)
    onItemSelectedListener = TypeSpinnerListener(setNoteType)
    setSelection(initialNoteType)
}

internal inline fun Spinner.initRepetitionSpinner(
    context: Context,
    initialRepetitionOrdinal: Int,
    crossinline setRepetition: (Int) -> Unit
) {
    adapter = RepetitionAdapter(context)
    onItemSelectedListener = RepetitionListener(setRepetition)
    setSelection(initialRepetitionOrdinal)
}

private fun TypeSpinnerAdapter(context: Context) =
    ArrayAdapter
        .createFromResource(context, R.array.note_types, android.R.layout.simple_spinner_item)
        .also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

private inline fun TypeSpinnerListener(crossinline setNoteType: (Int) -> Unit) =
    object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
            setNoteType(position)

        override fun onNothingSelected(parent: AdapterView<*>?) = Unit
    }

private fun RepetitionAdapter(context: Context) =
    ArrayAdapter
        .createFromResource(context, R.array.repetition_type, android.R.layout.simple_spinner_item)
        .also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

private inline fun RepetitionListener(crossinline setRepetition: (Int) -> Unit) =
    object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
            setRepetition(position)

        override fun onNothingSelected(parent: AdapterView<*>?) = Unit
    }