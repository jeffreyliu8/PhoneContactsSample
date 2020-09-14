package com.askjeffreyliu.mycontacts.model


data class MyContact(
    val id: String,
    val name: String?,
    val phone: String?,
//    val email: String?,
    val photo: String?,
    val star: Boolean
)