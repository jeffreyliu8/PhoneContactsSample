package com.askjeffreyliu.mycontacts.repository

import android.content.Context
import android.provider.ContactsContract
import com.askjeffreyliu.mycontacts.model.MyContact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ContactRepository(private val context: Context) {

    private val contacts = mutableListOf<MyContact>()

    suspend fun fetchContacts(): List<MyContact> = withContext(Dispatchers.IO) {
        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.STARRED,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.PHOTO_URI
        )

        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
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

                    val isStar =
                        it.getInt(it.getColumnIndex(ContactsContract.Contacts.STARRED)) == 1


                    contacts.add(
                        MyContact(
                            id = id,
                            name = name,
                            phone = phoneNo,
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
}