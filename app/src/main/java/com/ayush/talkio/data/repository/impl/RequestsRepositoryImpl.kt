package com.ayush.talkio.data.repository.impl

import com.ayush.talkio.data.model.Chat
import com.ayush.talkio.data.model.ChatRequest
import com.ayush.talkio.data.model.User
import com.ayush.talkio.data.repository.RequestsRepository
import com.ayush.talkio.utils.Constants
import com.ayush.talkio.utils.Constants.ERR
import com.ayush.talkio.utils.Constants.REQUESTS_COLLECTION
import com.ayush.talkio.utils.Constants.USERS_REF
import com.ayush.talkio.utils.Response
import com.ayush.talkio.utils.getChatroomId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestsRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val rtdb: FirebaseDatabase,
    private val storage: FirebaseStorage
) : RequestsRepository {

    override fun getAllRequests(): Flow<Response<List<ChatRequest>>> = callbackFlow {
        try {
            trySend(Response.None)
            trySend(Response.Loading)

            val userId = auth.currentUser?.uid!!

            db.collection("requests")
                .whereEqualTo("to", userId)
                .addSnapshotListener { value, error ->
                    value?.let {
                        val requests = it.toObjects(ChatRequest::class.java)
                        trySend(Response.Success(requests))
                    }
                    error?.let {
                        trySend(Response.Error(it.message ?: ERR))
                        trySend(Response.None)
                    }
                }
        } catch (e: Exception) {
            trySend(Response.Error(e.message ?: ERR))
            trySend(Response.None)
        }
        awaitClose {
            this.close()
        }
    }

    override fun rejectRequest(request: ChatRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            db.collection(REQUESTS_COLLECTION)
                .document(request.requestId)
                .delete()
                .await()
        }
    }

    override fun acceptRequest(request: ChatRequest): Flow<Response<Boolean>> = callbackFlow {
        CoroutineScope(Dispatchers.IO).launch {
            db.collection(REQUESTS_COLLECTION)
                .document(request.requestId)
                .delete()
                .await()
            val chatroomDocId = getChatroomId(request.to, request.from)

            val receiver = rtdb.getReference(USERS_REF)
                .child(request.to)
                .get()
                .await()
                .getValue(User::class.java)!!

            val sender = rtdb.getReference(USERS_REF)
                .child(request.from)
                .get()
                .await()
                .getValue(User::class.java)!!

            db.collection(Constants.CHATS_COLLECTION)
                .document(chatroomDocId)
                .set(
                    Chat(
                        chatroomId = chatroomDocId,
                        members = listOf(request.from, request.to),
                        lastMessageTime = System.currentTimeMillis(),
                        senderName = sender.name,
                        senderId = sender.userId,
                        senderPfp = sender.pfp,
                        receiverName = receiver.name,
                        receiverId = receiver.userId,
                        receiverPfp = receiver.pfp
                    )
                )
                .addOnSuccessListener {
                    trySend(Response.Success(true))
                }
                .addOnFailureListener {
                    trySend(Response.Error(it.message ?: ERR))
                    trySend(Response.None)
                }
                .await()
        }
        awaitClose {
            this.close()
        }
    }
}