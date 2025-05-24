package com.time2.superid.qrCodeHandler

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.UUID
import android.util.Base64
import com.time2.superid.passwordHandler.PasswordManager
import com.time2.superid.utils.TokenUtils

class qrCodeManager {
    private val firestore: FirebaseFirestore = Firebase.firestore
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Processa o loginToken
    suspend fun processLoginToken(loginToken: String): String {
        // Verifica se o usuário está logado
        val userId = auth.currentUser?.uid ?: throw Exception("Usuário não está logado")

        // Obtém a referência do documento na coleção login
        val loginDocRef = firestore.collection("login").document(loginToken)
        val loginDoc = loginDocRef.get().await()

        if (!loginDoc.exists()) {
            throw Exception("Token de login não encontrado")
        }

        // Obtém o partnerSite do documento login
        val partnerSite = loginDoc.getString("partnerSite") ?: throw Exception("Site parceiro não encontrado no token")

        // Busca o documento correspondente na coleção userPasswords
        val passwordQuery = firestore.collection("userPasswords")
            .document(userId)
            .collection("passwords")
            .whereEqualTo("partnerSite", partnerSite)
            .get()
            .await()

        if (passwordQuery.isEmpty) {
            throw Exception("Nenhuma senha encontrada para o site parceiro")
        }

        // Assume que há apenas um documento correspondente (ajuste conforme necessário)
        val passwordDoc = passwordQuery.documents.first()
        val username = passwordDoc.getString("username") ?: ""
        val encryptedPassword = passwordDoc.getString("password") ?: throw Exception("Senha não encontrada")
        val passwordId = passwordDoc.id

        // Atualiza o documento na coleção login com tipo explícito
        val loginUpdates: HashMap<String, Any> = hashMapOf(
            "UserUID" to userId,
            "username" to username,
            "password" to encryptedPassword, // Armazena a senha criptografada
            "loginTime" to Date() // Usa loginTimestamp conforme especificado
        )
        loginDocRef.update(loginUpdates).await()

        // Gera um novo accessToken (Base64 de 256 caracteres)
        val newAccessToken = TokenUtils.generateAccessToken()

        // Atualiza o accessToken e lastUpdated na coleção userPasswords com tipo explícito
        val passwordUpdates: HashMap<String, Any> = hashMapOf(
            "accessToken" to newAccessToken,
            "lastUpdated" to Date()
        )
        val passwordDocRef = firestore.collection("userPasswords")
            .document(userId)
            .collection("passwords")
            .document(passwordId)
        passwordDocRef.update(passwordUpdates).await()

        return partnerSite
    }

    // Geração do accesToken
    private fun generateAccessTokens(): String {
        val randomBytes = UUID.randomUUID().toString() + UUID.randomUUID().toString()
        return Base64.encodeToString(randomBytes.toByteArray(), Base64.NO_WRAP)
    }
}