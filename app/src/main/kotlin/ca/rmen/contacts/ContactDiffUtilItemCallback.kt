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