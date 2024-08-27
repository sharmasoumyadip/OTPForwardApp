package com.soumyadip.otpforwardapp.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.soumyadip.otpforwardapp.databinding.EditPolicyFilterListItemBinding
import com.soumyadip.otpforwardapp.domain.dto.Filter

class EditPolicyFilterListAdapter :
    ListAdapter<Filter, EditPolicyFilterListAdapter.EditPolicyFiltersViewHolder>(
        FiltersComparatorDiffUtil()
    ) {
    class FiltersComparatorDiffUtil : DiffUtil.ItemCallback<Filter>() {
        override fun areContentsTheSame(
            oldItem: Filter,
            newItem: Filter
        ): Boolean {
            return oldItem.filterId == newItem.filterId
        }

        override fun areItemsTheSame(oldItem: Filter, newItem: Filter): Boolean {
            return newItem == oldItem
        }

    }

    inner class EditPolicyFiltersViewHolder(private val binding: EditPolicyFilterListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(filter: Filter) {
            binding.filterItemFilterField.text = filter.filterField.stringValue
            binding.filterItemFilterOperator.text = filter.filterOperator.stringValue
            binding.filterItemFilterValue.text = filter.filterValue
        }
    }

    override fun onBindViewHolder(holder: EditPolicyFiltersViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditPolicyFiltersViewHolder {
        val binding = EditPolicyFilterListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EditPolicyFiltersViewHolder(binding)
    }


}