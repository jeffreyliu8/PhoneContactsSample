package com.askjeffreyliu.mycontacts.repository

import android.content.Context
import android.provider.ContactsContract
import com.askjeffreyliu.mycontacts.model.MyContact
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ContactRepository(private val context: Context) {

    suspend fun fetchContacts(): List<MyContact> = withContext(Dispatchers.IO) {
        val contacts = mutableListOf<MyContact>()
        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.STARRED,

            ContactsContract.Contacts.PHOTO_URI,
//            ContactsContract.CommonDataKinds.Phone.NUMBER,
//            ContactsContract.CommonDataKinds.Phone.PHOTO_URI
        )

        val cursor = context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            projection, null, null,
            ContactsContract.Contacts.STARRED + " DESC"
        )
        cursor?.let {
            if ((it.count) > 0) {
                while (it.moveToNext()) {
                    val id =
                        it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))
                    val name =
                        it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
//                    val phoneNo =
//                        it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

//                    val phonesCursor = context.contentResolver.query(
//                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                        null,
//                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
//                        null,
//                        null
//                    )
//                    var phone: String? = null
//                    phonesCursor?.let { phoneC ->
//                        while (phonesCursor.moveToNext()) {
//                            phone = phoneC.getString(phoneC.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
//                            break
//                        }
//                    }
//                    phonesCursor?.close()
//
//                    val emailsCursor = context.contentResolver.query(
//                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
//                        null,
//                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + id,
//                        null,
//                        null
//                    )
//                    var email: String? = null
//                    emailsCursor?.let { ec ->
//                        while (emailsCursor.moveToNext()) {
//                            email = ec.getString(ec.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
//                            break
//                        }
//                    }
//                    emailsCursor?.close()

                    val photoUri =
                        it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))

                    val isStar =
                        it.getInt(it.getColumnIndex(ContactsContract.Contacts.STARRED)) == 1

                    contacts.add(
                        MyContact(
                            id = id,
                            name = name,
//                            phone = phone,
//                            email = email,
                            photo = photoUri,
                            star = isStar
                        )
                    )
                }
            }
        }

        cursor?.close()
        contacts
    }

    suspend fun fetchEmailOfContact(id: String): List<String> = withContext(Dispatchers.IO) {
        val emails = mutableListOf<String>()
        val projection = arrayOf(ContactsContract.CommonDataKinds.Email.ADDRESS)

        val emailsCursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            projection,
            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + id,
            null,
            null
        )

        emailsCursor?.let { ec ->
            while (emailsCursor.moveToNext()) {
                val e =
                    ec.getString(ec.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS))
                Logger.d(e)
                emails.add(e)
            }
        }
        emailsCursor?.close()

        emails
    }

    suspend fun fetchPhonesOfContact(id: String): List<String> = withContext(Dispatchers.IO) {
        val phones = mutableListOf<String>()

        val phonesCursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
            null,
            null
        )

        phonesCursor?.let { phoneC ->
            while (phonesCursor.moveToNext()) {
                phones.add(phoneC.getString(phoneC.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)))
            }
        }
        phonesCursor?.close()

        phones
    }
}