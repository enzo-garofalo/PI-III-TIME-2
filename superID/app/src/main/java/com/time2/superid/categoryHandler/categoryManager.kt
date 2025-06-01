package com.time2.superid.categoryHandler

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.time2.superid.ui.components.category.CategoryIcon
import kotlinx.coroutines.tasks.await

data class Category(
    val id    : String = "",
    val title : String = "",
    val description : String = "",
    val iconName    : String = CategoryIcon.GENERIC.name,
    val isDefault   : Boolean = false,
    val isDeletable : Boolean = true,
    val createdAt   : Timestamp = Timestamp.now(),
    val numOfPasswords: Int = 0
)


class CategoryManager {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun getCategoryCollection() = auth.currentUser?.uid?.let { uid ->
        db.collection("userCategory")
            .document(uid)
            .collection("categories")

    }

    suspend fun createCategory(
        title : String = "",
        description : String = "",
        iconName : String = CategoryIcon.GENERIC.label,
        isDefault : Boolean = false,
        isDeletable : Boolean = true
    ): Boolean {
        val collection = getCategoryCollection() ?: return false.also {
            Log.e("CategoryManager", "Usuário não autenticado ao criar senha")
        }

        return try {
            val newCategory = Category(
                title = title,
                description = description,
                iconName = iconName,
                isDefault = isDefault,
                isDeletable = isDeletable,
                createdAt = Timestamp.now()
            )

            val docRef = collection.add(newCategory).await()
            docRef.update("id", docRef.id).await()

            true
        } catch (e: Exception) {
            Log.e("CategoryManager", "Erro ao criar senha: ${e.message}")
            false
        }
    }

    suspend fun getCategories(): List<Category> {
        val collection = getCategoryCollection() ?: return emptyList()

        return try {
            val snapshot = collection.get().await()
            snapshot.toObjects(Category::class.java)
        } catch (e: Exception) {
            Log.e("CategoryManager", "Erro ao buscar categorias: ${e.message}")
            emptyList()
        }
    }

    /**
     * Busca uma categoria pelo ID.
     */
    suspend fun getCategoryById(categoryId: String): Category? {
        val collection = getCategoryCollection() ?: return null

        return try {
            val snapshot = collection.document(categoryId).get().await()
            if (snapshot.exists()) {
                val data = snapshot.data ?: return null
                Category(
                    id = categoryId,
                    title = data["title"] as? String ?: "",
                    description = data["description"] as? String ?: "",
                    iconName = data["iconName"] as? String ?: CategoryIcon.GENERIC.name,
                    isDefault = data["default"] as? Boolean ?: false,
                    isDeletable = data["deletable"] as? Boolean ?: false,
                    createdAt = data["createdAt"] as? Timestamp ?: Timestamp.now(),
                    numOfPasswords = (data["numOfPasswords"] as? Long)?.toInt() ?: 0 // Firestore stores integers as Long
                )
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("CategoryManager", "Erro ao buscar categoria por ID: ${e.message}", e)
            null
        }
    }

    /**
     * Atualiza uma categoria existente na coleção do usuário.
     */
    suspend fun updateCategory(
        category : Category,
        newIcon  : String? = null,
        newTitle : String? = null,
        newDescription: String? = null,
    ): Boolean {
        val collection = getCategoryCollection() ?: return false.also {
            Log.e("CategoryManager", "Usuário não autenticado ao atualizar categoria")
        }

        return try {
            if (category.id.isEmpty()) {
                Log.e("CategoryManager", "ID da categoria é nulo ou vazio na atualização")
                return false
            }

            // Atualiza campos fornecidos, mantém os antigos caso não sejam informados
            val updatedCategory = category.copy(
                title = newTitle ?: category.title,
                description = newDescription ?: category.description,
                iconName = newIcon ?: category.iconName
            )

            collection.document(category.id).set(updatedCategory).await()
            true
        } catch (e: Exception) {
            Log.e("CategoryManager", "Erro ao atualizar categoria: ${e.message}")
            false
        }
    }

    /**
     * Deleta uma categoria da coleção do usuário autenticado.
     */
    suspend fun deleteCategory(categoryId: String): Boolean {
        val collection = getCategoryCollection() ?: return false.also {
            Log.e("CategoryManager", "Usuário não autenticado ao deletar categoria")
        }

        return try {
            collection.document(categoryId).delete().await()
            true
        } catch (e: Exception) {
            Log.e("CategoryManager", "Erro ao deletar categoria: ${e.message}")
            false
        }
    }

    fun incrementNumOfPasswords(categoryId: String) {
        val collection = getCategoryCollection()
        if (collection == null) {
            Log.e("CategoryManager", "Erro ao incrementar numero de senhas categoria")
            return
        }

        collection
            .document(categoryId)
            .update("numOfPasswords", FieldValue.increment(1))
            .addOnSuccessListener {
                Log.d("Category Manager", "Contador incrementado com sucesso")
            }
            .addOnFailureListener { e ->
                Log.w("Category Manager", "Erro ao incrementar contador", e)
            }
    }

    fun decrementNumOfPasswords(categoryId: String) {
        val collection = getCategoryCollection()
        if (collection == null) {
            Log.e("CategoryManager", "Erro ao acessar coleção de categorias")
            return
        }

        val docRef = collection.document(categoryId)
        docRef.get()
            .addOnSuccessListener { document ->
                val currentCount = document.getLong("numOfPasswords") ?: 0L

                if (currentCount <= 0L) {
                    Log.e("CategoryManager", "Erro: número de senhas já é zero ou negativo para a categoria $categoryId")
                    return@addOnSuccessListener
                }


                docRef.update("numOfPasswords", FieldValue.increment(-1))
                    .addOnSuccessListener {
                        Log.d("CategoryManager", "Contador decrementado com sucesso")
                    }
                    .addOnFailureListener { e ->
                        Log.w("CategoryManager", "Erro ao decrementar contador", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.w("CategoryManager", "Erro ao obter contador atual", e)
            }
    }

    fun createBasicCategories(userId: String){
        val categories = listOf(
            Category(
                title = "Apps",
                description = "Meus aplicativos",
                iconName = CategoryIcon.APPS.name,
                isDefault = true,
                isDeletable = false
            ),
            Category(
                title = "Web",
                description = "Meus sites web",
                iconName = CategoryIcon.WEB.name,
                isDefault = true,
                isDeletable = false
            ),
            Category(
                title = "NumPad",
                description = "Meus Teclados numéricos",
                iconName = CategoryIcon.PINPAD.name,
                isDefault = true,
                isDeletable = true
            )
        )

        val catCollection = db.collection("userCategory")
            .document(userId)
            .collection("categories")

        for (category in categories) {
            val newCategoryRef = catCollection.document()
            // Criando campo ID
            val categoryWithId = category.copy(id = newCategoryRef.id)

            newCategoryRef.set(categoryWithId)
                .addOnSuccessListener {
                    Log.d("CategoryManager", "Categoria '${category.title}' salva com sucesso.")
                }
                .addOnFailureListener { e ->
                    Log.e("CategoryManager", "Erro ao salvar categoria '${category.title}'", e)
                }
        }
    }
}