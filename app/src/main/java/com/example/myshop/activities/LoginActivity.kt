package com.example.myshop.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.example.myshop.R
import com.example.myshop.utils.MSButton
import com.example.myshop.utils.MSEditText
import com.example.myshop.utils.MSTextView
import com.example.myshop.utils.MSTextViewBold
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
                .addOnCompleteListener{
                    task-> hideProgressDialog() // hiding progress bar

                    if(task.isSuccessful){
                        showErrorSnackBar("You are logged in Successfully", false)
                    }
                    else{
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }
}

