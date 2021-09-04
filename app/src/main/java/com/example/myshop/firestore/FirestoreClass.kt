package com.example.myshop.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.myshop.activities.LoginActivity
import com.example.myshop.activities.RegisterActivity
import com.example.myshop.models.User
import com.example.myshop.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {
    private val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser (activity: RegisterActivity, userInfo: User) {
        //The 'users' is collection name. If the collection is already created, then other collection will not be craeted.
        mFirestore.collection(Constants.USERS)
        //Document ID for users fields. Here the document it is the user Id.
            .document(userInfo.id)
            //here userInfo are the field and set option is set to merge. It is for if we want to merge later
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                // call the function of base Activity for transfering the result to it.
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user",
                    e
                )
            }
    }

    // function to get loggedIn user ID
    fun getCurrentUserID() : String {
        // an instance of currentUser using Firebase Auth
        val currentUser = FirebaseAuth.getInstance().currentUser
        // variable to assign the currentUserId if its not null
        var currentUserID = ""
        if(currentUser!=null){
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun getUserDetails(activity: Activity){
       //pass the collection name from which we wants the data
        mFirestore.collection(Constants.USERS)
        //The document ID to get the fields of users
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())

                // here we have received the document snapshot which is converted into the User Data Model object.
                val user = document.toObject(User::class.java)!!


                // shared preferences is used to store the data on the device to avoid using firestore every time.
                val sharedPreferences = activity.getSharedPreferences(
                    Constants.MYSHOP_PREFERENCES,
                    Context.MODE_PRIVATE// private mode ensures that the data is available only inside your application.
                )

                // to save data from firestore, we need to have an editor.
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    // key: value = LOGGED_IN_USERNAME: FirstName LastName
                    Constants.LOGGED_IN_USERNAME,
                    "${user.firstName} ${user.lastName}"
                )

                when(activity) {
                    is LoginActivity -> {
                        //call a function of base activity for transfering the result to it.
                        activity.userLoggedInSuccess(user)
                    }
                }

            }
            .addOnFailureListener { e->
                //hide the progress dialog if there is any error.and print error in the log
                when(activity){
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting  the user details",
                    e
                )

            }
    }
}


