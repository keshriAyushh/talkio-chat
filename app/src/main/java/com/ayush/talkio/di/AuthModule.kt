package com.ayush.talkio.di

import com.ayush.talkio.data.repository.AuthRepository
import com.ayush.talkio.data.repository.impl.AuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseDatabase() = FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        rtdb: FirebaseDatabase
    ): AuthRepository {
        return AuthRepositoryImpl(
            auth = auth,
            rtdb = rtdb
        )
    }


}