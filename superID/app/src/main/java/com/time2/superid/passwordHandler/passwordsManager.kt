package com.time2.superid.passwordHandler

import android.util.Base64
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.time2.superid.utils.AESEncryption
import kotlinx.coroutines.tasks.await
import android.util.Log
import com.time2.superid.categoryHandler.CategoryManager
import com.time2.superid.utils.TokenUtils
import java.security.SecureRandom

data class Password(
    val id: String = "",
    val passwordTitle: String = "",
    val partnerSite: String = "",
    val username: String = "",
    val password: String = "",
    val categoryId:  String = "",
    val description: String = "",
    val accessToken: String = "",
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
     * Cria uma nova senha na coleção do usuário autenticado.
     */
    suspend fun createPassword(
        passwordTitle: String = "",
        partnerSite: String = "",
        username: String = "",
        password: String = "",
        categoryId:  String = "",
        description: String = ""
    ): Boolean {
        val collection = getPasswordsCollection() ?: return false.also {
            Log.e("PasswordManager", "Usuário não autenticado ao criar senha")
        }

        return try {
            // Gerar token de acesso
            val accessToken = TokenUtils.generateAccessToken()

            // Criptografar a senha
            val encryptedPassword = if (password.isNotEmpty()) {
                AESEncryption.encrypt(password)
            } else {
                ""
            }

            // Criar objeto de senha com valores atualizados
            val newPassword = Password(
                passwordTitle = passwordTitle,
                partnerSite = partnerSite,
                username = username,
                password = encryptedPassword,
                categoryId = categoryId,
                description = description,
                accessToken = accessToken,
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
                    categoryId  = data["categoryId"] as? String ?: "",
                    partnerSite = data["partnerSite"] as? String ?: "",
                    username = data["username"] as? String ?: "",
                    password = data["password"] as? String ?: "",
                    passwordTitle = data["passwordTitle"] as? String ?: "",
                    accessToken   = data["accessToken"] as? String ?: "",
                    description   = data["description"] as? String ?: "",
                    createdAt   = data["createdAt"] as? Timestamp ?: Timestamp.now(),
                    lastUpdated = data["lastUpdated"] as? Timestamp ?: Timestamp.now()
                )
            }
        } catch (e: Exception) {
            Log.e("PasswordManager", "Erro ao buscar senhas: ${e.message}")
            emptyList()
        }
    }

    /*
    * Buscando senhas por categoryID
    */
    suspend fun getPasswordsByCategoryID(categoryId : String) : List<Password>{
        val collection = getPasswordsCollection() ?: return emptyList()
        return try {
            collection
                .whereEqualTo("categoryId", categoryId)
                .get().await()
                .documents
                .mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    Password(
                        id            = data["id"] as? String ?: doc.id,
                        categoryId    = data["categoryId"] as? String ?: "",
                        partnerSite   = data["partnerSite"] as? String ?: "",
                        username      = data["username"] as? String ?: "",
                        password      = data["password"] as? String ?: "",
                        passwordTitle = data["passwordTitle"] as? String ?: "",
                        accessToken   = data["accessToken"] as? String ?: "",
                        description   = data["description"] as? String ?: "",
                        createdAt     = data["createdAt"] as? Timestamp ?: Timestamp.now(),
                        lastUpdated   = data["lastUpdated"] as? Timestamp ?: Timestamp.now()
                    )
                }
        } catch (e: Exception) {
            Log.e("PasswordManager", "Erro ao buscar por categoria: ${e.message}")
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
                    categoryId  = data["categoryId"] as? String ?: "",
                    partnerSite = data["partnerSite"] as? String ?: "",
                    username = data["username"] as? String ?: "",
                    password = data["password"] as? String ?: "",
                    passwordTitle = data["passwordTitle"] as? String ?: "",
                    accessToken = data["accessToken"] as? String ?: "",
                    description = data["description"] as? String ?: "",
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
        newDescription: String? = null,
        newPasswordTitle: String? = null,
        newCategory: String? = null,
        newPartnerSite: String? = null
    ): Boolean {
        val collection = getPasswordsCollection() ?: return false

        return try {
            // Verificar se pelo menos um campo está sendo atualizado
            if (newUsername == null && newPassword == null && newDescription == null) {
                Log.w("PasswordManager", "Nenhum campo para atualizar")
                return false
            }

            // Modificando numero de senhas da categoria
            if(newCategory != password.categoryId && newCategory != null){
                val catMan = CategoryManager()
                catMan.decrementNumOfPasswords(password.categoryId)
                catMan.incrementNumOfPasswords(newCategory)
            }

            // Atualiza campos fornecidos, mantém os antigos caso não sejam informados
            var updatedPassword = password.copy(
                username = newUsername ?: password.username,
                description = newDescription ?: password.description,
                passwordTitle = newPasswordTitle ?: password.passwordTitle,
                categoryId = newCategory ?: password.categoryId,
                partnerSite = newPartnerSite ?: password.partnerSite,
                accessToken = refreshAccessToken(password.id).toString(),
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
            val newAccessToken = TokenUtils.generateAccessToken()
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
            val currentPassword = getPasswordById(passwordId)
            val catMan = CategoryManager()
            catMan.decrementNumOfPasswords(currentPassword!!.categoryId)

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