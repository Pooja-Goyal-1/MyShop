package com.example.myshop.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.myshop.R
import java.io.IOException

class GlideLoader(val context: Context) {

    fun loadUserPicture(image: Any, imageView: ImageView){
        try{
            //load the user image in the imageView
            Glide
                .with(context)
                .load(image) // URI of the image
                .centerCrop() // scale type of the image
                .placeholder(R.drawable.user_image) // a default image if the image is failed to upload
                .into(imageView) // the view in which the image will be loaded
        }catch (e:IOException){
            e.printStackTrace()
        }
    }
}