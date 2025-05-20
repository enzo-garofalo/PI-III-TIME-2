package com.time2.superid.HomeHandler.utils

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import android.util.Log

data class Password(
    val id: String? = null,
    val name: String = "",
    val login: String = "",
    val password: String = "", // Deve ser criptografada com a senha mestre
    val category: String = "",
    val description: String = "",
    val createdAt: Timestamp = Timestamp.now()
) {
    fun toMap(): Map<String, Any> = mapOf(
        "name" to name,
        "login" to login,
        "password" to password,
        "category" to category,
        "description" to description,
        "createdAt" to createdAt
    )
}

/**
 * Repositório responsável por interagir com o Firestore,
 * gerenciando senhas dentro da subcoleção "Passwords" de cada usuário autenticado.
 */
class PasswordRepository {

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
            Log.e("PasswordRepository", "Usuário não autenticado ao criar senha")
        }

        return try {
            collection.add(password.toMap()).await()
            true
        } catch (e: Exception) {
            Log.e("PasswordRepository", "Erro ao criar senha: ${e.message}")
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
                    id = doc.id,
                    name = data["name"] as? String ?: "",
                    login = data["login"] as? String ?: "",
                    password = data["password"] as? String ?: "",
                    category = data["category"] as? String ?: "",
                    description = data["description"] as? String ?: "",
                    createdAt = data["createdAt"] as? Timestamp ?: Timestamp.now()
                )
            }
        } catch (e: Exception) {
            Log.e("PasswordRepository", "Erro ao buscar senhas: ${e.message}")
            emptyList()
        }
    }

    /**
     * Atualiza uma senha existente na subcoleção do usuário.
     */
    suspend fun updatePassword(password: Password): Boolean {
        val collection = getPasswordsCollection() ?: return false

        return try {
            password.id?.let {
                collection.document(it).set(password.toMap()).await()
                true
            } ?: false.also {
                Log.e("PasswordRepository", "ID da senha é nulo na atualização")
            }
        } catch (e: Exception) {
            Log.e("PasswordRepository", "Erro ao atualizar senha: ${e.message}")
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
            Log.e("PasswordRepository", "Erro ao deletar senha: ${e.message}")
            false
        }
    }
}
