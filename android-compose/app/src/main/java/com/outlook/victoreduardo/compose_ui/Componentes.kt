package com.outlook.victoreduardo.compose_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Formas usadas en toda la app
val FormaCampo = RoundedCornerShape(12.dp)
val FormaBusqueda = RoundedCornerShape(28.dp)

// Snackbar compartido (lo muestra el Scaffold principal)
val LocalSnackbar = compositionLocalOf<SnackbarHostState> { error("Falta el SnackbarHostState") }

// Función para navegar a otra sección
val LocalNavegar = compositionLocalOf<(Destino) -> Unit> { {} }

// Pantalla de una sección: título, subtítulo y contenido con desplazamiento
@Composable
fun PantallaSeccion(
    titulo: String,
    subtitulo: String,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = titulo,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = subtitulo,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        contenido()
    }
}

// Tarjeta de documentación: nombre, explicación y demostración interactiva
@Composable
fun TarjetaElemento(
    titulo: String,
    descripcion: String,
    contenido: @Composable ColumnScope.() -> Unit
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Nombre del elemento
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            // Explicación del elemento
            Text(
                text = descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
            // Demostración
            contenido()
        }
    }
}

// Texto de respuesta visible al interactuar con una demostración
@Composable
fun Respuesta(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.padding(top = 8.dp)
    )
}
