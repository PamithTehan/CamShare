# CamShare - Simple Camera Application

A modern Android application built with **Kotlin**, **Jetpack Compose**, and **CameraX**. The application allows users to request camera permissions, capture photos using a live camera preview, view captured images in real-time, and share them seamlessly using the native Android System Sharing interface.

---

##  Features
- **Runtime Camera Permission**: Handles permission requests gracefully with a custom UI prompt using Compose Activity Result API.
- **Live Camera Viewfinder**: Utilizes **CameraX** (`Preview` and `PreviewView`) to deliver a real-time camera stream.
- **Photograph Capture**: Takes photos using CameraX `ImageCapture` and stores them securely in temporary app cache storage.
- **Photo Display**: Shows captured photographs directly within the app UI using **Coil**.
- **System Share Integration**: Opens the native Android Share Sheet via `Intent.ACTION_SEND` and `FileProvider`, allowing instant sharing to WhatsApp, Bluetooth, Email, Google Drive, and other installed apps.

---

##  Tech Stack & Dependencies

- **Language**: Kotlin
- **UI Toolkit**: Jetpack Compose with Material 3
- **Camera Engine**: AndroidX CameraX (`1.4.0`)
  - `camera-core`
  - `camera-camera2`
  - `camera-lifecycle`
  - `camera-view`
- **Image Loading**: Coil Compose (`2.7.0`)
- **Target SDK**: 37 | **Min SDK**: 36 | **Compile SDK**: 37

---

##  Project Architecture & Structure

```text
com.example.myapplication
├── MainActivity.kt           # Main Entry point using ComponentActivity & setContent
└── ui
    ├── CameraApp.kt           # Main Compose entry point & screen state manager
    ├── CameraPreviewScreen.kt  # CameraX Live Preview & Shutter Capture UI
    ├── PhotoPreviewScreen.kt   # Captured Photo Viewer & Android Share Intent
    └── PermissionScreen.kt   # Camera Permission Request Prompt UI
```

---

##  Key Concepts Covered

### 1. Camera Permissions
Declarations in `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-feature android:name="android.hardware.camera" android:required="false" />
```
Runtime permission check in Jetpack Compose:
```kotlin
val permissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
) { isGranted ->
    hasCameraPermission = isGranted
}
```

### 2. Camera Access with CameraX
Binding lifecycle and use cases (`Preview` + `ImageCapture`):
```kotlin
val preview = Preview.Builder().build().also {
    it.surfaceProvider = previewView.surfaceProvider
}
val capture = ImageCapture.Builder().build()

cameraProvider.bindToLifecycle(lifecycleOwner, lensFacing, preview, capture)
```

### 3. File Sharing with `FileProvider` & Implicit Intents
Setting up `FileProvider` in `AndroidManifest.xml` and `res/xml/file_paths.xml` enables secure content URI generation:
```kotlin
val shareableUri = FileProvider.getUriForFile(
    context,
    "${context.packageName}.fileprovider",
    photoFile
)

val shareIntent = Intent(Intent.ACTION_SEND).apply {
    type = "image/jpeg"
    putExtra(Intent.EXTRA_STREAM, shareableUri)
    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
}
context.startActivity(Intent.createChooser(shareIntent, "Share Photo Via"))
```

---

##  How to Build & Run

1. Open the project in **Android Studio**.
2. Sync the project with Gradle files (`Sync Project with Gradle Files`).
3. Connect an Android device or launch an emulator with camera support.
4. Run the `:app` module.
