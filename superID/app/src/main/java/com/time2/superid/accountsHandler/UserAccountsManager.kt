package com.time2.superid.accountsHandler

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.time2.superid.accountsHandler.screens.EmailValidationActivity
import com.time2.superid.utils.getDeviceID
import com.time2.superid.utils.showShortToast

data class UserAccount(
    val email: String = "",
    val name: String = "",
    val registerDate: Timestamp = Timestamp.now(),
    val hasEmailVerified: Boolean = false,
    val uid: String = "",
    val imei: String = ""
)

class UserAccountsManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val TAG = "UserAccountsManager"

    fun createUserAccount(email: String, password: String, name: String, context: Context) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val userID = authResult.user?.uid.toString()
                val user = authResult.user!!
                sendEmailVerification(user)

                val imei = getDeviceID(context)
                val date = Timestamp.now()

                val userAccount = UserAccount(
                    email = email,
                    name = name,
                    registerDate = date,
                    uid = userID,
                    imei = imei
                )

                saveUserToFirestore(userAccount, context)
                showShortToast(context, "Verifique seu email")
                context.startActivity(Intent(context, EmailValidationActivity::class.java))
            }
            .addOnFailureListener { e ->
                Log.e("CreateAccount", "Falha ao criar conta", e)
                showShortToast(context, "Verifique o email e senha")
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

    /**
     * Get user data synchronously - Note: This function is problematic as it doesn't wait for the async call
     * Use getUserProfileName instead for UI elements that need to display user data
     */
    fun getUserData(uid: String): UserAccount? {
        var user: UserAccount? = null
        db.collection("AccountsManager")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents.first()
                    // Fixed: assignment was inside the variable declaration
                    user = document.toObject(UserAccount::class.java)
                }
            }
            .addOnFailureListener {
                Log.e("GetUserData", "Erro ao buscar usuário", it)
            }
        return user
    }

    /**
     * Get user profile name asynchronously with callback
     * This function returns the user's name via a callback when the Firestore query completes
     */
    fun getUserProfileName(uid: String, callback: (String) -> Unit) {
        db.collection("AccountsManager")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents.first()
                    val userAccount = document.toObject(UserAccount::class.java)
                    // Return the name via callback
                    userAccount?.let {
                        callback(it.name)
                    } ?: callback("")
                } else {
                    Log.w(TAG, "No user found with uid: $uid")
                    callback("")
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error fetching user data", e)
                callback("")
            }
    }

    private fun updateEmailVerificationStatus(uid: String) {
        db.collection("AccountsManager")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    querySnapshot.documents.first().reference
                        .update("hasEmailVerified", true)
                        .addOnSuccessListener {
                            Log.d("UpdateStatus", "Verificação de email atualizada")
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("UpdateStatus", "Erro ao atualizar status", e)
            }
    }

    private fun sendEmailVerification(user: FirebaseUser) {
        user.sendEmailVerification()
            .addOnSuccessListener {
                Log.d("EmailVerification", "Email de verificação enviado")
            }
            .addOnFailureListener { e ->
                Log.e("EmailVerification", "Erro ao enviar email", e)
            }
    }

    private fun saveUserToFirestore(userAccount: UserAccount, context: Context) {
        db.collection("AccountsManager")
            .document()
            .set(userAccount)
            .addOnSuccessListener {
                Log.d("Firestore", "Dados salvos com sucesso")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Erro ao salvar dados", e)
                showShortToast(context, "Erro ao salvar dados do usuário")
            }
    }


}