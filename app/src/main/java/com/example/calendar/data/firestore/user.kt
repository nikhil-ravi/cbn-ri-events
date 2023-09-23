package com.example.calendar.data.firestore

import android.util.Log
import com.example.calendar.domain.auth.UserData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

suspend fun addUserToFirestore(user: UserData) {
    val db = FirebaseFirestore.getInstance()

    try {
        db.collection("users").document(user.userId).set(user).await()
    } catch (e: FirebaseFirestoreException) {
        Log.d("error", "addUserToFirestore: ${e.message}")
    }
}