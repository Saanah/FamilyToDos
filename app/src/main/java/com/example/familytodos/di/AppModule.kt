package com.example.familytodos.di

import com.example.familytodos.GroupViewModel
import com.example.familytodos.data.AuthRepository
import com.example.familytodos.data.AuthRepositoryImpl
import com.example.familytodos.data.FirestoreRepository
import com.example.familytodos.data.FirestoreRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun provideFirebaseAuth() : FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideAuthRepository(impl: AuthRepositoryImpl) : AuthRepository = impl

    @Provides
    fun provideFirestoreRepository(impl: FirestoreRepositoryImpl) : FirestoreRepository = impl

    @Provides
    fun provideGroupViewModel(firestoreRepository: FirestoreRepository) : GroupViewModel {

        return GroupViewModel(firestoreRepository)
    }
}