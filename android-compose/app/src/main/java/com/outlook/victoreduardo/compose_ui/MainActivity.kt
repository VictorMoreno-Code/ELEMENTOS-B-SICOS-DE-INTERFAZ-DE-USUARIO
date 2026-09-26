package com.outlook.victoreduardo.compose_ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.outlook.victoreduardo.compose_ui.ui.theme.ComposeUITheme

class MainActivity : ComponentActivity() {

    // Datos compartidos entre las secciones
    private val vm: CatalogoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Pantalla completa (se dibuja debajo de las barras del sistema)
        enableEdgeToEdge()
        setContent {
            ComposeUITheme {
                AppPrincipal(vm)
            }
        }
    }
}
