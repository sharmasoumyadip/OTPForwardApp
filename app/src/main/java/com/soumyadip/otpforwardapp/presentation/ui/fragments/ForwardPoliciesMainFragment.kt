package com.soumyadip.otpforwardapp.presentation.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.soumyadip.otpforwardapp.databinding.FragmentForwardPoliciesMainBinding
import com.soumyadip.otpforwardapp.domain.dto.FlowResult
import com.soumyadip.otpforwardapp.domain.dto.ForwardPolicyDetails
import com.soumyadip.otpforwardapp.presentation.EditPolicyActivity
import com.soumyadip.otpforwardapp.presentation.ui.adapters.ForwardPolicyMainListAdapter
import com.soumyadip.otpforwardapp.presentation.ui.viewmodels.ForwardPolicyMainViewModel
import com.soumyadip.otpforwardapp.presentation.utils.getNewForwardPolicy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [ForwardPoliciesMainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ForwardPoliciesMainFragment : Fragment() {
    private var _binding: FragmentForwardPoliciesMainBinding? = null
    private val binding get() = _binding!!
    private val forwardPolicyMainViewModel by viewModels<ForwardPolicyMainViewModel>()
    lateinit var adapter: ForwardPolicyMainListAdapter
    private var firstTimeFetch = true

    @Inject
    lateinit var gson: Gson


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentForwardPoliciesMainBinding.inflate(inflater, container, false)
        adapter = ForwardPolicyMainListAdapter(::onEditPolicyClick, ::deletePolicy, ::updatePolicy)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        observer()
    }

    private fun bind() {

        binding.addNewPolicyButton.setOnClickListener(::onAddPolicyClick)
        binding.mainPolicyListRecyclerView.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.mainPolicyListRecyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        firstTimeFetch = true
    }

    private fun observer() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                forwardPolicyMainViewModel.forwardPolicyDetailsListResult.collect { flowResult ->
                    when (flowResult) {
                        is FlowResult.Error -> {

                        }

                        is FlowResult.Loading -> {

                            toggleNotItemsVisibility(true)
                        }

                        is FlowResult.Success -> {
                            val policyList = flowResult.data
                            toggleNotItemsVisibility(policyList?.isEmpty() ?: true)
                            adapter.submitList(policyList)

                            if (firstTimeFetch) {
                                firstTimeFetch = false
                                binding.mainPolicyListRecyclerView.scrollToPosition(10)
                            }


                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun toggleNotItemsVisibility(visible: Boolean) {
        binding.policyMainNoItemsTextview.visibility = if (visible) View.VISIBLE else View.GONE
        binding.mainPolicyListRecyclerView.visibility = if (!visible) View.VISIBLE else View.GONE
    }

    private fun onEditPolicyClick(forwardPolicyDetailsItem: ForwardPolicyDetails) {
//        val action =
//            ForwardPoliciesMainFragmentDirections.actionForwardPoliciesMainToSetupForwardPolicy(
//                gson.toJson(forwardPolicyItem)
//            )
//        findNavController().navigate(action)
        val intent = Intent(context, EditPolicyActivity::class.java)
        intent.putExtra("policyItem", gson.toJson(forwardPolicyDetailsItem))
        startActivity(intent)
    }

    private fun deletePolicy(policyDetails: ForwardPolicyDetails) {
        forwardPolicyMainViewModel.deletePolicy(policyDetails)
    }

    private fun updatePolicy(policyDetails: ForwardPolicyDetails) {
        forwardPolicyMainViewModel.updatePolicy(policyDetails)
    }

    private fun onAddPolicyClick(v: View) {
        val intent = Intent(context, EditPolicyActivity::class.java)
        intent.putExtra("policyItem", gson.toJson(getNewForwardPolicy()))
        startActivity(intent)

    }


}