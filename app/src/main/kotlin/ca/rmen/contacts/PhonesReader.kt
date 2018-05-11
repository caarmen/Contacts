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

import android.content.Context
import android.provider.ContactsContract
import android.support.annotation.WorkerThread
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class PhonesReader(private val contactId: Long) {

    fun readPhones(context: Context, callback: (List<Phone>) -> Unit) {
        launch(UI) { callback.invoke(async(CommonPool) { readPhones(context) }.await()) }
    }

    @WorkerThread
    private fun readPhones(context: Context): List<Phone> {
        val cursor = context.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                arrayOf(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.LABEL),
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER + " NOT NULL AND "
                        + ContactsContract.CommonDataKinds.Phone.NUMBER + " NOT NULL AND "
                        + ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                arrayOf(contactId.toString()),
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER)
        val result = mutableListOf<Phone>()
        cursor?.use {
            while (it.moveToNext()) {
                result.add(Phone(it.getString(0),
                        it.getString(1),
                        it.getInt(2),
                        it.getString(3)))
            }
        }
        return result
    }
}