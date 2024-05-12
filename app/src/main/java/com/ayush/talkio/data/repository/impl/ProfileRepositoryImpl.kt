package com.ayush.talkio.data.repository.impl

import android.content.Context
import android.net.Uri
import com.ayush.talkio.data.repository.ProfileRepository
import com.ayush.talkio.utils.Compressor
import com.ayush.talkio.utils.Constants.ERR
import com.ayush.talkio.utils.Constants.USERS_REF
import com.ayush.talkio.utils.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val storage: FirebaseStorage,
    private val rtdb: FirebaseDatabase,
    private val auth: FirebaseAuth,
    private val context: Context
): ProfileRepository {

    override fun completeProfile(bio: String?, profilePic: Uri?): Flow<Response<Boolean>> = callbackFlow {
        try {
            trySend(Response.None)
            trySend(Response.Loading)
            var pfpUri = ""
            profilePic?.let { pfp ->
                storage.getReference("images/pfp")
                    .child(auth.currentUser?.uid!!)
                    .putBytes(Compressor.compress(pfp, context))
                    .await()

                pfpUri = storage.getReference("images/pfp")
                    .child(auth.currentUser?.uid!!)
                    .downloadUrl
                    .await()!!
                    .toString()
            }

            val map = mapOf(
                "bio" to (bio?.trim() ?: ""),
                "pfp" to pfpUri.ifEmpty { "" }
            )

            rtdb.getReference(USERS_REF)
                .child(auth.currentUser?.uid!!)
                .updateChildren(map)
                .addOnSuccessListener {
                    trySend(Response.Success(true))
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
}