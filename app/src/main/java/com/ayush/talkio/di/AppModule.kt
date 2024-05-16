package com.ayush.talkio.di

import android.content.Context
import com.ayush.talkio.data.repository.AllChatsRepository
import com.ayush.talkio.data.repository.AuthRepository
import com.ayush.talkio.data.repository.ProfileRepository
import com.ayush.talkio.data.repository.RequestsRepository
import com.ayush.talkio.data.repository.impl.AllChatsRepositoryImpl
import com.ayush.talkio.data.repository.impl.AuthRepositoryImpl
import com.ayush.talkio.data.repository.impl.ProfileRepositoryImpl
import com.ayush.talkio.data.repository.impl.RequestsRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseDatabase() = FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun providesFirestore() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun providesStorage() = FirebaseStorage.getInstance()

    @Provides
    fun provideAuthRepository(
        auth: FirebaseAuth,
        rtdb: FirebaseDatabase
    ): AuthRepository = AuthRepositoryImpl(
        auth = auth,
        rtdb = rtdb
    )

    @Provides
    fun providesProfileRepository(
        auth: FirebaseAuth,
        rtdb: FirebaseDatabase,
        storage: FirebaseStorage,
        @ApplicationContext context: Context
    ): ProfileRepository = ProfileRepositoryImpl(
        auth = auth,
        rtdb = rtdb,
        storage = storage,
        context = context
    )

    @Provides
    fun providesAllChatsRepository(
        auth: FirebaseAuth,
        db: FirebaseFirestore,
        rtdb: FirebaseDatabase,
        storage: FirebaseStorage
    ): AllChatsRepository = AllChatsRepositoryImpl(
        auth = auth,
        db = db,
        rtdb = rtdb,
        storage = storage
    )

    @Provides
    fun providesRequestRepository(
        auth: FirebaseAuth,
        db: FirebaseFirestore,
        rtdb: FirebaseDatabase,
        storage: FirebaseStorage
    ): RequestsRepository = RequestsRepositoryImpl(
        auth = auth,
        db = db,
        rtdb = rtdb,
        storage = storage
    )
}