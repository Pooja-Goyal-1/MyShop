package com.example.myshop.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.User
import com.example.myshop.utils.*
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val login: MSButton = findViewById(R.id.btn_login)
        login.setOnClickListener{
            logInRegisteredUser()
        }

        val register : MSTextViewBold = findViewById(R.id.register)
        register.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val forgotPassword: MSTextView = findViewById(R.id.forgot_password)
        forgotPassword.setOnClickListener{
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

    }

    private fun validateLoginDetails(): Boolean {
        return when{
            TextUtils.isEmpty((findViewById<MSEditText>(R.id.et_email)).text.toString().trim {it<=' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email_id), true)
                false
            }
            TextUtils.isEmpty((findViewById<MSEditText>(R.id.et_password)).text.toString().trim {it<=' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun logInRegisteredUser(){
        if(validateLoginDetails()){
            //show the progress dialog.\
            showProgressDialog(resources.getString(R.string.please_wait))

            //Get the text from edit text and trim the space
            val email = findViewById<MSEditText>(R.id.et_email).text.toString().trim{ it <= ' '}
            val password = findViewById<MSEditText>(R.id.et_password).text.toString().trim { it <= ' '}

            //Login using Firebase Auth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{ task->

                    if(task.isSuccessful){
                        FirestoreClass().getUserDetails(this@LoginActivity)
                    }
                    else{
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

    fun userLoggedInSuccess(user: User) {
        //hide the progress dialog
        hideProgressDialog()

        //Print the user details in log
        Log.i("First Name:", user.firstName)
        Log.i("Last Name:", user.lastName)
        Log.i("Email:", user.email)

        if(user.profileCompleted==0){
            // if the profile of user is incomplete then launch the userProfileActivity
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)
        }
        else{
            // if profile is completed , launch the main activity.
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
        finish()
    }
}

