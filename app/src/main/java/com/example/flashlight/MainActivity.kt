package com.example.flashlight

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val MY_PERMISSIONS_REQUEST_CAMERA = 1000
    private val TAG = "MAIN_ACTIVITY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonActivateFlashlight.setOnClickListener(this)
    }

    private fun checkFlashLightPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                return true
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.CAMERA)) {

                } else ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.CAMERA),
                        MY_PERMISSIONS_REQUEST_CAMERA)
            }
        }
        return true
    }

    private fun activateButtonFlashLight(status: Boolean) {
        if (status) buttonActivateFlashlight.text = getString(R.string.on)
        else buttonActivateFlashlight.text = getString(R.string.off)
    }

    private fun buttonActivateFlashlightPressed() {
        Log.e(TAG, "button flashlight pressed ")
        if (checkFlashLightPermission() &&
                buttonActivateFlashlight.text == getString(R.string.on))
            buttonActivateFlashlightPressed(true)
        else buttonActivateFlashlightPressed(false)
    }

    private fun buttonActivateFlashlightPressed(status: Boolean) {
        invokeFlashlight(status)
        activateButtonFlashLight(!status)
    }

    private lateinit var cam: Camera
    private fun invokeFlashlight(status: Boolean) {
        Log.e(TAG, "invoke flashlight $status")
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            if (status) {
                cam = Camera.open()
                val parameters = cam.parameters
                parameters.flashMode = Camera.Parameters.FLASH_MODE_TORCH
                cam.parameters = parameters
                cam.startPreview()
            } else {
                cam.stopPreview()
                cam.release()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_CAMERA -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    buttonActivateFlashlightPressed()
                } else {
                }
                return
            }
            else -> {
            }
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                buttonActivateFlashlight.id -> buttonActivateFlashlightPressed()
            }
        }
    }
}
