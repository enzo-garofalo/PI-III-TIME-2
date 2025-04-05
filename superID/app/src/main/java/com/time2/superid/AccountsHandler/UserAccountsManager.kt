package com.time2.superid.AccountsHandler

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.time2.superid.HomeActivity
import com.time2.superid.utils.getDeviceID
import com.time2.superid.utils.AESEncryption
import com.time2.superid.utils.showShortToast


data class UserAccount(
    val email: String,
    val name: String,
    val password: String,
    val registerDate: com.google.firebase.Timestamp,
    val uid: String,
    val imei: String
)


class UserAccountsManager
{
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    fun createUserAccount(email : String, password : String, name : String, context: Context)
    {
        // Return Encrypted Password
        val encryptedPass = AESEncryption.encrypt(password)


        auth.createUserWithEmailAndPassword(email, encryptedPass)
            .addOnSuccessListener {
                // Getting UID and IMEI
                    authResult -> val userID = authResult.user?.uid.toString()
                val imei = getDeviceID(context)
                val date = com.google.firebase.Timestamp.now()

                // Creating User Model
                val userAccount = UserAccount(email, name, encryptedPass, date, userID, imei)


                saveUserToFirestore(userAccount, context)
                showShortToast(context,"Welcome to superID ${userAccount.name}")


                // Redirecting user to HomeActivity
                Log.w("CreateAccount", "Account Created")
                val intent = Intent(context, HomeActivity::class.java)
                context.startActivity(intent)
            }
            .addOnFailureListener {
                    e -> Log.e("CreateAccount", "Failed To create account")
                showShortToast(context,"Check Email or Password")
            }
    }

    private fun saveUserToFirestore(userAccount: UserAccount, context: Context)
    {
        // Adding user to Firestore
        db.collection("AccountsManager").document().set(userAccount)
            .addOnSuccessListener { Log.d("Firestore", "User data successfully written!") }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error writing document", e)
                showShortToast(context, "Error saving user data")
            }

    }
}
