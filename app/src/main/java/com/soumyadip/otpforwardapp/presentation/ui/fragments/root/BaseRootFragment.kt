package com.soumyadip.otpforwardapp.presentation.ui.fragments.root

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.soumyadip.otpforwardapp.presentation.utils.hideKeyboard

abstract class BaseRootFragment : Fragment() {
    abstract fun getNavController(): NavController
    abstract fun getRootDestinationID(): Int

    val customOnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (getNavController().currentDestination?.id != getRootDestinationID()) getNavController().popBackStack()
        }
    }

    fun destinationChangeListener(
        container: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        customOnBackPressedCallback.isEnabled = destination.id != getRootDestinationID()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = getNavController()
        navController.addOnDestinationChangedListener(this::destinationChangeListener)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            customOnBackPressedCallback
        )
        view.setOnTouchListener(this::customOnTouchListener)
    }

    private fun customOnTouchListener(view: View, event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) hideKeyboard(requireActivity())
        return false
    }


}