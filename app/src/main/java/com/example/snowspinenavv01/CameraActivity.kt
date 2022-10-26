package com.example.snowspinenavv01

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

private var imageCapture: ImageCapture? = null
private lateinit var outputDirectory: File
private lateinit var cameraExecutor: ExecutorService

class CameraActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)


        //outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()
        startCamera()
        displayTargetOnCam()

        //return button
        val return_button = findViewById<Button>(R.id.return_button)
        return_button.setOnClickListener() {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }






    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    val viewFinder = findViewById<View>(R.id.viewFinder) as PreviewView
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()
                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview
                )

            } catch (exc: Exception) {
                Log.e("Yes", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }
    //display target on camera preview
    private fun displayTargetOnCam()
    {
        //val heading = MainActivity().headingHolder
        //val bearing = MainActivity().
        val isTargetVisible = findViewById<View>(R.id.isTargetVisible) as ImageView
        isTargetVisible.visibility = View.VISIBLE
    }
}
