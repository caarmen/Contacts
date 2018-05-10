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
import android.view.ViewGroup
import ca.rmen.contacts.databinding.ContactItemBinding

class ContactsAdapter : PagedListAdapter<Contact, ContactViewHolder>(ContactDiffUtilItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = DataBindingUtil.inflate<ContactItemBinding>(LayoutInflater.from(parent.context), R.layout.contact_item, parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = getItem(position)
        if (contact == null) {
            holder.binding.contactDisplayName.text = holder.binding.root.context.getString(R.string.placeholder)
        } else {
            holder.binding.contactDisplayName.text = contact.displayName
        }
    }
}
