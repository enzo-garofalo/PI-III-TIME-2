package com.time2.superid.qrCodeHandler

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.Date
import com.time2.superid.utils.TokenUtils

class qrCodeManager {
    private val firestore: FirebaseFirestore = Firebase.firestore
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Processa o loginToken
    suspend fun processLoginToken(loginToken: String, docId: String): String {
        // Verifica se o usuário está logado
        val userUID = auth.currentUser?.uid ?: throw Exception("Usuário não está logado")

        // da um get para obter a ref do documento na coleção login
        val loginDocRef = firestore.collection("login").document(loginToken)
        val loginDoc = loginDocRef.get().await()

        if (!loginDoc.exists()) {
            throw Exception("Token de login não encontrado")
        }

        // Obtém o partnerSite do documento login
        val partnerSite = loginDoc.getString("partnerSite") ?: throw Exception("Site parceiro não encontrado no token")

        // Busca o documento específico na coleção userPasswords usando o docId
        val passwordDocRef = firestore.collection("userPasswords")
            .document(userUID)
            .collection("passwords")
            .document(docId)
        val passwordDoc = passwordDocRef.get().await()

        if (!passwordDoc.exists()) {
            throw Exception("Senha não encontrada para o docId fornecido")
        }

        // Verifica se o partnerSite do documento corresponde ao do loginToken
        val docPartnerSite = passwordDoc.getString("partnerSite") ?: throw Exception("Site parceiro não encontrado na senha")
        if (docPartnerSite != partnerSite) {
            throw Exception("O site parceiro da senha não corresponde ao do token de login")
        }

        // Extrai os dados do documento
        val username = passwordDoc.getString("username") ?: ""
        val encryptedPassword = passwordDoc.getString("password") ?: throw Exception("Senha não encontrada")
        val passwordId = passwordDoc.id

        // Atualiza o documento na coleção login
        val loginUpdates: HashMap<String, Any> = hashMapOf(
            "userUID" to userUID,
            "username" to username,
            "password" to encryptedPassword,
            "loginTime" to Date()
        )
        loginDocRef.update(loginUpdates).await()

        // Gera um novo accessToken
        val newAccessToken = TokenUtils.generateAccessToken()

        // Atualiza o accessToken e lastUpdated (indicador de quando o accesstoken foi atualizado) na coleção userPasswords
        val passwordUpdates: HashMap<String, Any> = hashMapOf(
            "accessToken" to newAccessToken,
            "lastUpdated" to Date()
        )
        passwordDocRef.update(passwordUpdates).await()

        return partnerSite
    }
}