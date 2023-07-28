package com.example.familytodos.data

import android.util.Log
import com.example.familytodos.data.utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,  //Get Firesbase Auth
    private val firestoreRepositoryImpl: FirestoreRepositoryImpl //Get Firestore repository
) : AuthRepository {

    //Callback for adding the user in Firestore after registration is done
    suspend fun onRegistrationSuccess(
        firestoreRepositoryImpl: FirestoreRepositoryImpl,
        username: String,
        email: String
    ) {

        firestoreRepositoryImpl.addUserToFirestore(username, email)
    }

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {

        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun register(
        username: String,
        email: String,
        password: String
    ): Resource<FirebaseUser> {

        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result?.user?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(username).build()
            )?.await()
            onRegistrationSuccess(firestoreRepositoryImpl, username, email) //If registration went through call callback function to add the user in Firestore
            Resource.Success(result.user!!)

        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}