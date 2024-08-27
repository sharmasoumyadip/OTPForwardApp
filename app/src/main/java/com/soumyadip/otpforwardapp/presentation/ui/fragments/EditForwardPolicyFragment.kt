package com.soumyadip.otpforwardapp.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.soumyadip.otpforwardapp.databinding.FragmentEditForwardPolicyBinding
import com.soumyadip.otpforwardapp.domain.dto.Filter
import com.soumyadip.otpforwardapp.domain.dto.FlowResult
import com.soumyadip.otpforwardapp.domain.dto.ForwardPolicyDetails
import com.soumyadip.otpforwardapp.domain.dto.PhoneContact
import com.soumyadip.otpforwardapp.presentation.ui.adapters.EditPolicyContactListAdapter
import com.soumyadip.otpforwardapp.presentation.ui.adapters.EditPolicyFilterListAdapter
import com.soumyadip.otpforwardapp.presentation.ui.viewmodels.EditForwardPolicyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [EditForwardPolicyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class EditForwardPolicyFragment : BaseEditPolicyActivityNavigationFragment() {
    private var _binding: FragmentEditForwardPolicyBinding? = null
    private val binding get() = _binding!!
    private val args: EditForwardPolicyFragmentArgs by navArgs()
    private val editForwardPolicyViewModel by activityViewModels<EditForwardPolicyViewModel>()

    private lateinit var contactListAdapter: EditPolicyContactListAdapter
    private lateinit var filterListAdapter: EditPolicyFilterListAdapter

    @Inject
    lateinit var gson: Gson
    private lateinit var currForwardPolicyDetails: ForwardPolicyDetails
    private var allContactListArg: List<PhoneContact>? = null
    private var allFilterListArg: List<Filter>? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditForwardPolicyBinding.inflate(inflater, container, false)
        contactListAdapter = EditPolicyContactListAdapter()
        filterListAdapter = EditPolicyFilterListAdapter()
        currForwardPolicyDetails = editForwardPolicyViewModel.forwardPolicy
        fetchNavArgs()
        return binding.root
    }

    private fun fetchNavArgs() {
        val allContactListJSONString = args.allContactList
        if (allContactListJSONString != null)
            allContactListArg = gson.fromJson(
                allContactListJSONString,
                object : TypeToken<List<PhoneContact>>() {}.type
            )
        val allFiltersListJSONString = args.allFiltersList
        if (allFiltersListJSONString != null)
            allFilterListArg = gson.fromJson(
                allFiltersListJSONString,
                object : TypeToken<List<Filter>>() {}.type
            )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        editForwardPolicyViewModel.fetchValues(allContactListArg, allFilterListArg)
        startCollectingData()
        startCollectingSaveData()
    }

    private fun startCollectingSaveData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                editForwardPolicyViewModel.saveResult.collect {
                    when (it) {
                        is FlowResult.Error -> {
                            binding.editPolicyErrorMessage.text = it.message
                            binding.editPolicyErrorMessage.visibility = View.VISIBLE
                            Toast.makeText(
                                requireContext(),
                                "Invalid Value. Please check error at top of the screen.",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.root.smoothScrollTo(0, 0)
                        }

                        is FlowResult.Loading -> {
                            binding.editPolicyErrorMessage.visibility = View.GONE
                        }

                        is FlowResult.Success -> {
                            Toast.makeText(
                                requireContext(),
                                "Successfully Saved",
                                Toast.LENGTH_SHORT
                            ).show()
                            requireActivity().finish()
                        }
                    }

                }
            }
        }
    }

    private fun toggleFilterListEmptyItemPlaceHolder(visbility: Boolean) {
        binding.editPolicyEmptyFilterListTextView.visibility =
            if (visbility) View.VISIBLE else View.GONE
        binding.editPolicyFilterRecyclerview.visibility =
            if (!visbility) View.VISIBLE else View.GONE

    }

    private fun toggleContactListEmptyItemPlaceHolder(visbility: Boolean) {
        binding.editPolicyEmptyContactListTextView.visibility =
            if (visbility) View.VISIBLE else View.GONE
        binding.editPolicyContactRecyclerview.visibility =
            if (!visbility) View.VISIBLE else View.GONE

    }


    private fun startCollectingData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                editForwardPolicyViewModel.policyFilterAndContactsFlowResult.collect {
                    when (it) {
                        is FlowResult.Error -> {}
                        is FlowResult.Loading -> {
                            enableDisableButtons(false)
                            toggleContactListEmptyItemPlaceHolder(true)
                            toggleFilterListEmptyItemPlaceHolder(true)
                        }

                        is FlowResult.Success -> {
                            if (it.data != null) {
                                val selectedContacts = it.data.selectedContactList
                                val savedFilters = it.data.savedFilters

                                filterListAdapter.submitList(savedFilters)
                                contactListAdapter.submitList(selectedContacts)
                                toggleFilterListEmptyItemPlaceHolder(savedFilters.isEmpty())
                                toggleContactListEmptyItemPlaceHolder(selectedContacts.isEmpty())
                                enableDisableButtons(true)


                            }

                        }
                    }
                }
            }
        }
    }


    private fun bind() {
        binding.editPolicyTitleEditText.setText(currForwardPolicyDetails.policyName)
        binding.editPolicyActiveSwitch.isChecked = currForwardPolicyDetails.isActive
        if (currForwardPolicyDetails.uniqueMessageIdentifier != null) binding.editPolicyIdentifierEditText.setText(
            currForwardPolicyDetails.uniqueMessageIdentifier
        )
        val editPolicyContactRecyclerview = binding.editPolicyContactRecyclerview
        editPolicyContactRecyclerview.adapter = contactListAdapter
        editPolicyContactRecyclerview.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.editPolicyContactsButton.setOnClickListener { editContactsOnClickListener() }

        val editPolicyFilterRecyclerview = binding.editPolicyFilterRecyclerview
        editPolicyFilterRecyclerview.adapter = filterListAdapter
        editPolicyFilterRecyclerview.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        binding.editPolicyFilterButton.setOnClickListener { editFiltersOnClickListener() }
        binding.editPolicyCancelButton.setOnClickListener { cancelButtonOnClickListener() }
        binding.editPolicySaveButton.setOnClickListener { saveButtonOnClickListener() }


    }

    private fun saveButtonOnClickListener() {
        editForwardPolicyViewModel.setForwardPolicy(getDataFromUI())
        editForwardPolicyViewModel.savePolicy()
    }

    private fun cancelButtonOnClickListener() {
        Toast.makeText(
            requireContext(),
            "The Changes are not saved. Operation cancelled",
            Toast.LENGTH_SHORT
        ).show()
        requireActivity().finish()
    }

    private fun editFiltersOnClickListener() {
        val forwardPolicyFromUI = getDataFromUI()
        editForwardPolicyViewModel.setForwardPolicy(forwardPolicyFromUI)
        val action =
            EditForwardPolicyFragmentDirections.actionSetupForwardPolicyToConfigureFilterConditionFragment(
                gson.toJson(editForwardPolicyViewModel.policyFilterAndContactsFlowResult.value.data!!.savedFilters)
            )
        findNavController().navigate(action)
    }

    private fun editContactsOnClickListener() {
        val forwardPolicyFromUI = getDataFromUI()
        editForwardPolicyViewModel.setForwardPolicy(forwardPolicyFromUI)
        val action =
            EditForwardPolicyFragmentDirections.actionSetupForwardPolicyToContactPickerFragment(
                gson.toJson(editForwardPolicyViewModel.policyFilterAndContactsFlowResult.value.data!!.allPhoneContactsWithSelected)
            )
        findNavController().navigate(action)
    }

    private fun enableDisableButtons(isEnabled: Boolean) {
        binding.editPolicySaveButton.isEnabled = isEnabled
        binding.editPolicyCancelButton.isEnabled = isEnabled
        binding.editPolicyContactsButton.isEnabled = isEnabled
        binding.editPolicyFilterButton.isEnabled = isEnabled
    }

    private fun getDataFromUI(): ForwardPolicyDetails {
        val policyID = currForwardPolicyDetails.policyId
        val policyName = binding.editPolicyTitleEditText.text.toString()
        val policyIdentifier = binding.editPolicyIdentifierEditText.text.toString()
        val isPolicyActive = binding.editPolicyActiveSwitch.isChecked

        return ForwardPolicyDetails(policyID, policyName, policyIdentifier, isPolicyActive)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}