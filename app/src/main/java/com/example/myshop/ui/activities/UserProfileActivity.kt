package com.example.myshop.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.User
import com.example.myshop.utils.Constants
import com.example.myshop.utils.GlideLoader
import com.example.myshop.utils.MSButton
import com.example.myshop.utils.MSEditText
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.activity_user_profile.user_photo
import java.io.IOException

class UserProfileActivity : BaseActivity(), View.OnClickListener{

    private lateinit var mUserDetails: User
    private var mSelectedImageFileUri: Uri? = null
    private var mUserProfileImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        // retrieving the information send using putExtra to this activity.\


        if(intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
            // get the user dtails from the intent as a parcelableExtra
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        if(mUserDetails.profileCompleted == 0){
            title_profile.text = resources.getString(R.string.title_complete_profile)
            et_first_name.isEnabled = false
            et_first_name.setText(mUserDetails.firstName)

            et_last_name.isEnabled = false   // this field can't be changed.
            et_last_name.setText(mUserDetails.lastName) // setting its value.

            et_email.isEnabled = false   // this field can't be changed.
            et_email.setText(mUserDetails.email) // setting its value.
        }else{
            title_profile.text = resources.getString(R.string.title_edit_profile)
            GlideLoader(this@UserProfileActivity).loadUserPicture(mUserDetails.image, user_photo)
            et_first_name.setText(mUserDetails.firstName)
            et_last_name.setText(mUserDetails.lastName)

            et_email.isEnabled = false   // this field can't be changed.
            et_email.setText(mUserDetails.email) // setting its value.

            if(mUserDetails.mobile != 0L) {
                et_mobile_number.setText(mUserDetails.mobile.toString())
            }
            if(mUserDetails.gender==Constants.MALE){
                rb_male.isChecked = true
            }else{
                rb_female.isChecked = true
            }
        }



        findViewById<ImageView>(R.id.user_photo).setOnClickListener(this@UserProfileActivity)

        findViewById<MSButton>(R.id.btn_submit).setOnClickListener(this@UserProfileActivity)

    }

    override fun onClick(v: View?) {
        if(v!=null){
            when(v.id){

                R.id.user_photo -> {
                    //checking if the app have permission to access external storage.
                    //if not asking for the permissions.
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        Constants.showImageChooser(this)
                    }else{
                        //Requests permissions to be granted to this application.
                            //these must be requested in your manifest, they should not be granted to your app, and they should have protection level.
                        ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE)
                    }
                }

                R.id.btn_submit -> {



                    if(validateUserProfileDetails()){


                        showProgressDialog(resources.getString(R.string.please_wait))

                        if(mSelectedImageFileUri != null){
                            FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri)
                        }else{
                            updateUserProfile()
                        }


                    }
                }
            }
        }
    }

    private fun updateUserProfile(){

        //showErrorSnackBar("Your details are valid. You can update them.", false )
        val userHashMap = HashMap<String, Any>()  // hashmaps are key value pairs

        val firstName = findViewById<MSEditText>(R.id.et_first_name).text.toString().trim() { it <= ' ' }
        if(firstName != mUserDetails.firstName) {
            userHashMap[Constants.FIRST_NAME] = firstName
        }

        val lastName = findViewById<MSEditText>(R.id.et_last_name).text.toString().trim() { it <= ' '}
        if(lastName != mUserDetails.lastName) {
            userHashMap[Constants.LAST_NAME] = lastName
        }

        val mobileNumber = findViewById<MSEditText>(R.id.et_mobile_number).text.toString().trim { it <= ' '}

        val gender = if(rb_male.isChecked) {
            Constants.MALE
        }else{
            Constants.FEMALE
        }

        if(mUserProfileImageURL.isNotEmpty() && mobileNumber!=mUserDetails.mobile.toString()){
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
        }

        if(mobileNumber.isNotEmpty()){
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }
        //key: gender , value: male/female
        userHashMap[Constants.GENDER] = gender

        userHashMap[Constants.COMPLETE_PROFILE] = 1

        FirestoreClass().updateUserProfileDetails(this, userHashMap)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // onRequestPermissionsGranted is an in-built function. My overriding code begins here .
        if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Constants.showImageChooser(this)
        }else{
            // if permission is not granted
            showErrorSnackBar("Oops, you just denied the permission for storage.You can also allow it from settings.", true)
        }
    }

    // to ask for permissions, add "<uses-permission ....../>" line.

    //reading the result

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == Constants.PICK_IMAGE_REQUEST_CODE){
                if(data != null){
                    try{
                        // the uri of selected image from phone storage.
                        mSelectedImageFileUri = data.data!!
                        // findViewById<ImageView>(R.id.user_photo).setImageURI(selectedImageFileUri)
                        // Glide loader is a faster way to do the above task
                        GlideLoader(this).loadUserPicture(mSelectedImageFileUri!!, findViewById(R.id.user_photo))
                    }catch (e: IOException){
                        e.printStackTrace()
                        showErrorSnackBar("Image selection failed", false)
                    }
                }
            }
        }
    }

    private fun validateUserProfileDetails(): Boolean {
        return when {
            TextUtils.isEmpty(findViewById<MSEditText>(R.id.et_mobile_number).text.toString().trim { it <= ' '})-> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number), true )
                false
            }
            else -> {
                true
            }
        }
    }

    fun userProfileUpdateSuccess(){
        hideProgressDialog()

        Toast.makeText(this, resources.getString(R.string.profile_updated),Toast.LENGTH_SHORT).show()

        startActivity(Intent(this@UserProfileActivity, DashboardActivity::class.java))
        finish()
    }

    fun imageUploadSuccess(imageURL: String) {
        mUserProfileImageURL = imageURL
        updateUserProfile()
    }
}
