package com.soumyadip.otpforwardapp.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.soumyadip.otpforwardapp.databinding.AddListItemBinding
import com.soumyadip.otpforwardapp.databinding.ConfigureFilterConditionListItemBinding
import com.soumyadip.otpforwardapp.domain.dto.Filter
import com.soumyadip.otpforwardapp.domain.dto.FilterField
import com.soumyadip.otpforwardapp.domain.dto.FilterOperator

class ConfigureFilterListAdapter
    (
    val onEditTextClickListener: (String, (String) -> Unit) -> Unit,
    val addPolicyClickClickListener: () -> Unit,
    val onDeleteButtonClickLister: (Int) -> Unit
) :
    ListAdapter<Filter, RecyclerView.ViewHolder>(FilterComparatorDiffUtil()) {


    private val ITEM = 0
    private val ADD_ITEM = 1
    private val filterFieldNameList = FilterField.entries.map { it.name }
    private val filterOperatorNameList = FilterOperator.entries.map { it.name }

    class FilterComparatorDiffUtil : DiffUtil.ItemCallback<Filter>() {

        override fun areContentsTheSame(
            oldItem: Filter,
            newItem: Filter
        ): Boolean {
            return oldItem.filterId == newItem.filterId
        }

        override fun areItemsTheSame(oldItem: Filter, newItem: Filter): Boolean {
            return oldItem == newItem
        }

    }

    private inner class FilterConditionViewHolder(val binding: ConfigureFilterConditionListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val filterFieldDropDown = binding.configureFilterFieldMenu
        val filterOperatorDropdown = binding.configureFilterOperatorMenu
        val filterValueEditText = binding.configureFilterEditText
        val deleteButton = binding.configureFilterDelButton
        private var currPositionInList: Int = 0
        private lateinit var currFilter: Filter

        val filterFieldAdapter = ArrayAdapter(
            binding.root.context,
            android.R.layout.simple_spinner_dropdown_item,
            filterFieldNameList
        )
        val filterOperatorAdapter = ArrayAdapter(
            binding.root.context,
            android.R.layout.simple_spinner_dropdown_item,
            filterOperatorNameList
        )

        private fun updateFilterValue(value: String) {
            currFilter.filterValue = value.uppercase()
            filterValueEditText.setText(value.uppercase())

        }

        init {
            filterFieldDropDown.adapter = filterFieldAdapter
            filterOperatorDropdown.adapter = filterOperatorAdapter
            filterValueEditText.setOnClickListener {
                onEditTextClickListener(currFilter.filterValue, ::updateFilterValue)
            }
            deleteButton.setOnClickListener {
                onDeleteButtonClickLister(currPositionInList)
            }
            filterFieldDropDown.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if (parent != null)
                            currFilter.filterField =
                                FilterField.valueOf(parent.getItemAtPosition(position).toString())
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }

                }


            filterOperatorDropdown.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if (parent != null)
                            currFilter.filterOperator = FilterOperator.valueOf(
                                parent.getItemAtPosition(position).toString()
                            )

                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }


        }

        fun bind(filter: Filter, position: Int) {
            currPositionInList = position
            currFilter = filter
            filterFieldDropDown.setSelection(filter.filterField.ordinal)
            filterOperatorDropdown.setSelection(filter.filterOperator.ordinal)
            filterValueEditText.setText(filter.filterValue)


        }

    }

    inner class AddFilterViewHolder(binding: AddListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.addNewPolicyButton.setOnClickListener { addPolicyClickClickListener() }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) ADD_ITEM else ITEM
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FilterConditionViewHolder) holder.bind(getItem(position), position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == ITEM) {
            val binding = ConfigureFilterConditionListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            FilterConditionViewHolder(binding)
        } else {
            val binding =
                AddListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            AddFilterViewHolder(binding)
        }

    }

}


