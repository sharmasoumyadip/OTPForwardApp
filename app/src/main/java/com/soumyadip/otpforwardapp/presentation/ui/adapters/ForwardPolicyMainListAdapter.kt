package com.soumyadip.otpforwardapp.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.soumyadip.otpforwardapp.databinding.FooterBinding
import com.soumyadip.otpforwardapp.databinding.PolicyListItemBinding
import com.soumyadip.otpforwardapp.domain.dto.ForwardPolicyDetails

class ForwardPolicyMainListAdapter(
    private val onEditPolicyClick: (ForwardPolicyDetails) -> Unit,
    private val onDeletePolicyClick: (ForwardPolicyDetails) -> Unit,
    private val onSwitchChangeListener: (ForwardPolicyDetails) -> Unit
) :
    ListAdapter<ForwardPolicyDetails, RecyclerView.ViewHolder>(PolicyComparatorDiffUtil()) {
    private val FOOTER = 1
    private val ITEM = 0

    inner class PolicyViewHolder(private val binding: PolicyListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(forwardPolicyDetails: ForwardPolicyDetails) {
            binding.policyActiveSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                forwardPolicyDetails.isActive = isChecked
                onSwitchChangeListener(forwardPolicyDetails)
            }
            binding.editPolicyButton.setOnClickListener {
                onEditPolicyClick(forwardPolicyDetails)
            }
            binding.deletePolicyButton.setOnClickListener {
                onDeletePolicyClick(forwardPolicyDetails)
            }
            binding.policyTitleMain.text = forwardPolicyDetails.policyName
            binding.policyActiveSwitch.isChecked = forwardPolicyDetails.isActive


        }
    }

    inner class FooterViewHolder(private val binding: FooterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) FOOTER else ITEM
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == FOOTER) return FooterViewHolder(
            FooterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
        val binding =
            PolicyListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PolicyViewHolder(binding)


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PolicyViewHolder) holder.bind(getItem(position))
    }

    class PolicyComparatorDiffUtil : DiffUtil.ItemCallback<ForwardPolicyDetails>() {
        override fun areItemsTheSame(
            oldItem: ForwardPolicyDetails,
            newItem: ForwardPolicyDetails
        ): Boolean {
            return oldItem.policyId == newItem.policyId
        }

        override fun areContentsTheSame(
            oldItem: ForwardPolicyDetails,
            newItem: ForwardPolicyDetails
        ): Boolean {
            return oldItem == newItem
        }

    }
}

