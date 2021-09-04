package com.example.myshop.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

class MSRadioButton(context: Context, attributeSet: AttributeSet): AppCompatRadioButton(context, attributeSet){

    init{
        //call the function to apply the font to the components
        applyFont()
    }

    private fun applyFont() {
        //this is used to get the file from the assets folder and set it to the edittext.
        val typeface: Typeface = Typeface.createFromAsset(context.assets, "Roboto-Bold.ttf")
        setTypeface(typeface)
    }

}