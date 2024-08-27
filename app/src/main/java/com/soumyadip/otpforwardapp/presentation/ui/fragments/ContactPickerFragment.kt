package com.soumyadip.otpforwardapp.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.soumyadip.otpforwardapp.databinding.FragmentContactPickerBinding
import com.soumyadip.otpforwardapp.domain.dto.FlowResult
import com.soumyadip.otpforwardapp.domain.dto.PhoneContact
import com.soumyadip.otpforwardapp.presentation.ui.adapters.ContactPickerListAdapter
import com.soumyadip.otpforwardapp.presentation.ui.viewmodels.ContactPickerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ContactPickerFragment : BaseEditPolicyActivityNavigationFragment() {
    private var _binding: FragmentContactPickerBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ContactPickerListAdapter
    private val contactPickerViewModel by viewModels<ContactPickerViewModel>()
    private val args: ContactPickerFragmentArgs by navArgs()


    @Inject
    lateinit var gson: Gson


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactPickerBinding.inflate(inflater, container, false)
        adapter = ContactPickerListAdapter()
        val allContactListJSONString = args.allContactList
        val allContactListNavArg: List<PhoneContact> = gson.fromJson(
            allContactListJSONString,
            object : TypeToken<List<PhoneContact>>() {}.type
        )
        contactPickerViewModel.setValue(allContactListNavArg)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        observerFilteredValues()

    }

    private fun observerFilteredValues() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                contactPickerViewModel.filteredContactList.collect {
                    when (it) {
                        is FlowResult.Error -> {

                        }

                        is FlowResult.Loading -> {

                        }

                        is FlowResult.Success -> {
                            adapter.submitList(it.data)
                            binding.contactPickerList.smoothScrollToPosition(0)
                        }
                    }
                }
            }
        }
    }

    private fun bind() {
        binding.contactPickerList.adapter = adapter
        binding.contactPickerList.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        adapter.submitList(contactPickerViewModel.phoneContactList)
        binding.contactPickerUpdateButton.setOnClickListener {
            onUpdateClickListener()
        }
        val x = binding.contactPickerSearchView
        binding.contactPickerSearchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                contactPickerViewModel.fetchFilteredContacts(newText)
                return true
            }
        })
    }


    private fun onUpdateClickListener() {
        val action =
            ContactPickerFragmentDirections.actionContactPickerFragmentToSetupForwardPolicy(
                gson.toJson(contactPickerViewModel.phoneContactList)
            )
        findNavController().navigate(action)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}