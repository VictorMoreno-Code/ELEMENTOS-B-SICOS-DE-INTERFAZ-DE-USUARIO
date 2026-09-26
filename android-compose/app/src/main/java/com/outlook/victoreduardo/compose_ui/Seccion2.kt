@file:OptIn(ExperimentalMaterial3Api::class)

package com.outlook.victoreduardo.compose_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Sección 2: Botones y acciones
@Composable
fun Seccion2() {
    val snackbar = LocalSnackbar.current
    val alcance = rememberCoroutineScope()

    PantallaSeccion("Sección 2: Botones y acciones", "Elementos que ejecutan una acción al tocarlos. Todos responden con un mensaje visible.") {

        // Botón relleno, con contorno y de solo texto
        TarjetaElemento(
            titulo = "Botón relleno, con contorno y de solo texto",
            descripcion = "Tres niveles de énfasis: el relleno es la acción principal, el de contorno una acción secundaria y el de solo texto la acción de menor importancia."
        ) {
            var relleno by remember { mutableIntStateOf(0) }
            var contorno by remember { mutableIntStateOf(0) }
            var texto by remember { mutableIntStateOf(0) }
            var respuesta by remember { mutableStateOf("Pulsa un botón para ver la respuesta") }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Botón relleno
                Button(onClick = { relleno++; respuesta = "Botón relleno pulsado $relleno veces" }) { Text("Relleno") }
                // Botón con contorno
                OutlinedButton(onClick = { contorno++; respuesta = "Botón con contorno pulsado $contorno veces" }) { Text("Contorno") }
                // Botón de solo texto
                TextButton(onClick = { texto++; respuesta = "Botón de solo texto pulsado $texto veces" }) { Text("Texto") }
            }
            Respuesta(respuesta)
        }

        // Botón con ícono
        TarjetaElemento(
            titulo = "Botón con ícono",
            descripcion = "Los botones pueden llevar solo un ícono (compactos) o un ícono junto al texto (más claros). El corazón cambia de estado cada vez que lo tocas."
        ) {
            var favorito by remember { mutableStateOf(false) }
            var respuesta by remember { mutableStateOf("Toca un botón con ícono") }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Solo ícono (relleno)
                FilledIconButton(onClick = {
                    favorito = !favorito
                    respuesta = if (favorito) "Ícono de corazón: marcado como favorito" else "Ícono de corazón: favorito quitado"
                }) {
                    Icon(if (favorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder, contentDescription = "Favorito")
                }
                // Solo ícono (contorno)
                OutlinedIconButton(onClick = { respuesta = "Ícono de lápiz: modo de edición" }) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                // Ícono más texto
                Button(onClick = {
                    respuesta = "Botón con ícono y texto: compartir"
                    alcance.launch { snackbar.showSnackbar("Se compartió el contenido de ejemplo") }
                }) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text("Compartir", modifier = Modifier.padding(start = 8.dp))
                }
            }
            Respuesta(respuesta)
        }

        // Botón de acción flotante, normal y extendido
        TarjetaElemento(
            titulo = "Botón de acción flotante (normal y extendido)",
            descripcion = "El botón flotante (FAB) destaca la acción principal de una pantalla. La versión extendida incluye texto; al tocarla se contrae y se vuelve a expandir."
        ) {
            var toques by remember { mutableIntStateOf(0) }
            var expandido by remember { mutableStateOf(true) }
            var respuesta by remember { mutableStateOf("Toca uno de los botones flotantes") }
            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // FAB normal
                FloatingActionButton(onClick = { toques++; respuesta = "FAB normal: $toques toques" }) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar")
                }
                // FAB extendido
                ExtendedFloatingActionButton(
                    onClick = {
                        expandido = !expandido
                        respuesta = if (expandido) "FAB extendido: expandido" else "FAB extendido: contraído"
                    },
                    expanded = expandido,
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("Nuevo") }
                )
            }
            Respuesta(respuesta)
        }

        // Botón de alternancia (toggle) y selector segmentado
        TarjetaElemento(
            titulo = "Botón de alternancia y selector segmentado",
            descripcion = "El selector segmentado permite elegir una sola opción entre varias visibles. El botón de alternancia cambia entre dos estados (activado y desactivado) cada vez que se toca."
        ) {
            val opciones = listOf("Día", "Semana", "Mes")
            var elegida by remember { mutableIntStateOf(0) }
            var recordatorio by remember { mutableStateOf(false) }
            // Selector segmentado
            SingleChoiceSegmentedButtonRow {
                opciones.forEachIndexed { indice, opcion ->
                    SegmentedButton(
                        selected = elegida == indice,
                        onClick = { elegida = indice },
                        shape = SegmentedButtonDefaults.itemShape(indice, opciones.size)
                    ) { Text(opcion) }
                }
            }
            // Botón de alternancia
            val modificador = Modifier.padding(top = 12.dp)
            if (recordatorio) {
                FilledTonalButton(onClick = { recordatorio = false }, modifier = modificador) {
                    Icon(Icons.Default.Notifications, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text("Recordatorio", modifier = Modifier.padding(start = 8.dp))
                }
            } else {
                OutlinedButton(onClick = { recordatorio = true }, modifier = modificador) {
                    Icon(Icons.Default.Notifications, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text("Recordatorio", modifier = Modifier.padding(start = 8.dp))
                }
            }
            Respuesta("Vista elegida: ${opciones[elegida]} · Recordatorio: ${if (recordatorio) "activado" else "desactivado"}")
        }

        // Botón deshabilitado y botón en estado de carga
        TarjetaElemento(
            titulo = "Botón deshabilitado y botón en carga",
            descripcion = "Un botón deshabilitado no responde al tocarlo. Un botón en carga bloquea la acción mientras se realiza una tarea y muestra un indicador de progreso."
        ) {
            var habilitado by remember { mutableStateOf(false) }
            var cargando by remember { mutableStateOf(false) }
            var respuesta by remember { mutableStateOf("Prueba habilitar el botón o guardar") }
            // Botón deshabilitado
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { respuesta = "Ahora sí: el botón habilitado respondió" },
                    enabled = habilitado
                ) { Text(if (habilitado) "Habilitado" else "Deshabilitado") }
                TextButton(onClick = {
                    habilitado = !habilitado
                    respuesta = if (habilitado) "Botón habilitado: ya puedes tocarlo" else "Botón deshabilitado: no responde"
                }) { Text(if (habilitado) "Deshabilitar" else "Habilitar") }
            }
            // Botón en estado de carga
            Row(
                modifier = Modifier.padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        alcance.launch {
                            cargando = true
                            respuesta = "Guardando cambios, espera un momento"
                            delay(2000)
                            cargando = false
                            respuesta = "Cambios guardados correctamente"
                        }
                    },
                    enabled = !cargando
                ) { Text(if (cargando) "Guardando…" else "Guardar cambios") }
                if (cargando) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(28.dp),
                        strokeWidth = 3.dp
                    )
                }
            }
            Respuesta(respuesta)
        }
    }
}
