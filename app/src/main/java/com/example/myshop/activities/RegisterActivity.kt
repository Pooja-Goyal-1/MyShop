package com.example.myshop.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.CheckBox
import com.example.myshop.R
import com.example.myshop.utils.MSButton
import com.example.myshop.utils.MSEditText
import com.example.myshop.utils.MSTextViewBold
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)



        val login: MSTextViewBold = findViewById(R.id.login)
        login.setOnClickListener{

            //launch the register screen when the user clicks on the text
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<MSButton>(R.id.btn_register).setOnClickListener{
            registerUser()

        }
    }

    //function to validate the entries of a new user.
    private fun validateRegisterDetails(): Boolean {
        return when{
            TextUtils.isEmpty (findViewById<MSEditText>(R.id.et_first_name).text.toString().trim { it <= ' ' })->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }
            TextUtils.isEmpty (findViewById<MSEditText>(R.id.et_last_name).text.toString().trim { it <= ' ' })->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }
            TextUtils.isEmpty (findViewById<MSEditText>(R.id.et_email).text.toString().trim { it <= ' ' })->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email_id), true)
                false
            }
            TextUtils.isEmpty (findViewById<MSEditText>(R.id.et_password).text.toString().trim { it <= ' ' })->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            TextUtils.isEmpty (findViewById<MSEditText>(R.id.et_confirm_password).text.toString().trim { it <= ' ' })->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_password), true)
                false
            }
            findViewById<MSEditText>(R.id.et_password).text.toString().trim { it <= ' '}!=findViewById<MSEditText>(R.id.et_confirm_password).text.toString().trim { it <= ' '} -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch), true)
                false
            }
            !findViewById<CheckBox>(R.id.cb_terms_and_condition).isChecked -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_agree_terms_and_condition), true)
                false
            }
            else -> {
               //  showErrorSnackBar("Your details are valid", false)
                true
            }
        }
    }

    // Registering the user
    private fun registerUser() {
        //check with validate function if the entries are valid or not
        if(validateRegisterDetails()) {

            //show the progress Dialog
            showProgressDialog(resources.getString(R.string.please_wait))

            val email: String = findViewById<MSEditText>(R.id.et_email).text.toString().trim{ it <= ' ' }
            val password: String = findViewById<MSEditText>(R.id.et_password).text.toString().trim { it <= ' '}

            //create an instance and registering user using email and password.
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener <AuthResult>{ task ->

                        //hide progress bar
                        hideProgressDialog()

                        //if registration is successful
                        if(task.isSuccessful){
                            //Firebase registered User
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            showErrorSnackBar(
                                "You are registered successfully. Your user id is ${firebaseUser.uid}",
                                false
                            )

                            //close the register screen and take to the login screen
                            FirebaseAuth.getInstance().signOut()
                            finish()
                        }
                        else
                        {
                            // if user is not registered successfully then show error message.
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
                )
        }
    }


}
