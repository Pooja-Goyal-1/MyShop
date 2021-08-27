package com.example.myshop.activities

import android.os.Bundle
import com.example.myshop.R
import com.example.myshop.utils.MSButton
import com.example.myshop.utils.MSEditText
import com.google.firebase.auth.FirebaseAuth


class ForgotPasswordActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val submitButton: MSButton = findViewById(R.id.btn_submit)
        submitButton.setOnClickListener{
            val email : String = findViewById<MSEditText>(R.id.et_email_forgot_password).text.toString().trim { it<= ' '}
            if(email.isEmpty()){
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email_id), true)
            }
            else{
                showProgressDialog(resources.getString(R.string.please_wait))
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener{ task->
                        hideProgressDialog()
                        if(task.isSuccessful){
                            //show toast msg and finish the forgot password activity to go back to the login activity
                           showErrorSnackBar("the email has been successfully sent to rest the password", false)
                            finish()
                        }
                        else{
                            showErrorSnackBar (task.exception!!.message.toString(), true)
                        }
                    }
            }
        }

    }


}