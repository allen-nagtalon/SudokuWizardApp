package com.example.sudokuWizard.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.example.sudokuWizard.R
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import kotlinx.android.synthetic.main.activity_scan_board.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScanBoardActivity : AppCompatActivity() {

    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_board)

        if(allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            // Bind lifecycle camera to that of owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Build CameraX PreviewView
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(view_finder.createSurfaceProvider())
                }

            // Build CameraX Analyzer
            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, ImageAnalysis.Analyzer { image ->
                        val rotationDegrees = image.imageInfo.rotationDegrees
                        val mediaImage = image.image
                        if(mediaImage != null) {
                            requestProcessImage(
                                    InputImage.fromMediaImage(mediaImage, rotationDegrees))
                                    .addOnCompleteListener{ image.close() }
                        }
                    })
                }

            // Select back camera
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalyzer)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    fun requestProcessImage(image : InputImage) : Task<Text> {
        val textRecognizer = TextRecognition.getClient()
        return textRecognizer.process(image)
                .addOnSuccessListener {results ->
                    board_overlay_view.processScan(results)
                }
                .addOnFailureListener {
                    Log.d(TAG, "No Scan")
                }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
                baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val TAG = "CameraX"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}