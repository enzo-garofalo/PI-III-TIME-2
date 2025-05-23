package com.time2.superid.utils



import android.view.ViewTreeObserver
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

// Essa função Composable retorna um estado booleano que indica se o teclado (IME) está visível ou não.
@Composable
fun rememberImeState(): State<Boolean> {
    // Cria um estado mutável que começa como 'false' (ou seja, o teclado está fechado inicialmente)
    val imeState = remember {
        mutableStateOf(false)
    }

    // Obtém a `View` atual do Compose (usada para observar alterações de layout)
    val view = LocalView.current

    // Efeito colateral que se conecta ao ciclo de vida do Composable (entra e sai da composição)
    DisposableEffect(view) {
        // Cria um listener que é chamado sempre que o layout global muda (ex: teclado abre ou fecha)
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            // Verifica se o teclado (Input Method Editor - IME) está visível
            val isKeyboardOpen = ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.ime()) // Retorna true se o teclado estiver visível
                ?: true // Por padrão, assume que está visível se não for possível obter o estado

            // Atualiza o estado com o valor atual (true se o teclado estiver aberto)
            imeState.value = isKeyboardOpen
        }

        // Adiciona o listener ao observador de layout da View
        view.viewTreeObserver.addOnGlobalLayoutListener(listener)

        // Define o que fazer quando o Composable sair da composição: remover o listener
        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

    // Retorna o estado que pode ser observado por outros Composables
    return imeState
}