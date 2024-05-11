package com.ayush.talkio.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ayush.talkio.navigation.Navigation
import com.ayush.talkio.presentation.ui.theme.TalkioTheme
import com.ayush.talkio.utils.Constants.USERS_REF
import com.ayush.talkio.utils.HandleOnlineActivity
import com.ayush.talkio.utils.LocalActivity
import com.ayush.talkio.utils.LocalAppNavigator
import com.ayush.talkio.utils.LocalAuthNavigator
import com.ayush.talkio.utils.LocalSnackbarState
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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CompositionLocalProvider(
                        LocalAuthNavigator provides rememberNavController(),
                        LocalAppNavigator provides rememberNavController(),
                        LocalSnackbarState provides remember { SnackbarHostState() },
                        LocalActivity provides this@MainActivity
                    ) {
                        Navigation()
                    }
                }
            }
        }
    }

//    override fun onStart() {
//        super.onStart()
//        if (auth.currentUser != null) {
//            handleOnlineActivity.setOnline(auth.currentUser!!.uid)
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        if (auth.currentUser != null) {
//            handleOnlineActivity.setOnline(auth.currentUser!!.uid)
//        }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        if (auth.currentUser != null) {
//            handleOnlineActivity.setLastSeen(auth.currentUser!!.uid, System.currentTimeMillis())
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        handleOnlineActivity.setLastSeen(auth.currentUser!!.uid, System.currentTimeMillis())
//    }
//
//    override fun onStop() {
//        super.onStop()
//        handleOnlineActivity.setLastSeen(auth.currentUser!!.uid, System.currentTimeMillis())
//    }
}
