package com.soumyadip.otpforwardapp.presentation.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.soumyadip.otpforwardapp.presentation.ui.fragments.ForwardPoliciesMainFragment
import com.soumyadip.otpforwardapp.presentation.ui.fragments.StartStopServiceFragment

class MainViewPagerAdapter(acitivty: FragmentActivity) : FragmentStateAdapter(acitivty) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment? = null
        return when (position) {
            0 -> StartStopServiceFragment()
            1 -> ForwardPoliciesMainFragment()
            else -> StartStopServiceFragment()
        }
    }


}