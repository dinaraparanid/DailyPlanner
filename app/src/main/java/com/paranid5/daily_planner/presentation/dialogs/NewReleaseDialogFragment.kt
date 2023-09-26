package com.paranid5.daily_planner.presentation.dialogs

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.paranid5.daily_planner.R
import com.paranid5.daily_planner.data.Release
import com.paranid5.daily_planner.databinding.DialogNewReleaseBinding
import com.paranid5.daily_planner.presentation.utils.ext.DefaultMarkwon

class NewReleaseDialogFragment : DialogFragment() {
    private val markwon by lazy { DefaultMarkwon(requireContext()) }

    companion object {
        val TAG = NewReleaseDialogFragment::class.simpleName!!
        private const val RELEASE_ARG = "release"

        fun newInstance(release: Release) = NewReleaseDialogFragment().apply {
            arguments = Bundle().apply { putParcelable(RELEASE_ARG, release) }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        MaterialAlertDialogBuilder(requireContext())
            .setCancelable(true)
            .setView(
                inflateViewBinding()
                    .apply { initView() }
                    .root
            )
            .create()

    private fun inflateViewBinding() =
        DataBindingUtil.inflate<DialogNewReleaseBinding>(
            layoutInflater,
            R.layout.dialog_new_release,
            null,
            false
        )

    private fun DialogNewReleaseBinding.initView() {
        val arg = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                requireArguments().getParcelable(RELEASE_ARG, Release::class.java)!!

            else -> requireArguments().getParcelable(RELEASE_ARG)!!
        }

        release = arg

        markwon.setMarkdown(body, arg.body)

        updateNowButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(arg.htmlUrl)))
        }

        updateLaterButton.setOnClickListener { dismiss() }
    }
}