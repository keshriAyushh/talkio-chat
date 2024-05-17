package com.ayush.talkio.data.repository.impl

import com.ayush.talkio.data.model.Message
import com.ayush.talkio.data.model.User
import com.ayush.talkio.data.repository.ChatRepository
import com.ayush.talkio.utils.Constants.CHATROOMS_COLLECTION
import com.ayush.talkio.utils.Constants.CHATS_COLLECTION
import com.ayush.talkio.utils.Constants.ERR
import com.ayush.talkio.utils.Constants.MESSAGES_COLLECTION
import com.ayush.talkio.utils.Constants.USERS_REF
import com.ayush.talkio.utils.NotificationHandler
import com.ayush.talkio.utils.Response
import com.ayush.talkio.utils.getChatroomId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.messaging.FirebaseMessaging
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
class ChatRepositoryImpl @Inject constructor(
    private val rtdb: FirebaseDatabase,
    private val db: FirebaseFirestore,
    private val messaging: FirebaseMessaging,
    private val auth: FirebaseAuth
) : ChatRepository {

    override fun getMessages(receiverId: String): Flow<Response<List<Message>>> = callbackFlow {
        try {
            trySend(Response.None)
            trySend(Response.Loading)

            db.collection(CHATROOMS_COLLECTION)
                .document(getChatroomId(receiverId, auth.currentUser?.uid!!))
                .collection(MESSAGES_COLLECTION)
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener { value, error ->
                    value?.let {
                        val messages = it.toObjects(Message::class.java)
                        trySend(Response.Success(messages))
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

    override fun sendMessage(message: Message) {
        CoroutineScope(Dispatchers.IO).launch {
            val chatroomId = getChatroomId(message.receiverId, auth.currentUser?.uid!!)
            val newMessage = message.copy(
                senderId = auth.currentUser?.uid!!,
                timestamp = System.currentTimeMillis()
            )
            db.collection(CHATROOMS_COLLECTION)
                .document(chatroomId)
                .collection(MESSAGES_COLLECTION)
                .add(newMessage)
                .addOnSuccessListener {
                    NotificationHandler.sendMessageNotification(newMessage)
                }
                .await()

            val map = hashMapOf(
                "lastMessage" to message.text,
                "lastMessageTimestamp" to message.timestamp,
                "lastMessageSenderId" to auth.currentUser?.uid!!,
            )

            db.collection(CHATS_COLLECTION)
                .document(chatroomId)
                .update(map as Map<String, Any>)
                .await()


        }
    }

    override fun getUser(userId: String): Flow<Response<User>> = callbackFlow {
        try {
            rtdb.getReference(USERS_REF)
                .child(userId)
                .get()
                .addOnSuccessListener { task ->
                    val user = task.getValue(User::class.java)!!
                    trySend(Response.Success(user))
                }
                .addOnFailureListener {
                    trySend(Response.Error(it.message ?: ERR))
                    trySend(Response.None)
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
}