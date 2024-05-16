package com.ayush.talkio.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.ayush.talkio.navigation.Navigation
import com.ayush.talkio.presentation.ui.theme.TalkioTheme
import com.ayush.talkio.utils.HandleOnlineActivity
import com.ayush.talkio.utils.LocalActivity
import com.ayush.talkio.utils.LocalSnackbarState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
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
