package com.paranid5.daily_planner.presentation.dialogs

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.paranid5.daily_planner.R

@RequiresApi(Build.VERSION_CODES.S)
class AlarmPermissionRequiredDialogFragment : DialogFragment() {
    companion object {
        val TAG = AlarmPermissionRequiredDialogFragment::class.simpleName!!
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        MaterialAlertDialogBuilder(requireContext())
            .setCancelable(false)
            .setView(layoutInflater.inflate(R.layout.dialog_alarm_permission_required, null))
            .setPositiveButton(R.string.ok) { dialog, _ ->
                startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .create()
}