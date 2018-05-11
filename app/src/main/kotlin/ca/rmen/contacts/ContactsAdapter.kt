/*
 * Copyright 2018 Carmen Alvarez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca.rmen.contacts

import android.arch.paging.PagedListAdapter
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.rmen.contacts.databinding.ContactPhoneItemBinding

class ContactsAdapter(private val editListener: ContactEditListener,
                      private val callListener: ContactCallListener)
    : PagedListAdapter<Contact, ContactViewHolder>(ContactDiffUtilItemCallback()) {
    private var selectedContactPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.contact_item, parent,
                false))
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = getItem(position)
        holder.binding.contact = contact
        holder.binding.editListener = editListener
        holder.binding.contactPhones.removeAllViews()
        if (selectedContactPosition == position) {
            displayContactDetail(holder)
        } else {
            displayContactSummary(holder)
        }
        holder.binding.openCloseListener = object : ContactOpenCloseListener {
            override fun onOpenClose() {
                onContactOpenedOrClosed(holder.adapterPosition)
            }
        }
    }

    private fun onContactOpenedOrClosed(position: Int) {
        val previousSelectedContactPosition = selectedContactPosition
        selectedContactPosition = if (position == selectedContactPosition) {
            -1 // this item was open: now we'ell close it
        } else {
            position // this item was closed: now we'll open it
        }
        notifyItemChanged(position)
        if (previousSelectedContactPosition != -1) {
            notifyItemChanged(previousSelectedContactPosition)
        }
    }

    private fun displayContactSummary(holder: ContactViewHolder) {
        holder.binding.contactDetails.visibility = View.GONE
        holder.binding.btnOpenClose.setImageResource(R.drawable.ic_open)
    }

    private fun displayContactDetail(holder: ContactViewHolder) {
        holder.binding.contactDetails.visibility = View.VISIBLE
        holder.binding.btnOpenClose.setImageResource(R.drawable.ic_close)

        holder.binding.contact?.let {
            PhonesReader(it.id).readPhones(holder.binding.root.context, { phones ->
                val inflater = LayoutInflater.from(holder.binding.root.context)
                phones.forEach {
                    val phoneBinding = DataBindingUtil.inflate<ContactPhoneItemBinding>(
                            inflater,
                            R.layout.contact_phone_item, holder.binding.contactPhones,
                            true)
                    phoneBinding.callListener = callListener
                    phoneBinding.phone = it
                }
            })
        }
    }
}
