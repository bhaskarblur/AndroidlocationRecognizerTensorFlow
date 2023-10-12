package com.bhaskarblur.landmarkrecognizer.domain

import android.graphics.Bitmap

interface landMarkClassifier {

    fun classify(bitmap: Bitmap, rotation: Int) : List<classification>
}