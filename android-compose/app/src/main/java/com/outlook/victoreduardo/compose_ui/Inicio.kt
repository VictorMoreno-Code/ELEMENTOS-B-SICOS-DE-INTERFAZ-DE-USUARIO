package com.outlook.victoreduardo.compose_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.outlook.victoreduardo.compose_ui.ui.theme.DegradadoFin
import com.outlook.victoreduardo.compose_ui.ui.theme.DegradadoInicio

// Pantalla principal: presenta la app y permite entrar a las seis secciones
@Composable
fun Inicio() {
    val navegar = LocalNavegar.current
    val secciones = listOf(
        Triple(Destino.Seccion1, "Entrada de texto", "Campos, validación, contraseña, teclados y búsqueda"),
        Triple(Destino.Seccion2, "Botones y acciones", "Botones, íconos, FAB, alternancia y carga"),
        Triple(Destino.Seccion3, "Elementos de selección", "Casillas, opciones, interruptor, deslizadores, fecha y chips"),
        Triple(Destino.Seccion4, "Listas y colecciones", "Listas, cuadrícula, detalle, deslizar para borrar y pestañas"),
        Triple(Destino.Seccion5, "Información y retroalimentación", "Textos, imágenes, progreso, mensajes, diálogo y bottom sheet"),
        Triple(Destino.Seccion6, "Contenedores y estructura", "Fila, columna, superpuesta, barra superior, navegación y pesos")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Tarjeta de bienvenida con degradado azul
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Brush.linearGradient(listOf(DegradadoInicio, DegradadoFin)))
                .padding(24.dp)
        ) {
            Column {
                Text(
                    "UIvibe Compose",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    "Catálogo interactivo de elementos básicos de interfaz de usuario. Cada elemento muestra su nombre, una explicación breve y una demostración con la que puedes interactuar.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    "Android nativo · Jetpack Compose · Kotlin",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }

        Text(
            "Secciones",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 12.dp)
        )

        // Tarjetas de las seis secciones
        secciones.forEachIndexed { indice, (destino, nombre, resumen) ->
            OutlinedCard(
                onClick = { navegar(destino) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Número de la sección
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "${indice + 1}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text(nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(
                            resumen,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Explicación de la conexión entre secciones
        TarjetaElemento(
            titulo = "Conexión entre secciones",
            descripcion = "Las secciones comparten datos: lo que agregues desde la Sección 1 aparece en la lista de la Sección 4, y el interruptor y el deslizador de la Sección 3 cambian lo que se muestra en la Sección 5."
        ) {
            FilledTonalButton(onClick = { navegar(Destino.Seccion1) }) { Text("Probar la conexión") }
        }
    }
}
