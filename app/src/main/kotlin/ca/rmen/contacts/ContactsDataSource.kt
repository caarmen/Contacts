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
import android.database.ContentObserver
import android.database.Cursor
import android.os.Handler
import android.os.Looper
import android.provider.BaseColumns
import android.provider.ContactsContract
import android.util.Log

class ContactsDataSource(context: Context) : PositionalDataSource<Contact>() {

    companion object {
        private val TAG = Constants.TAG + ContactsDataSource::class.java.simpleName
        private val CONTACTS_URI = ContactsContract.Contacts.CONTENT_URI
    }

    private val contentResolver: ContentResolver = context.contentResolver
    private var invalidatedCallbackCount = 0

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Contact>) {
        readContacts()?.let {
            callback.onResult(cursorToList(it, params.requestedStartPosition, params.requestedLoadSize),
                    params.requestedStartPosition, it.count)
            it.close()
        }
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Contact>) {
        readContacts()?.let {
            callback.onResult(cursorToList(it, params.startPosition, params.loadSize))
            it.close()
        }
    }

    override fun addInvalidatedCallback(onInvalidatedCallback: InvalidatedCallback) {
        super.addInvalidatedCallback(onInvalidatedCallback)
        if (invalidatedCallbackCount == 0) {
            contentResolver.registerContentObserver(CONTACTS_URI, false, contentObserver)
        }
        invalidatedCallbackCount++
    }

    override fun removeInvalidatedCallback(onInvalidatedCallback: InvalidatedCallback) {
        invalidatedCallbackCount--
        if (invalidatedCallbackCount == 0) {
            contentResolver.unregisterContentObserver(contentObserver)
        }
        super.removeInvalidatedCallback(onInvalidatedCallback)
    }

    private fun readContacts(): Cursor? {
        return contentResolver.query(CONTACTS_URI,
                arrayOf(BaseColumns._ID, ContactsContract.Contacts.LOOKUP_KEY, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY),
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " NOT NULL",
                null, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
    }

    private fun cursorToList(cursor: Cursor, startPosition: Int, loadSize: Int): List<Contact> {
        Log.v(TAG, "readContacts: startPosition=$startPosition, loadSize=$loadSize: ${cursor.count} total items")
        val contacts = mutableListOf<Contact>()
        for (i in startPosition until (loadSize + startPosition)) {
            if (cursor.moveToPosition(i)) {
                val contactId = cursor.getLong(0)
                val contactLookupKey = cursor.getString(1)
                val contactDisplayName = cursor.getString(2)
                contacts.add(Contact(contactId, contactLookupKey, contactDisplayName))
            }
        }
        return contacts
    }

    private val contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean) {
            Log.v(TAG, "change to $CONTACTS_URI")
            invalidate()
        }
    }
}