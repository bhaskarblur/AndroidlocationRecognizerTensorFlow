package com.bhaskarblur.landmarkrecognizer.data

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.material3.Surface
import com.bhaskarblur.landmarkrecognizer.domain.classification
import com.bhaskarblur.landmarkrecognizer.domain.landMarkClassifier
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.lang.IllegalStateException

class tfLiteLandMarkClassifier(
    private val context : Context,
    private val threshold: Float = 0.5f,
    private val maxResults : Int = 1
) : landMarkClassifier{

    private var classifier : ImageClassifier? = null

    private fun setupClassifier() {
        val baseOptions = BaseOptions.builder()
            .setNumThreads(2).build()

        val options = ImageClassifier.ImageClassifierOptions.builder()
            .setBaseOptions(baseOptions)
            .setMaxResults(maxResults)
            .setScoreThreshold(threshold)
            .build()

        try {
            classifier = ImageClassifier.createFromFileAndOptions(
                context, "europe_location.tflite", options)

        } catch (err : IllegalStateException) {
            err.printStackTrace()
        }
    }
    override fun classify(bitmap: Bitmap, rotation: Int): List<classification> {
        if(classifier == null) {
            setupClassifier();
        }

        val imageProcessor = ImageProcessor.Builder().build()

        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap));
        val imageProcesingOptions = ImageProcessingOptions.builder()
            .setOrientation(getOrientationFromRotation(rotation))
            .build()

       val results = classifier?.classify(tensorImage,imageProcesingOptions)

        return results?.flatMap { classication ->
            classication.categories.map { category ->
                classification(
                    name = category.displayName,
                    score = category.score
                )
            }
        }?.distinctBy { it.name }!!

    }

    private fun getOrientationFromRotation(rotation: Int) : ImageProcessingOptions.Orientation{
        return when(rotation) {
            android.view.Surface.ROTATION_0 -> ImageProcessingOptions.Orientation.RIGHT_TOP
            android.view.Surface.ROTATION_90 -> ImageProcessingOptions.Orientation.TOP_LEFT
            android.view.Surface.ROTATION_180 -> ImageProcessingOptions.Orientation.RIGHT_BOTTOM
            android.view.Surface.ROTATION_270 -> ImageProcessingOptions.Orientation.BOTTOM_RIGHT
            else -> { ImageProcessingOptions.Orientation.BOTTOM_RIGHT}
        }
    }


}