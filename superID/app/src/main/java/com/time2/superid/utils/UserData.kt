package com.time2.superid.utils

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.time2.superid.AccountsHandler.UserAccount
import com.time2.superid.HomeActivity
import kotlinx.coroutines.tasks.await

fun getDeviceID(context: Context) : String
{
    // Return device IMEI
    return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}

//suspend fun getUserData(uid: String): UserAccount? {
//    val db = FirebaseFirestore.getInstance()
//    return try {
//        val snapshot = db.collection("AccountsManager")
//            .whereEqualTo("uid", uid)
//            .get()
//            .await()
//        if (!snapshot.isEmpty) {
//            snapshot.documents.first().toObject(UserAccount::class.java)
//        } else null
//    } catch (e: Exception) {
//        Log.e("Firestore", "Failed to get user data", e)
//        null
//    }
//}


fun redirectIfLogged(activity: ComponentActivity, TAG: String): Boolean {
    val auth = Firebase.auth

    if (auth.currentUser != null) {
        Log.i(TAG, "Usuário já logado: ${auth.currentUser?.uid}")
        val intent = Intent(activity, HomeActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
        return true
    }
    return false
}