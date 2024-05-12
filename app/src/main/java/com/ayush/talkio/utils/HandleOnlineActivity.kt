package com.ayush.talkio.utils

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HandleOnlineActivity {


    private val rtdb: FirebaseDatabase = FirebaseDatabase.getInstance()

    fun setOnline(userId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            rtdb.getReference("users").child(userId)
                .child("isOnline")
                .setValue(true)
                .await()
        }
    }

    fun setLastSeen(userId: String, lastSeen: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            rtdb.getReference("users").child(userId)
                .child("isOnline")
                .setValue(false)
                .await()

            rtdb.getReference("users").child(userId)
                .child("lastSeen")
                .setValue(System.currentTimeMillis())
                .await()
        }
    }
}