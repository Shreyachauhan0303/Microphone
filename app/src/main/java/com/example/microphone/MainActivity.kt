package com.example.microphone
import android.Manifest

import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException
private val PERMISSION_REQUEST_CODE = 1
private var mediaRecorder: MediaRecorder? = null
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val microphoneImage: ImageView = findViewById(R.id.microphone_image)
        microphoneImage.setOnClickListener { onMicrophoneImageClick(it) }
    }

    fun onMicrophoneImageClick(view: android.view.View) {
        if (checkPermission()) {
            testMicrophone()
            Toast.makeText(this, "Hearing", Toast.LENGTH_SHORT).show()
        } else {
            requestPermission()
        }
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.RECORD_AUDIO
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            PERMISSION_REQUEST_CODE
        )
    }

    private fun testMicrophone() {
        try {
            mediaRecorder = MediaRecorder()
            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mediaRecorder?.setOutputFile("/dev/null")
            mediaRecorder?.prepare()
            mediaRecorder?.start()

            // Microphone testing logic here

        } catch (e: IOException) {
            e.printStackTrace()
            stopRecording()
        }
//       } finally {
//            mediaRecorder?.stop()
//            mediaRecorder?.release()
//            mediaRecorder = null
//
//        }

        // Handle permission result
        fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == PERMISSION_REQUEST_CODE) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with microphone testing
                    testMicrophone()

                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun stopRecording() {
        try {
            mediaRecorder?.stop()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } finally {
            mediaRecorder?.release()
            mediaRecorder = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRecording()
    }
}





