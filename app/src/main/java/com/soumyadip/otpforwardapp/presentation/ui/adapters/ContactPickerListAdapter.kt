package com.soumyadip.otpforwardapp.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.soumyadip.otpforwardapp.databinding.ContactPickerListItemBinding
import com.soumyadip.otpforwardapp.domain.dto.PhoneContact

class ContactPickerListAdapter :
    ListAdapter<PhoneContact, ContactPickerListAdapter.ContactPickerViewHolder>(
        ContactComparatorDiffUtil()
    ) {
    inner class ContactPickerViewHolder(private val binding: ContactPickerListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(phoneContact: PhoneContact) {
            binding.contactPickerListItemCheckbox.setOnCheckedChangeListener { _, isChecked ->
                phoneContact.isSelected = isChecked
            }
            binding.contactPickerListItemCheckbox.isChecked = phoneContact.isSelected
            binding.contactPickerListItemName.text = phoneContact.displayName ?: ""
            binding.contactPickerListItemMobile.text = phoneContact.phoneNumber


        }
    }

    class ContactComparatorDiffUtil : DiffUtil.ItemCallback<PhoneContact>() {
        override fun areItemsTheSame(oldItem: PhoneContact, newItem: PhoneContact): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PhoneContact, newItem: PhoneContact): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactPickerViewHolder {
        val binding = ContactPickerListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ContactPickerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactPickerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}