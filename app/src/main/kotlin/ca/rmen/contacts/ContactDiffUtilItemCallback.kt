package ca.rmen.contacts

import android.support.v7.util.DiffUtil

class ContactDiffUtilItemCallback : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact?, newItem: Contact?): Boolean {
        if (oldItem != null && newItem != null) {
            return oldItem.id == newItem.id
        }
        return oldItem == null && newItem == null
    }

    override fun areContentsTheSame(oldItem: Contact?, newItem: Contact?): Boolean {
        if (oldItem != null && newItem != null) {
            return oldItem == newItem
        }
        return oldItem == null && newItem == null
    }
}