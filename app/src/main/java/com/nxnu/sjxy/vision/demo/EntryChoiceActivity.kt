/*
 * @Author: yyj-wiki
 * @Creation Date:   2023-03-12
 */

package com.nxnu.sjxy.vision.demo

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.demo.R
import com.nxnu.sjxy.vision.demo.java.LivePreviewActivity
import java.util.ArrayList

class EntryChoiceActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

  override fun onCreate(savedInstanceState: Bundle?) {

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_vision_entry_choice)

    findViewById<TextView>(R.id.java_entry_point).setOnClickListener {
      val intent = Intent(this@EntryChoiceActivity, LivePreviewActivity::class.java)

      startActivity(intent)
    }


    if (!allRuntimePermissionsGranted()) {
      getRuntimePermissions()
    }
  }

  private fun allRuntimePermissionsGranted(): Boolean {
    for (permission in REQUIRED_RUNTIME_PERMISSIONS) {
      permission?.let {
        if (!isPermissionGranted(this, it)) {
          return false
        }
      }
    }
    return true
  }

  private fun getRuntimePermissions() {
    val permissionsToRequest = ArrayList<String>()
    for (permission in REQUIRED_RUNTIME_PERMISSIONS) {
      permission?.let {
        if (!isPermissionGranted(this, it)) {
          permissionsToRequest.add(permission)
        }
      }
    }

    if (permissionsToRequest.isNotEmpty()) {
      ActivityCompat.requestPermissions(
        this,
        permissionsToRequest.toTypedArray(),
        PERMISSION_REQUESTS
      )
    }
  }

  private fun isPermissionGranted(context: Context, permission: String): Boolean {
    if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    ) {
      Log.i(TAG, "Permission granted: $permission")
      return true
    }
    Log.i(TAG, "Permission NOT granted: $permission")
    return false
  }

  companion object {
    private const val TAG = "EntryChoiceActivity"
    private const val PERMISSION_REQUESTS = 1

    private val REQUIRED_RUNTIME_PERMISSIONS =
      arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
      )
  }
}
