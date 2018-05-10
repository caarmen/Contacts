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

import android.arch.paging.PositionalDataSource
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import android.provider.ContactsContract
import android.util.Log

class ContactsDataSource(context: Context) : PositionalDataSource<Contact>() {

    private val TAG = Constants.TAG + ContactsDataSource::class.java.simpleName

    private val contentResolver: ContentResolver = context.contentResolver
    private var cursor: Cursor? = null

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Contact>) {
        val contacts = readContacts(params.requestedStartPosition, params.requestedLoadSize)
        callback.onResult(contacts, params.requestedStartPosition, cursor?.count ?: 0)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Contact>) {
        val contacts = readContacts(params.startPosition, params.loadSize)
        callback.onResult(contacts)
    }

    override fun invalidate() {
        super.invalidate()
        cursor?.close()
    }

    private fun readContacts(startPosition: Int, loadSize: Int): List<Contact> {
        Log.v(TAG, "readContacts: startPosition=${startPosition}, loadSize=${loadSize}")
        if (cursor == null) {
            cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                    arrayOf(BaseColumns._ID, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY),
                    null, null, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
            Log.v(TAG, "${cursor?.count} items")
        }
        val contacts = mutableListOf<Contact>()
        cursor?.let {
            for (i in startPosition until (loadSize + startPosition)) {
                if (it.moveToPosition(i)) {
                    contacts.add(Contact(it.getLong(0), i.toString() + " " + it.getString(1)))
                }
            }
        }
        return contacts
    }
}