package com.example.sudokuWizard.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat

import com.example.sudokuWizard.R


class ScanBoardActivity : AppCompatActivity() {
    private val RC_HANDLE_CAMERA_PERM = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_board)

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startTextRecognizer()
        } else {
            askCameraPermission()
        }
    }

    private fun startTextRecognizer() {

    }

    private fun askCameraPermission() {
        val permissions = Array(1) {i -> Manifest.permission.CAMERA}

        if(!ActivityCompat.shouldShowRequestPermissionRationale(this,
            Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM)
        }
    }
}