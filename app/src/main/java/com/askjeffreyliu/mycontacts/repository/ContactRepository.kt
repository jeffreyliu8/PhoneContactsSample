package com.askjeffreyliu.mycontacts.repository

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Email
import com.askjeffreyliu.mycontacts.model.MyContact
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ContactRepository(private val context: Context) {

    private val contacts = mutableListOf<MyContact>()

    suspend fun fetchContacts(): List<MyContact> = withContext(Dispatchers.IO) {
        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        cursor?.let {
            if ((it.count) > 0) {
                while (it.moveToNext()) {
                    val id =
                        it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))
                    val name =
                        it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val phoneNo =
                        it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

//                    val emailsCursor = context.contentResolver.query(
//                        Email.CONTENT_URI,
//                        null,
//                        Email.CONTACT_ID + " = " + id,
//                        null,
//                        null
//                    )
//                    var email: String? = null
//                    emailsCursor?.let { ec ->
//                        while (emailsCursor.moveToNext()) {
//                            email = ec.getString(ec.getColumnIndex(Email.DATA))
//                            break
//                        }
//                    }
//                    emailsCursor?.close()

                    val photoUri =
                        it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
                    Logger.d("contact", "getAllContacts: $name $phoneNo $photoUri")

                    contacts.add(
                        MyContact(
                            id = id,
                            name = name,
                            phone = phoneNo,
//                            email = email,
                            photo = photoUri
                        )
                    )
                }
            }
        }

        cursor?.close()
        contacts
    }
}