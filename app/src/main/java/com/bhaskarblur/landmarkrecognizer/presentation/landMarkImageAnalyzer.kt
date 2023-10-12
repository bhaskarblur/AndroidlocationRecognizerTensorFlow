package com.bhaskarblur.landmarkrecognizer.presentation

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.bhaskarblur.landmarkrecognizer.domain.classification
import com.bhaskarblur.landmarkrecognizer.domain.landMarkClassifier

class landMarkImageAnalyzer(
    private val classifier: landMarkClassifier,
    private val onResult : (List<classification>) -> Unit

) : ImageAnalysis.Analyzer{

    private var frameSkipCounter = 0

    override fun analyze(image: ImageProxy) {
        if(frameSkipCounter % 60 == 0) {
            val degrees = image.imageInfo.rotationDegrees
            var bitmap = image.toBitmap()
                .centerCrop(321, 321);

            val result = classifier.classify(bitmap, degrees);
            onResult(result)

        }
        frameSkipCounter++;
        image.close()
    }

}