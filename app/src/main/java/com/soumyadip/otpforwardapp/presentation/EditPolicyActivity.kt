package com.soumyadip.otpforwardapp.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.soumyadip.otpforwardapp.R
import com.soumyadip.otpforwardapp.databinding.ActivityEditPolicyBinding
import com.soumyadip.otpforwardapp.domain.dto.ForwardPolicyDetails
import com.soumyadip.otpforwardapp.domain.dto.PhoneContact
import com.soumyadip.otpforwardapp.presentation.ui.viewmodels.EditForwardPolicyViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditPolicyActivity : AppCompatActivity() {
    private var _binding: ActivityEditPolicyBinding? = null
    private val binding: ActivityEditPolicyBinding get() = _binding!!

    private val editForwardPolicyViewModel by viewModels<EditForwardPolicyViewModel>()
    private lateinit var forwardPolicyDetailsArg: ForwardPolicyDetails
    private var phoneContactListArg: List<PhoneContact> = emptyList()


    @Inject
    lateinit var gson: Gson
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //installSplashScreen()
        enableEdgeToEdge()
        _binding = ActivityEditPolicyBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.edit_policy_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initializeIntentArgs()
        editForwardPolicyViewModel.setForwardPolicy(forwardPolicyDetailsArg)

    }


    private fun initializeIntentArgs() {
        val currIntent = intent
        val forwardPolicyJSONString = currIntent.getStringExtra("policyItem")
        forwardPolicyDetailsArg =
            gson.fromJson(forwardPolicyJSONString, ForwardPolicyDetails::class.java)

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}