package com.askjeffreyliu.mycontacts.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.askjeffreyliu.mycontacts.model.MyContact
import com.askjeffreyliu.mycontacts.repository.ContactRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ContactRepository(application.applicationContext)

    private val _contactsLiveData = MutableLiveData<List<MyContact>>()
    val contactsLiveData: LiveData<List<MyContact>>
        get() = _contactsLiveData

    fun fetchContacts() {
        viewModelScope.launch {
            _contactsLiveData.value = repository.fetchContacts()
        }
    }
}