package com.soumyadip.otpforwardapp.presentation.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.soumyadip.otpforwardapp.databinding.FragmentStartStopServiceBinding
import com.soumyadip.otpforwardapp.presentation.OTPForwardService
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [StartStopServiceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StartStopServiceFragment : Fragment() {

    private var _binding: FragmentStartStopServiceBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentStartStopServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        observerService()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun bind() {
        binding.homeStartServiceButton.setOnClickListener {
            startService()
        }
        binding.homeStopServiceButton.setOnClickListener {
            stopService()
        }
    }

    private fun startService() {
        val intent = Intent(requireContext(), OTPForwardService::class.java).apply {
            action = OTPForwardService.Action.Start_Service.toString()
        }
        requireActivity().startService(intent)
    }

    private fun stopService() {
        val intent = Intent(requireContext(), OTPForwardService::class.java).apply {
            action = OTPForwardService.Action.Stop_Service.toString()
        }
        requireActivity().startService(intent)
    }

    private fun updateUiServiceRunning() {
        binding.homeStartServiceButton.visibility = View.GONE
        binding.homeStopServiceButton.visibility = View.VISIBLE
        binding.homeRunningSinceTextView.visibility = View.VISIBLE
        binding.homeRunningStatusTextView.text = "Running"
        binding.homeRunningSinceTextView.text =
            "Running Since: ${OTPForwardService.serviceRunningSince}"
    }

    private fun updateUiServiceStopped() {
        binding.homeStartServiceButton.visibility = View.VISIBLE
        binding.homeStopServiceButton.visibility = View.GONE
        binding.homeRunningSinceTextView.visibility = View.GONE
        binding.homeRunningStatusTextView.text = "Not Running"
    }

    private fun observerService() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                OTPForwardService.isServiceRunning.collect {
                    when (it) {
                        true -> updateUiServiceRunning()
                        false -> updateUiServiceStopped()
                    }
                }
            }
        }
    }

}


