package com.time2.superid.passwordHandler

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import android.util.Log

data class Password(
    val id: String = "",
    val name: String = "",
    val login: String = "",
    val password: String = "", // Deve ser criptografada com a senha mestre
    val category: String = "",
    val description: String = "",
    val createdAt: Timestamp = Timestamp.now()
)

/**
 * Repositório responsável por interagir com o Firestore,
 * gerenciando senhas dentro da subcoleção "Passwords" de cada usuário autenticado.
 */
class PasswordManager {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    /**
     * Obtém a referência à subcoleção "Passwords" do usuário autenticado.
     */
    private fun getPasswordsCollection() = auth.currentUser?.uid?.let { uid ->
        db.collection("AccountsManager")
            .document(uid)
            .collection("Passwords")
    }

    /**
     * Cria uma nova senha na subcoleção do usuário autenticado.
     */
    suspend fun createPassword(password: Password): Boolean {
        val collection = getPasswordsCollection() ?: return false.also {
            Log.e("PasswordManager", "Usuário não autenticado ao criar senha")
        }

        return try {
            val docRef = collection.add(password).await()
            // Atualiza o campo "id" com o ID gerado
            docRef.update("id", docRef.id).await()
            true
        } catch (e: Exception) {
            Log.e("PasswordManager", "Erro ao criar senha: ${e.message}")
            false
        }
    }

    /**
     * Recupera todas as senhas do usuário autenticado.
     */
    suspend fun getPasswords(): List<Password> {
        val collection = getPasswordsCollection() ?: return emptyList()

        return try {
            val snapshot = collection.get().await()
            snapshot.documents.mapNotNull { doc ->
                val data = doc.data ?: return@mapNotNull null
                Password(
                    id =  data["id"] as? String ?: doc.id,
                    name = data["name"] as? String ?: "",
                    login = data["login"] as? String ?: "",
                    password = data["password"] as? String ?: "",
                    category = data["category"] as? String ?: "",
                    description = data["description"] as? String ?: "",
                    createdAt = data["createdAt"] as? Timestamp ?: Timestamp.now()
                )
            }
        } catch (e: Exception) {
            Log.e("PasswordManager", "Erro ao buscar senhas: ${e.message}")
            emptyList()
        }
    }

    suspend fun getPasswordById(docId: String): Password? {
        val collection = getPasswordsCollection() ?: return null

        return try {
            val docSnapshot = collection.document(docId).get().await()
            if (docSnapshot.exists()) {
                val data = docSnapshot.data ?: return null
                Password(
                    id = docId,
                    name = data["name"] as? String ?: "",
                    login = data["login"] as? String ?: "",
                    password = data["password"] as? String ?: "",
                    category = data["category"] as? String ?: "",
                    description = data["description"] as? String ?: "",
                    createdAt = data["createdAt"] as? Timestamp ?: Timestamp.now()
                )
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("PasswordManager", "Erro ao buscar documento: ${e.message}")
            null
        }
    }


    /**
     * Atualiza uma senha existente na subcoleção do usuário.
     */
    suspend fun updatePassword(password: Password): Boolean {
        val collection = getPasswordsCollection() ?: return false

        return try {
            password.id?.let {
                collection.document(it).set(password).await()
                true
            } ?: false.also {
                Log.e("PasswordManager", "ID da senha é nulo na atualização")
            }
        } catch (e: Exception) {
            Log.e("PasswordManager", "Erro ao atualizar senha: ${e.message}")
            false
        }
    }

    /**
     * Deleta uma senha da subcoleção do usuário autenticado.
     */
    suspend fun deletePassword(passwordId: String): Boolean {
        val collection = getPasswordsCollection() ?: return false

        return try {
            collection.document(passwordId).delete().await()
            true
        } catch (e: Exception) {
            Log.e("PasswordManager", "Erro ao deletar senha: ${e.message}")
            false
        }
    }
}
