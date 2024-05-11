package com.ayush.talkio.data.repository.impl

import android.app.Activity
import com.ayush.talkio.data.model.User
import com.ayush.talkio.data.repository.AuthRepository
import com.ayush.talkio.utils.Constants.ERR
import com.ayush.talkio.utils.Constants.USERS_REF
import com.ayush.talkio.utils.Response
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val rtdb: FirebaseDatabase
) : AuthRepository {
    override fun sendOtp(phone: String, activity: Activity): Flow<Response<String>> = callbackFlow {
        trySend(Response.None)
        trySend(Response.Loading)

        val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {}

            override fun onVerificationFailed(p0: FirebaseException) {
                trySend(Response.Error(p0.localizedMessage ?: ERR))
            }

            override fun onCodeSent(
                verificationCode: String,
                p1: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationCode, p1)
                trySend(Response.Success(verificationCode))
            }
        }

        val options = PhoneAuthOptions
            .newBuilder(auth)
            .setPhoneNumber("+91$phone")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callback)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)

        awaitClose {
            close()
        }
    }

    override fun verifyOtp(
        phone: String,
        otp: String,
        verificationCode: String
    ): Flow<Response<Boolean>> =
        callbackFlow {
            trySend(Response.None)
            trySend(Response.Loading)
            val credential = PhoneAuthProvider.getCredential(verificationCode, otp)
            auth.signInWithCredential(credential)
                .addOnSuccessListener {
                    if (rtdb.getReference(USERS_REF).child(it.user?.uid!!).get().result.exists()) {
                        trySend(Response.Success(true))
                    } else {
                        rtdb.getReference(USERS_REF).child(it.user?.uid!!)
                            .setValue(User(userId = it.user?.uid!!, phone = phone))
                            .addOnSuccessListener {
                                trySend(Response.Success(true))
                            }
                            .addOnFailureListener { err ->
                                trySend(Response.Error(err.localizedMessage ?: ERR))
                            }
                    }
                }
                .addOnFailureListener {
                    trySend(Response.Error(it.localizedMessage ?: ERR))
                }
                .await()

            awaitClose {
                this.close()
            }

        }

    override fun logout() {
        auth.signOut()
    }
}