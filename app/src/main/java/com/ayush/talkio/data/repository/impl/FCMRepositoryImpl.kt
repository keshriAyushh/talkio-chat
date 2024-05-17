package com.ayush.talkio.data.repository.impl

import android.util.Log
import com.ayush.talkio.data.repository.FCMRepository
import com.ayush.talkio.utils.Constants.USERS_REF
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FCMRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val messaging: FirebaseMessaging,
    private val rtdb: FirebaseDatabase
) : FCMRepository {

    override fun getFCMToken() {
        CoroutineScope(Dispatchers.IO).launch {
            val userId = auth.currentUser?.uid!!
            messaging.token
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("token", task.result.toString())

                        rtdb.getReference(USERS_REF)
                            .child(userId)
                            .child("fcmToken")
                            .setValue(task.result.toString())
                            .addOnFailureListener {
                                Log.d("fail", it.message.toString())
                            }
                    }

                }
                .await()
        }
    }

    override fun deleteFCMToken() {
        CoroutineScope(Dispatchers.IO).launch {
            rtdb.getReference(USERS_REF)
                .child(auth.currentUser?.uid!!)
                .child("fcmToken")
                .setValue("")
                .await()
            messaging.deleteToken()
        }
    }
}