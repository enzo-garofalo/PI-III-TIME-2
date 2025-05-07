package com.time2.superid.AccountsHandler

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.common.base.Verify
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.time2.superid.EmailValidationActivity
import com.time2.superid.HomeActivity
import com.time2.superid.utils.getDeviceID
import com.time2.superid.utils.AESEncryption
import com.time2.superid.utils.showShortToast
import kotlinx.coroutines.tasks.await


data class UserAccount(
    val email: String,
    val name: String,
    val password: String,
    val registerDate: com.google.firebase.Timestamp,
    val hasEmailVerified: Boolean = false,
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

                // Sending email verification
                val user = authResult.user!!
                sendEmailVerification(user)


                val imei = getDeviceID(context)
                val date = com.google.firebase.Timestamp.now()

                // Creating User Model
                val userAccount = UserAccount(email, name, encryptedPass, date, false, userID, imei)

                saveUserToFirestore(userAccount, context)
                showShortToast(context,"Verift your email")


                // Redirecting user to EmailVerificationActivity
                Log.w("CreateAccount", "Account Created")
                context.startActivity(Intent(context, EmailValidationActivity::class.java))

            }
            .addOnFailureListener {
                    e -> Log.e("CreateAccount", "Failed To create account")
                showShortToast(context,"Check Email or Password")
            }
    }

    fun checkEmailVerification(callback: (Boolean) -> Unit) {
        val user = auth.currentUser
        user?.reload()
            ?.addOnSuccessListener {
                val isVerified = user.isEmailVerified
                if (isVerified) {
                    updateEmailVerificationStatus(user.uid)
                }
                callback(isVerified)
            }
            ?.addOnFailureListener { e ->
                Log.e("EmailVerification", "Error reloading user", e)
                callback(false)
            }
    }

    private fun updateEmailVerificationStatus(uid: String) {
        db.collection("AccountsManager")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { results ->
                results.documents.first().reference.update("hasEmailVerified", true)
                Log.d("updateEmailVerificationStatus", "Email verification status updated")
            }
            .addOnFailureListener { e ->
                Log.e("updateEmailVerificationStatus", "Error updating email verification status", e)
            }
    }

    private fun sendEmailVerification(user: FirebaseUser)
    {
        user.sendEmailVerification()
            .addOnSuccessListener {
                Log.d("EmailVerification", "Email sent successfully")
            }
            .addOnFailureListener { e ->
                Log.e("EmailVerification", "Error sending email", e)
            }
    }

    private fun saveUserToFirestore(userAccount: UserAccount, context: Context)
    {
        // Adding user to Firestore
        db.collection("AccountsManager")
            .document()
            .set(userAccount)
            .addOnSuccessListener { Log.d("Firestore", "User data successfully written!") }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error writing document", e)
                showShortToast(context, "Error saving user data")
            }

    }
}