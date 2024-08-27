package com.soumyadip.otpforwardapp.presentation.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.soumyadip.otpforwardapp.databinding.EditFilterValueFloatingDialogBinding
import com.soumyadip.otpforwardapp.databinding.FragmentConfigureFilterConditiosBinding
import com.soumyadip.otpforwardapp.domain.dto.Filter
import com.soumyadip.otpforwardapp.domain.dto.FlowResult
import com.soumyadip.otpforwardapp.presentation.ui.adapters.ConfigureFilterListAdapter
import com.soumyadip.otpforwardapp.presentation.ui.viewmodels.ConfigureFiltersViewModel
import com.soumyadip.otpforwardapp.presentation.utils.getNewFilterCondition
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [ConfigureFilterConditionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ConfigureFilterConditionFragment : BaseEditPolicyActivityNavigationFragment() {
    private var _binding: FragmentConfigureFilterConditiosBinding? = null
    private val binding get() = _binding!!
    private var _dialogBinding: EditFilterValueFloatingDialogBinding? = null
    private val dialogBinding get() = _dialogBinding!!
    private val configureFiltersViewModel by viewModels<ConfigureFiltersViewModel>()
    private lateinit var adapter: ConfigureFilterListAdapter
    private lateinit var dialog: AlertDialog

    private val args: ConfigureFilterConditionFragmentArgs by navArgs()

    @Inject
    lateinit var gson: Gson

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfigureFilterConditiosBinding.inflate(inflater, container, false)
        adapter = ConfigureFilterListAdapter(
            ::onEditTextClickListener,
            ::onAddPolicyClickListener,
            ::onDeleteButtonClickListener
        )
        val filtersListArgJSONString = args.allFiltersList
        val filterListNavArg: List<Filter> = gson.fromJson(
            filtersListArgJSONString,
            object : TypeToken<List<Filter>>() {}.type
        )
        configureFiltersViewModel.setValue(filterListNavArg)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpDialogBox()
        observerValidationResult()
        bind()
    }

    private fun bind() {
        adapter.submitList(configureFiltersViewModel.filterList)
        binding.configureFilterRecyclerView.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.configureFilterRecyclerView.adapter = adapter
        binding.configureFilterUpdateButton.setOnClickListener {
            onUpdateClickListener()
        }

    }

    private fun onUpdateClickListener() {
        configureFiltersViewModel.validateFilterList()
    }

    private fun observerValidationResult() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                configureFiltersViewModel.filterListValidationResult.collect { validationResult ->
                    binding.configureFilterErrorTextView.visibility = View.GONE
                    binding.configureFilterSpinkitView.visibility = View.GONE
                    if (validationResult != null) {
                        when (validationResult) {
                            is FlowResult.Error -> {
                                binding.configureFilterErrorTextView.text = validationResult.message
                                binding.configureFilterErrorTextView.visibility = View.VISIBLE
                            }

                            is FlowResult.Loading -> {
                                binding.configureFilterSpinkitView.visibility = View.VISIBLE
                            }

                            is FlowResult.Success -> {
                                val action =
                                    ConfigureFilterConditionFragmentDirections.actionConfigureFilterConditionFragmentToSetupForwardPolicy(
                                        allFiltersList = gson.toJson(configureFiltersViewModel.filterList)
                                    )
                                findNavController().navigate(action)
                            }
                        }
                    }

                }
            }
        }
    }

    private fun setUpDialogBox() {
        _dialogBinding = EditFilterValueFloatingDialogBinding.inflate(layoutInflater)
        dialog = AlertDialog.Builder(requireContext()).setView(dialogBinding.root).create()
        dialog.setOnShowListener {
            dialogBinding.dialogFilterValueEditText.requestFocus()
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            dialogBinding.dialogFilterValueEditText.postDelayed({
                imm.showSoftInput(
                    dialogBinding.dialogFilterValueEditText,
                    InputMethodManager.SHOW_IMPLICIT
                )
            }, 100)

        }
    }

    private fun onAddPolicyClickListener() {
        configureFiltersViewModel.addFilter(getNewFilterCondition())
        adapter.notifyDataSetChanged()
        binding.configureFilterRecyclerView.smoothScrollToPosition(adapter.itemCount - 1)
    }

    private fun onDeleteButtonClickListener(position: Int) {
        configureFiltersViewModel.removeFilter(position)
        adapter.notifyDataSetChanged()
    }


    private fun onEditTextClickListener(filterValue: String, callback: (String) -> Unit) {
        val editText = dialogBinding.dialogFilterValueEditText
        val button = dialogBinding.dialogFilterValueUpdateButton
        editText.setText(filterValue)
        editText.setSelection(filterValue.length)
        button.setOnClickListener {
            val currString = editText.text.toString().trim()
            if (currString.isNotBlank()) {
                callback(currString)

                dialog.dismiss()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Filter Value can not be blank",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        _dialogBinding = null
    }

}