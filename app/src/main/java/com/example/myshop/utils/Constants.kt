package com.example.myshop.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    const val USERS: String = "users"
    const val MYSHOP_PREFERENCES: String = "MyShopPreferences"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val EXTRA_USER_DETAILS: String = "extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE =2 // any number can be chosen
    const val PICK_IMAGE_REQUEST_CODE = 1

    const val MALE: String = "male"
    const val FEMALE: String = "female"
    const val FIRST_NAME = "firstName"
    const val LAST_NAME = "lastName"
    const val GENDER: String = "gender"
    const val MOBILE: String = "mobile"
    const val IMAGE: String = "image"
    const val COMPLETE_PROFILE: String = "profileCompleted"

    const val USER_PROFILE_IMAGE: String = "user_profile_image"

    fun showImageChooser(activity: Activity) {
        // an intent for launching the image selection of phone storage.

        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        //launches the image selection of phone storage using the constant code.
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        /*
        MimeTypeMap : Two-way map that maps MINE-types to file extensions and vice-versa.
        getSingleton(): Get the singleton instance of MimeTypeMap
        getExtensionsFromMimeType: Return the registered extension for the given MIME type.
        contentResolver.getType: Return the MIME type of the given content URL
         */
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}