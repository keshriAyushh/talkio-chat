package com.ayush.talkio.utils

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat

object PermissionHandler {
    fun hasPermissions(context: Context, vararg permissions: String): Boolean =
        permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }


//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestMultiplePermissions()
//    ) { maps ->
//        val granted = maps.values.reduce { acc, next -> (acc && next) }
//        if (granted) {
//            // all permission granted
//        } else {
//            // Permission Denied: Do something
//        }
//        // You can check one by one
//        maps.forEach { entry ->
//            Log.i("Permission = ${entry.key}", "Enabled ${entry.value}")
//        }
//    }

}