package com.soumyadip.otpforwardapp.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.soumyadip.otpforwardapp.databinding.EditPolicyContactListItemBinding
import com.soumyadip.otpforwardapp.domain.dto.PhoneContact
import com.soumyadip.otpforwardapp.presentation.ui.adapters.ContactPickerListAdapter.ContactComparatorDiffUtil

class EditPolicyContactListAdapter
    :
    ListAdapter<PhoneContact, EditPolicyContactListAdapter.EditPolicyPhoneContactViewHolder>(
        ContactComparatorDiffUtil()
    ) {


    inner class EditPolicyPhoneContactViewHolder(
        private val binding
        : EditPolicyContactListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(phoneContact: PhoneContact) {
            binding.contactDisplayName.text = phoneContact.displayName
            binding.contactMobileNumber.text = phoneContact.phoneNumber
        }
    }

    override fun onBindViewHolder(holder: EditPolicyPhoneContactViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EditPolicyPhoneContactViewHolder {
        val binding = EditPolicyContactListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return EditPolicyPhoneContactViewHolder(binding)

    }

}