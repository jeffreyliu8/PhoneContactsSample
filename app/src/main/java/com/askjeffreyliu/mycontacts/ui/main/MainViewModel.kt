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

    private val _emailsLiveData = MutableLiveData<List<String>>()
    val emailsLiveData: LiveData<List<String>>
        get() = _emailsLiveData

    private val _phonesLiveData = MutableLiveData<List<String>>()
    val phonesLiveData: LiveData<List<String>>
        get() = _phonesLiveData

    fun fetchContacts() {
        viewModelScope.launch {
            _contactsLiveData.value = repository.fetchContacts()
        }
    }

    fun fetchEmails(id: String) {
        _emailsLiveData.value = emptyList()
        viewModelScope.launch {
            _emailsLiveData.value = repository.fetchEmailOfContact(id)
        }
    }

    fun fetchPhones(id: String) {
        _phonesLiveData.value = emptyList()
        viewModelScope.launch {
            _phonesLiveData.value = repository.fetchPhonesOfContact(id)
        }
    }
}