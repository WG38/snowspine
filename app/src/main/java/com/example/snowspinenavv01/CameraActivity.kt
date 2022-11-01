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


class CameraActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)


        //outputDirectory = getOutputDirectory()


        //displayTargetOnCam()

        //return button
        val return_button = findViewById<Button>(R.id.return_button)
        return_button.setOnClickListener() {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }


    }









    //display target on camera preview

}
