package com.ayush.talkio.data.repository.impl

import android.util.Log
import com.ayush.talkio.data.model.Chat
import com.ayush.talkio.data.model.ChatRequest
import com.ayush.talkio.data.model.User
import com.ayush.talkio.data.repository.AllChatsRepository
import com.ayush.talkio.utils.Constants.CHATS_COLLECTION
import com.ayush.talkio.utils.Constants.ERR
import com.ayush.talkio.utils.Constants.REQUESTS_COLLECTION
import com.ayush.talkio.utils.Constants.USERS_REF
import com.ayush.talkio.utils.NotificationHandler
import com.ayush.talkio.utils.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AllChatsRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val rtdb: FirebaseDatabase,
    private val storage: FirebaseStorage
) : AllChatsRepository {

    override fun getAllChatRooms(): Flow<Response<List<Chat>>> = callbackFlow {
        try {
            trySend(Response.None)
            trySend(Response.Loading)

            val userId = auth.currentUser?.uid!!

            db.collection(CHATS_COLLECTION)
//                .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING)
                .whereArrayContains("members", userId)
                .addSnapshotListener { value, error ->
                    error?.let { err ->
                        trySend(Response.Error(err.message ?: ERR))
                        Log.d("error", err.message ?: ERR)
                        trySend(Response.None)
                        this.close()
                    }
                    value?.let {
                        val chats = it.toObjects(Chat::class.java)
                        Log.d("chats", chats.toString())
                        trySend(Response.Success(chats))
                    }
                }
        } catch (e: Exception) {
            trySend(Response.Error(e.message ?: ERR))
            trySend(Response.None)
        }

        awaitClose {
            channel.close()
        }
    }

    override fun sendChatRequest(to: String): Flow<Response<Boolean>> = callbackFlow {
        try {
            trySend(Response.None)

            val from = auth.currentUser?.uid!!

            //Check if the user id exists or not
            rtdb.getReference(USERS_REF)
                .child(to)
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val ss = it.result
                        if (!ss.exists()) {
                            trySend(Response.Error("The user with userId: $to does not exist"))
                            trySend(Response.None)
                        } else {
                            //If user id exists, check if the request is already sent or not
                            db.collection(REQUESTS_COLLECTION)
                                .whereEqualTo("from", from)
                                .whereEqualTo("to", to)
                                .limit(1)
                                .get()
                                .addOnSuccessListener { task ->
                                    if (!task.isEmpty) {
                                        trySend(Response.Error("Request already sent, please wait for its approval"))
                                        trySend(Response.None)
                                    } else {
                                        //Request is not sent, therefore, create one
                                        rtdb.getReference(USERS_REF)
                                            .child(from)
                                            .get()
                                            .addOnCompleteListener { taskSnap ->
                                                val user = taskSnap.result.getValue<User>()!!
                                                val chatReq = ChatRequest(
                                                    from = from,
                                                    to = to,
                                                    senderName = user.name,
                                                    timestamp = System.currentTimeMillis(),
                                                    requestId = "",
                                                    senderPfp = user.pfp
                                                )
                                                db.collection(REQUESTS_COLLECTION)
                                                    .add(chatReq)
                                                    .addOnSuccessListener { taskSnap ->
                                                        db.collection(REQUESTS_COLLECTION)
                                                            .document(taskSnap.id)
                                                            .set(chatReq.copy(requestId = taskSnap.id))
                                                            .addOnSuccessListener {
                                                                NotificationHandler.sendChatRequestNotification(
                                                                    request = chatReq
                                                                )
                                                                trySend(Response.Success(true))
                                                                trySend(Response.None)
                                                            }
                                                            .addOnFailureListener { fail ->
                                                                trySend(
                                                                    Response.Error(
                                                                        fail.message ?: ERR
                                                                    )
                                                                )
                                                                trySend(Response.None)
                                                            }
                                                    }
                                                    .addOnFailureListener { err ->
                                                        trySend(Response.Error(err.message ?: ERR))
                                                        trySend(Response.None)
                                                    }
                                            }
                                    }
                                }
                        }
                    }
                }
                .await()
        } catch (e: Exception) {
            trySend(Response.Error(e.message ?: ERR))
            trySend(Response.None)
        }
        awaitClose {
            this.close()
        }
    }

    override fun setFcmToken() {

    }
}