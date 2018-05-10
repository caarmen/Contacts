package ca.rmen.contacts

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList

class ContactsViewModel(application: Application) : AndroidViewModel(application) {

    val pagedList: LiveData<PagedList<Contact>>

    init {
        val pagingConfig = PagedList.Config.Builder()
                .setPageSize(50)
                .setPrefetchDistance(150)
                .setEnablePlaceholders(true)
                .build()

        val dataSourceFactory = ContactsDataSourceFactory(application)

        pagedList = LivePagedListBuilder(dataSourceFactory, pagingConfig)
                .build()
    }
}

