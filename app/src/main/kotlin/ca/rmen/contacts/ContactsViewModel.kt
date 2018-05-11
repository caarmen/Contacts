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

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract

class ContactsViewModel(application: Application) : AndroidViewModel(application),
        ContactEditListener, ContactCallListener {
    val pagedList: LiveData<PagedList<Contact>>

    init {
        val pagingConfig = PagedList.Config.Builder()
                .setPageSize(30)
                .setPrefetchDistance(60)
                .setEnablePlaceholders(true)
                .build()

        val dataSourceFactory = ContactsDataSourceFactory(application)

        pagedList = LivePagedListBuilder(dataSourceFactory, pagingConfig)
                .build()
    }

    override fun onEdit(contact: Contact) {
        val uri = ContactsContract.Contacts.getLookupUri(contact.id, contact.lookupKey)
        val editIntent = Intent(Intent.ACTION_EDIT)
                .setDataAndType(uri, ContactsContract.Contacts.CONTENT_ITEM_TYPE)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        getApplication<Application>().startActivity(editIntent)
    }

    override fun onCall(phone: Phone) {
        val uri = Uri.parse("tel:" + Uri.encode(phone.normalized))
        val dialIntent = Intent(Intent.ACTION_DIAL, uri)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        getApplication<Application>().startActivity(dialIntent)
    }
}

