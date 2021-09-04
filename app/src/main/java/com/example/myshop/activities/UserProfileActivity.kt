package com.example.myshop.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myshop.R
import com.example.myshop.models.User
import com.example.myshop.utils.Constants
import com.example.myshop.utils.MSEditText
import java.io.IOException

class UserProfileActivity : BaseActivity(), View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        // retrieving the information send using putExtra to this activity.\

        var userDetails: User = User()
        if(intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
            // get the user dtails from the intent as a parcelableExtra
            userDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        val first_name : MSEditText = findViewById(R.id.et_first_name)
        first_name.isEnabled = false   // this field can't be changed.
        first_name.setText(userDetails.firstName) // setting its value.

        val last_name : MSEditText = findViewById(R.id.et_last_name)
        last_name.isEnabled = false   // this field can't be changed.
        last_name.setText(userDetails.lastName) // setting its value.

        val email : MSEditText = findViewById(R.id.et_email)
        email.isEnabled = false   // this field can't be changed.
        email.setText(userDetails.email) // setting its value.

        findViewById<ImageView>(R.id.user_photo).setOnClickListener(this@UserProfileActivity)

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
            }
        }
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
                        val selectedImageFileUri = data.data!!
                        findViewById<ImageView>(R.id.user_photo).setImageURI(selectedImageFileUri)
                    }catch (e: IOException){
                        e.printStackTrace()
                        showErrorSnackBar("Image selection failed", false)
                    }
                }
            }
        }
    }
}