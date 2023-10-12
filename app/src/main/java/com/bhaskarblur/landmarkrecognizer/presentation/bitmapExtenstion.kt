package com.bhaskarblur.landmarkrecognizer.presentation

import android.graphics.Bitmap

fun Bitmap.centerCrop(dWidth : Int, dHeight: Int) : Bitmap {
    val xStart = (width - dWidth) / 2
    val yStart = (height - dHeight) / 2

    if(xStart < 0 || yStart < 0 || dHeight > height || dWidth > width) {
        throw IllegalAccessException("Invalid arguements for center cropping")
    }

    return Bitmap.createBitmap(this, xStart, yStart, dWidth, dHeight)
}