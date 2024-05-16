package com.ayush.talkio.data.repository.impl

import com.ayush.talkio.data.model.User
import com.ayush.talkio.data.repository.AuthRepository
import com.ayush.talkio.utils.Constants.ERR
import com.ayush.talkio.utils.Constants.USERS_REF
import com.ayush.talkio.utils.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val rtdb: FirebaseDatabase
) : AuthRepository {
    override fun signUp(user: User): Flow<Response<Boolean>> = callbackFlow {

        try {
            trySend(Response.None)
            trySend(Response.Loading)

            auth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnSuccessListener { task ->
                    rtdb.getReference(USERS_REF)
                        .child(task.user?.uid!!)
                        .setValue(user.copy(userId = task.user?.uid!!, timestamp = System.currentTimeMillis()))
                        .addOnSuccessListener {
                            trySend(Response.Success(true))
                        }
                        .addOnFailureListener {
                            trySend(Response.Error(it.message ?: ERR))
                        }

                }
                .addOnFailureListener {
                    trySend(Response.Error(it.message ?: ERR))
                }
                .await()
        } catch (e: Exception) {
            trySend(Response.Error(e.message ?: ERR))
        }
        awaitClose {
            this.cancel()
        }
    }

    override fun signIn(email: String, password: String): Flow<Response<Boolean>> = callbackFlow {
        try {
            trySend(Response.None)
            trySend(Response.Loading)

            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    trySend(Response.Success(true))
                }
                .addOnFailureListener { e ->
                    trySend(Response.Error(e.message ?: ERR))
                }
                .await()

        } catch (e: Exception) {
            trySend(Response.Error(e.message ?: ERR))
        }

        awaitClose {
            this.cancel()
        }
    }

    override fun isLoggedIn() = auth.currentUser != null

    override fun getCurrentUserId() = auth.currentUser?.uid!!

    override fun logout() {
        auth.signOut()
    }
}