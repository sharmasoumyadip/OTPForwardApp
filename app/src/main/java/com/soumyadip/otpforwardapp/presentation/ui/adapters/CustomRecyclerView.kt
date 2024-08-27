package com.soumyadip.otpforwardapp.presentation.ui.adapters

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.soumyadip.otpforwardapp.presentation.utils.hideKeyboard

class CustomRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    fun getActivity(): Activity? {
        var currentContext = context
        while (currentContext is ContextWrapper) {
            if (currentContext is Activity) {
                return currentContext // Found the Activity
            }
            currentContext = currentContext.baseContext // Move up the context hierarchy
        }
        return null // Activity not found
    }


    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        val activity = getActivity()
        if (e?.action == MotionEvent.ACTION_DOWN && activity != null) hideKeyboard(activity)
        return super.onInterceptTouchEvent(e)
    }

}

