package com.time2.superid.passwordHandler

import android.util.Base64
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.time2.superid.utils.AESEncryption
import kotlinx.coroutines.tasks.await
import android.util.Log
import java.security.SecureRandom

data class Password(
    val id: String = "",
    val categoryId: String = "defaultSitesWeb",
    val partnerSite: String = "",
    val username: String = "",
    val password: String = "",
    val accessToken: String = "",
    val description: String = "",
    val type: String = "web",
    val createdAt: Timestamp = Timestamp.now(),
    val lastUpdated: Timestamp = Timestamp.now()
)

/**
 * Repositório responsável por interagir com o Firestore,
 * gerenciando senhas dentro da coleção "userPasswords/{userId}/{passwordId}".
 */
class PasswordManager {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    /**
     * Obtém a referência à coleção "userPasswords/{userId}" do usuário autenticado.
     */
    private fun getPasswordsCollection() = auth.currentUser?.uid?.let { uid ->
        db.collection("userPasswords")
            .document(uid)
            .collection("passwords")
    }

    /**
     * Gera um token de acesso em Base64 de 256 caracteres
     */
    private fun generateAccessToken(): String {
        val random = SecureRandom()
        val bytes = ByteArray(192)
        random.nextBytes(bytes)
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    /**
     * Cria uma nova senha na coleção do usuário autenticado.
     */
    suspend fun createPassword(
        categoryId: String = "defaultSitesWeb",
        partnerSite: String = "",
        username: String = "",
        password: String = "",
        description: String = "",
        type: String = "web"
    ): Boolean {
        val collection = getPasswordsCollection() ?: return false.also {
            Log.e("PasswordManager", "Usuário não autenticado ao criar senha")
        }

        return try {
            // Gerar token de acesso
            val accessToken = generateAccessToken()

            // Criptografar a senha
            val encryptedPassword = if (password.isNotEmpty()) {
                AESEncryption.encrypt(password)
            } else {
                ""
            }

            // Criar objeto de senha com valores atualizados
            val newPassword = Password(
                categoryId = categoryId,
                partnerSite = partnerSite,
                username = username,
                password = encryptedPassword,
                accessToken = accessToken,
                description = description,
                type = type,
                createdAt = Timestamp.now(),
                lastUpdated = Timestamp.now()
            )

            // Adicionar ao Firestore
            val docRef = collection.add(newPassword).await()

            // Atualizar o campo "id" com o ID gerado
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
                    id = data["id"] as? String ?: doc.id,
                    categoryId = data["categoryId"] as? String ?: "defaultSitesWeb",
                    partnerSite = data["partnerSite"] as? String ?: "",
                    username = data["username"] as? String ?: "",
                    password = data["password"] as? String ?: "",
                    accessToken = data["accessToken"] as? String ?: "",
                    description = data["description"] as? String ?: "",
                    type = data["type"] as? String ?: "web",
                    createdAt = data["createdAt"] as? Timestamp ?: Timestamp.now(),
                    lastUpdated = data["lastUpdated"] as? Timestamp ?: Timestamp.now()
                )
            }
        } catch (e: Exception) {
            Log.e("PasswordManager", "Erro ao buscar senhas: ${e.message}")
            emptyList()
        }
    }

    /**
     * Busca uma senha pelo ID.
     */
    suspend fun getPasswordById(passwordId: String): Password? {
        val collection = getPasswordsCollection() ?: return null

        return try {
            val docSnapshot = collection.document(passwordId).get().await()
            if (docSnapshot.exists()) {
                val data = docSnapshot.data ?: return null
                Password(
                    id = passwordId,
                    categoryId = data["categoryId"] as? String ?: "defaultSitesWeb",
                    partnerSite = data["partnerSite"] as? String ?: "",
                    username = data["username"] as? String ?: "",
                    password = data["password"] as? String ?: "",
                    accessToken = data["accessToken"] as? String ?: "",
                    description = data["description"] as? String ?: "",
                    type = data["type"] as? String ?: "web",
                    createdAt = data["createdAt"] as? Timestamp ?: Timestamp.now(),
                    lastUpdated = data["lastUpdated"] as? Timestamp ?: Timestamp.now()
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
     * Atualiza uma senha existente na coleção do usuário.
     */
    suspend fun updatePassword(
        password: Password,
        newUsername: String? = null,
        newPassword: String? = null,
        newDescription: String? = null
    ): Boolean {
        val collection = getPasswordsCollection() ?: return false

        return try {
            // Verificar se pelo menos um campo está sendo atualizado
            if (newUsername == null && newPassword == null && newDescription == null) {
                Log.w("PasswordManager", "Nenhum campo para atualizar")
                return false
            }

            // Gerar novo token de acesso
            val newAccessToken = generateAccessToken()

            // Construir o objeto Password atualizado
            var updatedPassword = password.copy(
                // Atualizar campos apenas se novos valores forem fornecidos
                username = newUsername ?: password.username,
                description = newDescription ?: password.description,
                // Sempre atualizar o token de acesso e timestamp
                accessToken = newAccessToken,
                lastUpdated = Timestamp.now()
            )

            // Se uma nova senha foi fornecida, criptografá-la e atualizar
            if (newPassword != null) {
                val encryptedPassword = AESEncryption.encrypt(newPassword)
                updatedPassword = updatedPassword.copy(password = encryptedPassword)
            }

            if (password.id.isNotEmpty()) {
                collection.document(password.id).set(updatedPassword).await()
                true
            } else {
                Log.e("PasswordManager", "ID da senha é nulo na atualização")
                false
            }
        } catch (e: Exception) {
            Log.e("PasswordManager", "Erro ao atualizar senha: ${e.message}")
            false
        }
    }

    /**
     * Atualiza apenas o accessToken de uma senha existente.
     */
    suspend fun refreshAccessToken(passwordId: String): Boolean {
        val collection = getPasswordsCollection() ?: return false

        return try {
            val newAccessToken = generateAccessToken()
            collection.document(passwordId).update(
                mapOf(
                    "accessToken" to newAccessToken,
                    "lastUpdated" to Timestamp.now()
                )
            ).await()
            true
        } catch (e: Exception) {
            Log.e("PasswordManager", "Erro ao atualizar token de acesso: ${e.message}")
            false
        }
    }
    /**
     * Deleta uma senha da coleção do usuário autenticado.
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

    /**
     * Descriptografa uma senha para exibição.
     */
    fun decryptPassword(encryptedPassword: String): String {
        return try {
            if (encryptedPassword.isEmpty()) return ""
            AESEncryption.decrypt(encryptedPassword)
        } catch (e: Exception) {
            Log.e("PasswordManager", "Erro ao descriptografar senha: ${e.message}")
            ""
        }
    }
}