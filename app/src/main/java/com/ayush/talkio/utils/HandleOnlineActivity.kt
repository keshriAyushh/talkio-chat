package com.ayush.talkio.utils

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

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

    fun listenForUserOnlineStatus(userId: String, onStatusChange: (Boolean) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("users")

        val userOnlineStatusListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val isOnline = dataSnapshot.child("isOnline").getValue(Boolean::class.java) ?: false
                onStatusChange(isOnline)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        }
        usersRef.child(userId).addValueEventListener(userOnlineStatusListener)
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