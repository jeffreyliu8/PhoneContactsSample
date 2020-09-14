package com.askjeffreyliu.mycontacts.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.askjeffreyliu.mycontacts.extension.setLoading
import com.askjeffreyliu.mycontacts.extension.setSuccess
import com.askjeffreyliu.mycontacts.model.MyContact
import com.askjeffreyliu.mycontacts.model.Resource
import com.askjeffreyliu.mycontacts.repository.ContactRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ContactRepository(application.applicationContext)

    private val _contactsLiveData = MutableLiveData<Resource<List<MyContact>>>()
    val contactsLiveData: LiveData<Resource<List<MyContact>>>
        get() = _contactsLiveData

    private val _emailsLiveData = MutableLiveData<Resource<List<String>>>()
    val emailsLiveData: LiveData<Resource<List<String>>>
        get() = _emailsLiveData

    private val _phonesLiveData = MutableLiveData<Resource<List<String>>>()
    val phonesLiveData: LiveData<Resource<List<String>>>
        get() = _phonesLiveData

    fun fetchContacts() {
        _contactsLiveData.setLoading()
        viewModelScope.launch {
            _contactsLiveData.setSuccess(repository.fetchContacts())
        }
    }

    fun fetchEmails(id: String) {
        _emailsLiveData.setLoading()
        viewModelScope.launch {
            _emailsLiveData.setSuccess(repository.fetchEmailOfContact(id))
        }
    }

    fun fetchPhones(id: String) {
        _phonesLiveData.setLoading()
        viewModelScope.launch {
            _phonesLiveData.setSuccess(repository.fetchPhonesOfContact(id))
        }
    }
}