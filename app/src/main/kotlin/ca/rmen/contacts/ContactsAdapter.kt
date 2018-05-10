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
