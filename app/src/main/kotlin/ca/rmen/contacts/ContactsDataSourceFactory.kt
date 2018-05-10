package ca.rmen.contacts

import android.app.Application
import android.arch.paging.DataSource

class ContactsDataSourceFactory(private val application: Application) : DataSource.Factory<Integer, Contact>() {
    override fun create(): DataSource<Integer, Contact> {
        @Suppress("UNCHECKED_CAST")
        return ContactsDataSource(application) as DataSource<Integer, Contact>
    }
}
