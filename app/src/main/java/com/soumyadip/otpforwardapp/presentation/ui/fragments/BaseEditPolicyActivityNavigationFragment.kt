package com.soumyadip.otpforwardapp.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.soumyadip.otpforwardapp.databinding.OnBackPressedDialogBinding

abstract class BaseEditPolicyActivityNavigationFragment : Fragment() {

    private var _dialogBinding: OnBackPressedDialogBinding? = null
    private val dialogBinding: OnBackPressedDialogBinding get() = _dialogBinding!!
    private lateinit var onBackPressedDialog: AlertDialog
    private var onDismissCallbackEnabled = true

    private val fragmentOnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            onBackPressedDialog.show()
        }

    }
    private val alertDialogOnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            dismissAndGoBack()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createDialogBox()

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            fragmentOnBackPressedCallback
        )
    }

    private fun dismissAndGoBack() {
        onBackPressedDialog.dismiss()
        fragmentOnBackPressedCallback.isEnabled = false
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun createDialogBox() {
        _dialogBinding = OnBackPressedDialogBinding.inflate(LayoutInflater.from(requireContext()))
        onBackPressedDialog =
            AlertDialog.Builder(requireContext()).setView(dialogBinding.root).create()

        onBackPressedDialog.onBackPressedDispatcher.addCallback(alertDialogOnBackPressedCallback)
        dialogBinding.dialogContinueButton.setOnClickListener {
            onBackPressedDialog.dismiss()
        }
        dialogBinding.dialogDiscardButton.setOnClickListener {
            dismissAndGoBack()

        }


    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentOnBackPressedCallback.isEnabled = false
        _dialogBinding = null
    }


}