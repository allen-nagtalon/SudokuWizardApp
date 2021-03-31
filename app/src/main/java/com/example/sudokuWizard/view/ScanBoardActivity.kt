package com.example.sudokuWizard.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.camera.core.*
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
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_board)

        if(allPermissionsGranted()) {
            startCamera()

            image_capture_button.setOnClickListener{
                takePhoto()
            }
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
                    it.setSurfaceProvider(view_finder.surfaceProvider
                    )
                }

            imageCapture = ImageCapture.Builder()
                .build()

            // Select back camera
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                val camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture)

                val cameraControl = camera.cameraControl

                view_finder.setOnTouchListener(View.OnTouchListener { view: View, motionEvent: MotionEvent ->
                    when (motionEvent.action) {
                        MotionEvent.ACTION_DOWN -> true
                        MotionEvent.ACTION_UP -> {
                            val factory = view_finder.meteringPointFactory

                            val point = factory.createPoint(motionEvent.x, motionEvent.y)

                            val action = FocusMeteringAction.Builder(point).build()

                            cameraControl.startFocusAndMetering(action)

                            true
                        }
                        else -> false
                    }
                })
            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val TAG = "IMAGE_CAPTURE"
        val imageCapture = imageCapture ?: return

        imageCapture.takePicture(ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                }

                @SuppressLint("UnsafeExperimentalUsageError")
                override fun onCaptureSuccess(image: ImageProxy) {
                    Log.d(TAG, "Photo capture successful!")
                    val rotationDegrees = image.imageInfo.rotationDegrees
                    val mediaImage = image.image
                    if(mediaImage != null) {
                        requestProcessImage(
                            InputImage.fromMediaImage(mediaImage, rotationDegrees))
                            .addOnCompleteListener{ image.close() }
                    }
                    // Process ImageAnalysis here

                }
        })
    }

    private fun requestProcessImage(image : InputImage) : Task<Text> {
        val textRecognizer = TextRecognition.getClient()
        return textRecognizer.process(image)
                .addOnSuccessListener {results ->
                    test_text_view.text = results.text
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