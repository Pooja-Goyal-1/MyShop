package com.example.myshop.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myshop.R
import com.example.myshop.utils.MSTextView
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

    private lateinit var mProgressDialog:Dialog

    fun showErrorSnackBar(message: String,errorMessage: Boolean ){
        val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if(errorMessage){
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this, R.color.colorSnackBarError)
            )

        }
        else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this, R.color.colorSnackBarSuccess
                )
            )
        }
        snackBar.show()
    }

    fun showProgressDialog(text: String){
        mProgressDialog = Dialog(this)

        //set the screen content from layout resource.
        //the resource will be inflated, adding all top-level views to the screen
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.findViewById<MSTextView>(R.id.progress_text).text = text
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        //satrt the dialog and display it on screen
        mProgressDialog.show()

    }

    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }
}