package com.soumyadip.otpforwardapp.presentation.ui.fragments.root

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.soumyadip.otpforwardapp.R
import com.soumyadip.otpforwardapp.databinding.FragmentRootForwardPolicyBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [RootForwardPolicyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RootForwardPolicyFragment : BaseRootFragment() {
    private var _binding: FragmentRootForwardPolicyBinding? = null
    private val binding get() = _binding!!


    override fun getNavController(): NavController {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.roo_forward_policies_navhost) as NavHostFragment
        return navHostFragment.navController
    }

    override fun getRootDestinationID(): Int {
        return 0
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRootForwardPolicyBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}