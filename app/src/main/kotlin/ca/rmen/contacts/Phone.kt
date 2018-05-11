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
import android.support.annotation.StringRes

data class Phone(val normalized: String, val entered: String, val type: Int, val label: String?) {
    fun concatLabelAndType(context: Context): String? {
        @StringRes val typeResId = when (type) {
            ContactsContract.CommonDataKinds.Phone.TYPE_HOME -> R.string.phone_type_home
            ContactsContract.CommonDataKinds.Phone.TYPE_WORK -> R.string.phone_type_work
            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> R.string.phone_type_mobile
            else -> 0
        }
        if (typeResId != 0 && label?.isNotEmpty() == true) {
            return context.getString(typeResId) + " - " + label
        }
        if (typeResId != 0) return context.getString(typeResId)

        return label
    }
}

