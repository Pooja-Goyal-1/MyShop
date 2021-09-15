package com.example.myshop.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.User
import com.example.myshop.utils.Constants
import com.example.myshop.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        btn_logout.setOnClickListener(this@SettingsActivity)
        edit.setOnClickListener(this@SettingsActivity)

    }

    private fun getUserDetails(){
        showProgressDialog(resources.getString(R.string.please_wait))
        // calling function from  firestore class to get user details.
        FirestoreClass().getUserDetails(this)

    }

    fun userDetailsSuccess(user: User) {

        mUserDetails = user

        hideProgressDialog()
        GlideLoader(this@SettingsActivity).loadUserPicture(user.image, user_photo)

        name.text = "${user.firstName} ${user.lastName}"
        email.text = user.email
        mobile.text = "${user.mobile}"
        gender.text = user.gender
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    override fun onClick(v: View?) {
        if(v!=null){
            when(v.id){
                R.id.edit -> {
                    val intent = Intent(this@SettingsActivity, UserProfileActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
                    startActivity(intent)
                }
                R.id.btn_logout -> {
                    //signing out
                    FirebaseAuth.getInstance().signOut()

                    val intent = Intent(this@SettingsActivity, LoginActivity::class.java )
                    // ensures that all layers of activities are finished.
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }

            }
        }

    }
}