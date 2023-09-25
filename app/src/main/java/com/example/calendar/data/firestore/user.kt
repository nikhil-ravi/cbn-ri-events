package com.example.calendar.data.firestore

import android.util.Log
import com.example.calendar.domain.auth.UserData
import com.google.firebase.firestore.FieldValue
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

suspend fun addEventToUserFavorites(userId: String, eventId: String) {
    val db = FirebaseFirestore.getInstance()

    try {
        db.collection("users").document(userId).update(
            "favoriteEvents", FieldValue.arrayUnion(
                eventId
            )
        ).await()
    } catch (e: FirebaseFirestoreException) {
        Log.d("error", "addEventToUserFavorites: ${e.message}")
    }
}

suspend fun removeEventFromUserFavorites(userId: String, eventId: String) {
    val db = FirebaseFirestore.getInstance()

    try {
        db.collection("users").document(userId).update(
            "favoriteEvents", FieldValue.arrayRemove(
                eventId
            )
        ).await()
    } catch (e: FirebaseFirestoreException) {
        Log.d("error", "removeEventFromUserFavorites: ${e.message}")
    }
}

suspend fun getFavoritesFromFirestore(userId: String): List<String> {
    val db = FirebaseFirestore.getInstance()

    return try {
        val user = db.collection("users").document(userId).get().await()
        user["favoriteEvents"] as List<String>
    } catch (e: FirebaseFirestoreException) {
        Log.d("error", "getFavoritesFromFirestore: ${e.message}")
        emptyList()
    }
}