package com.example.myshop.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myshop.R
import com.example.myshop.utils.Constants
import com.example.myshop.utils.MSTextViewBold

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // to use the shared preferences data we stored
        //similar to as written in FireStore class
        val sharedPreferences = getSharedPreferences(Constants.MYSHOP_PREFERENCES, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "")!!

        findViewById<MSTextViewBold>(R.id.main_activity).text = "Hello ${username}"
    }
}
