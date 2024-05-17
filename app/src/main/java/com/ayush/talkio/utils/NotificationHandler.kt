package com.ayush.talkio.utils


import android.util.Log
import com.ayush.talkio.data.model.ChatRequest
import com.ayush.talkio.data.model.Message
import com.ayush.talkio.data.model.User
import com.ayush.talkio.utils.Constants.ERR
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException

object NotificationHandler {

    private val rtdb = FirebaseDatabase.getInstance()

    fun sendMessageNotification(message: Message) {
        CoroutineScope(Dispatchers.IO).launch {
            val otherUserFcmToken = rtdb.getReference(Constants.USERS_REF)
                .child(message.receiverId)
                .child("fcmToken")
                .get()
                .await()
                .getValue(String::class.java)!!

            rtdb.getReference(Constants.USERS_REF)
                .child(message.senderId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = task.result.getValue(User::class.java)!!

                        val jsonObject = JsonObject()
                        val notifObject = JsonObject()
                        val dataObject = JsonObject()

                        notifObject.addProperty("title", user.name)
                        notifObject.addProperty("body", message.text)

                        dataObject.addProperty("userId", user.userId)

                        jsonObject.add("notification", notifObject)
                        jsonObject.add("data", dataObject)

                        jsonObject.addProperty("to", otherUserFcmToken)


                        callApi(jsonObject)
                    }
                }
        }
    }

    fun sendChatRequestNotification(request: ChatRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            val otherUserFcmToken = rtdb.getReference(Constants.USERS_REF)
                .child(request.to)
                .child("fcmToken")
                .get()
                .await()
                .getValue(String::class.java)!!

            rtdb.getReference(Constants.USERS_REF)
                .child(request.from)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = task.result.getValue(User::class.java)!!

                        val jsonObject = JsonObject()
                        val notifObject = JsonObject()
//                        val dataObject = JsonObject()

                        notifObject.addProperty("title", user.name)
                        notifObject.addProperty("body", "Sent you a chat request!")

//                        dataObject.addProperty("userId", user.userId)

                        jsonObject.add("notification", notifObject)
//                        jsonObject.add("data", dataObject)

                        jsonObject.addProperty("to", otherUserFcmToken)


                        callApi(jsonObject)
                    }
                }
        }
    }


    private fun callApi(jsonObject: JsonObject) {
        val json: MediaType = "application/json; charset=utf-8".toMediaType()
        val client = OkHttpClient()
        val jsonString = jsonObject.toString()
        val requestBody = jsonString.toRequestBody(json)
        val request = Request.Builder()
            .url(Constants.FCM_URL)
            .post(requestBody)
            .header(
                "Authorization",
                "Bearer AAAAKQd_ceU:APA91bGo1MSRpL3vecfqvU74N8wHv9kjRt-9bGyuFGfP1WD2nOwnGZj_ZOvRARATPRy0qO9WTpJcNxgz3LhE9_fn8MsZRoZAo05Phzii26SW_CO1PUSdLwNX9HC-Q-mU8IpRG3TAtROz"
            )
            .build()

        client.newCall(request).enqueue(
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("notificationErr", e.message ?: ERR)
                }

                override fun onResponse(call: Call, response: Response) {

                }
            }

        )
    }
}
