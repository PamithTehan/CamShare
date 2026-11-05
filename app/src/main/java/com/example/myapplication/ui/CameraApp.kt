package com.example.myapplication.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun CameraApp() {
    val context = LocalContext.current

    var hasCameraPermission by remember {
        mutableStateOf(checkCameraPermission(context))
    }

    var capturedPhotoUri by remember { mutableStateOf<Uri?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            when {
                !hasCameraPermission -> {
                    PermissionScreen(
                        onRequestPermission = {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    )
                }

                capturedPhotoUri != null -> {
                    PhotoPreviewScreen(
                        photoUri = capturedPhotoUri!!,
                        onRetakePhoto = {
                            capturedPhotoUri = null
                        }
                    )
                }

                else -> {
                    CameraPreviewScreen(
                        onPhotoCaptured = { uri ->
                            capturedPhotoUri = uri
                        }
                    )
                }
            }
        }
    }
}

/**
 * Helper function to check if CAMERA permission has been granted.
 */
private fun checkCameraPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}
