package com.example.camerax

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class ImageAnalyzer: ImageAnalysis.Analyzer {
    private val barcodeScanner = BarcodeScanning.getClient()

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {
        val mediaImage = image.image

        if (mediaImage != null) {

            val inputImage = InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)
            barcodeScanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    processBarcodes(barcodes) // Handle the detected barcodes
                }
                .addOnFailureListener { exception ->
                    Log.e("BarcodeAnalyzer", "Barcode detection failed", exception)
                }
                .addOnCompleteListener {
                    // Close the image after processing
                    image.close()
                }
        } else {
            image.close()
        }
    }
}

    private fun processBarcodes(barcodes: List<Barcode>) {

        for (barcode in barcodes) {
            val rawValue = barcode.rawValue
            Log.d("BarcodeAnalyzer", "Detected barcode: $rawValue")

            when(barcode.valueType) {
                Barcode.TYPE_URL -> {
                    val url = barcode.url?.url
                    Log.d("BarcodeAnalyzer", " URL: $url")
                }
                Barcode.TYPE_TEXT -> {
                    val text = barcode.displayValue
                    Log.d("BarcodeAnalyzer", " Text: $text")
                }

            }
        }

    }

