package com.time2.superid.AccountsHandler

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.time2.superid.EmailValidationActivity
import com.time2.superid.utils.getDeviceID
import com.time2.superid.utils.showShortToast

data class UserAccount(
    val email: String,
    val name: String,
    val password: String,
    val registerDate: Timestamp,
    val hasEmailVerified: Boolean = false,
    val uid: String,
    val imei: String
)

class UserAccountsManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

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
                    password = password,
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