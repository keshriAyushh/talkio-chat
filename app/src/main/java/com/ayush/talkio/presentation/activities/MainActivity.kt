package com.ayush.talkio.presentation.activities

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.ayush.talkio.navigation.Navigation
import com.ayush.talkio.presentation.ui.theme.TalkioTheme
import com.ayush.talkio.utils.Constants
import com.ayush.talkio.utils.Constants.USERS_REF
import com.ayush.talkio.utils.HandleOnlineActivity
import com.ayush.talkio.utils.LocalActivity
import com.ayush.talkio.utils.LocalAppNavigator
import com.ayush.talkio.utils.LocalAuthNavigator
import com.ayush.talkio.utils.LocalSnackbarState
import com.ayush.talkio.utils.PermissionHandler.hasPermissions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var auth: FirebaseAuth

    private val handleOnlineActivity = HandleOnlineActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TalkioTheme {
//                val launcher = rememberLauncherForActivityResult(
//                    contract = ActivityResultContracts.RequestMultiplePermissions()
//                ) { maps ->
//                    val granted = maps.values.reduce { acc, next -> (acc && next) }
//                    if (granted) {
//
//                    } else {
//                        Toast.makeText(
//                            this@MainActivity,
//                            "Permission Denied $maps",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                    // You can check one by one
//                    maps.forEach { entry ->
//                        Log.i("Permission = ${entry.key}", "Enabled ${entry.value}")
//                    }
//                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
//                    when {
//                        hasPermissions(this@MainActivity, *Constants.permissions) -> {
//                            // All permissions granted
//                        }
//                        else -> {
//                            // Request permissions
//                            launcher.launch(Constants.permissions)
//                        }
//                    }
                    CompositionLocalProvider(
                        LocalSnackbarState provides remember { SnackbarHostState() },
                        LocalActivity provides this@MainActivity
                    ) {
                        Navigation()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            handleOnlineActivity.setOnline(auth.currentUser!!.uid)
        }
    }

    override fun onResume() {
        super.onResume()
        if (auth.currentUser != null) {
            handleOnlineActivity.setOnline(auth.currentUser!!.uid)
        }
    }

    override fun onPause() {
        super.onPause()
        if (auth.currentUser != null) {
            handleOnlineActivity.setLastSeen(auth.currentUser!!.uid, System.currentTimeMillis())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handleOnlineActivity.setLastSeen(auth.currentUser!!.uid, System.currentTimeMillis())
    }

//    override fun onStop() {
//        super.onStop()
//        handleOnlineActivity.setLastSeen(auth.currentUser!!.uid, System.currentTimeMillis())
//    }
}
