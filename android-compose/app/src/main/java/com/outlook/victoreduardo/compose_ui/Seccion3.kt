@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.outlook.victoreduardo.compose_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TriStateCheckbox
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

// Sección 3: Elementos de selección
@Composable
fun Seccion3(vm: CatalogoViewModel) {
    PantallaSeccion("Sección 3: Elementos de selección", "Elementos para elegir una o varias opciones.") {

        // Casilla de verificación (con estado indeterminado)
        TarjetaElemento(
            titulo = "Casilla de verificación",
            descripcion = "Permite marcar o desmarcar una opción. La casilla «Seleccionar todos los avisos» tiene tres estados: marcada, desmarcada e indeterminada (cuando solo algunas opciones están marcadas)."
        ) {
            // Casilla simple
            var terminos by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier.toggleable(value = terminos, role = Role.Checkbox, onValueChange = { terminos = it }),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = terminos, onCheckedChange = null)
                Text("Acepto los términos y condiciones", modifier = Modifier.padding(start = 8.dp))
            }

            // Casilla principal con estado indeterminado y tres casillas hijas
            val nombres = listOf("Avisos por correo", "Avisos por mensaje SMS", "Avisos en la aplicación")
            val hijas = remember { mutableStateListOf(false, true, false) }
            val marcadas = hijas.count { it }
            val estadoPadre = when (marcadas) {
                0 -> ToggleableState.Off
                hijas.size -> ToggleableState.On
                else -> ToggleableState.Indeterminate
            }
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                TriStateCheckbox(
                    state = estadoPadre,
                    onClick = {
                        val marcarTodas = estadoPadre != ToggleableState.On
                        for (i in hijas.indices) hijas[i] = marcarTodas
                    }
                )
                Text("Seleccionar todos los avisos", modifier = Modifier.padding(start = 8.dp))
            }
            nombres.forEachIndexed { indice, nombre ->
                Row(
                    modifier = Modifier
                        .padding(start = 32.dp)
                        .toggleable(value = hijas[indice], role = Role.Checkbox, onValueChange = { hijas[indice] = it }),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = hijas[indice], onCheckedChange = null)
                    Text(nombre, modifier = Modifier.padding(start = 8.dp))
                }
            }
            Respuesta(if (terminos) "Términos aceptados · Avisos activados: $marcadas de ${hijas.size}" else "Términos sin aceptar · Avisos activados: $marcadas de ${hijas.size}")
        }

        // Grupo de botones de opción
        TarjetaElemento(
            titulo = "Botones de opción (radio)",
            descripcion = "Muestra varias opciones de las que solo se puede elegir una. Al elegir una, la anterior se desmarca automáticamente."
        ) {
            val envios = listOf("Envío rápido (1 día)", "Envío estándar (3 días)", "Envío económico (7 días)")
            var elegido by remember { mutableStateOf(1) }
            envios.forEachIndexed { indice, envio ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(selected = elegido == indice, role = Role.RadioButton, onClick = { elegido = indice })
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = elegido == indice, onClick = null)
                    Text(envio, modifier = Modifier.padding(start = 8.dp))
                }
            }
            Respuesta("Elegiste: ${envios[elegido]}")
        }

        // Interruptor (switch)
        TarjetaElemento(
            titulo = "Interruptor (switch)",
            descripcion = "Activa o desactiva un ajuste al instante. El «Modo detallado» está conectado con la Sección 5: al activarlo, allá aparece información adicional."
        ) {
            var notificaciones by remember { mutableStateOf(false) }
            // Interruptor conectado con la Sección 5
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Modo detallado (afecta a la Sección 5)")
                Switch(checked = vm.modoDetallado, onCheckedChange = { vm.modoDetallado = it })
            }
            // Interruptor independiente
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Recibir notificaciones")
                Switch(checked = notificaciones, onCheckedChange = { notificaciones = it })
            }
            Respuesta(
                "Modo detallado: ${if (vm.modoDetallado) "activado" else "desactivado"} · " +
                    "Notificaciones: ${if (notificaciones) "activadas" else "desactivadas"}"
            )
        }

        // Deslizador de valor único y deslizador de rango
        TarjetaElemento(
            titulo = "Deslizador simple y de rango",
            descripcion = "El deslizador simple elige un valor y el de rango elige un mínimo y un máximo. El valor del deslizador simple se muestra como progreso en la Sección 5."
        ) {
            // Deslizador de valor único
            Text("Nivel: ${vm.nivel} (se muestra en la Sección 5)", style = androidx.compose.material3.MaterialTheme.typography.labelLarge)
            Slider(
                value = vm.nivel.toFloat(),
                onValueChange = { vm.nivel = it.toInt() },
                valueRange = 0f..100f,
                steps = 99
            )
            // Deslizador de rango
            var rango by remember { mutableStateOf(20f..70f) }
            Text(
                "Rango: de ${rango.start.toInt()} a ${rango.endInclusive.toInt()}",
                style = androidx.compose.material3.MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
            RangeSlider(
                value = rango,
                onValueChange = { rango = it },
                valueRange = 0f..100f,
                steps = 99
            )
        }

        // Lista desplegable de selección
        TarjetaElemento(
            titulo = "Lista desplegable",
            descripcion = "Muestra una lista de opciones al tocar el campo y guarda la que elijas. Ocupa poco espacio cuando hay muchas opciones."
        ) {
            val carreras = listOf(
                "Ingeniería en Sistemas Computacionales",
                "Ingeniería en Inteligencia Artificial",
                "Licenciatura en Ciencia de Datos"
            )
            var expandido by remember { mutableStateOf(false) }
            var carrera by remember { mutableStateOf<String?>(null) }
            ExposedDropdownMenuBox(expanded = expandido, onExpandedChange = { expandido = it }) {
                OutlinedTextField(
                    value = carrera ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Carrera") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                    shape = FormaCampo,
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
                    carreras.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = { carrera = opcion; expandido = false }
                        )
                    }
                }
            }
            Respuesta(if (carrera == null) "Toca el campo para elegir una carrera" else "Elegiste: $carrera")
        }

        // Selector de fecha y selector de hora
        TarjetaElemento(
            titulo = "Selector de fecha y de hora",
            descripcion = "Abren un calendario o un reloj para elegir una fecha o una hora sin tener que escribirlas. El resultado aparece debajo."
        ) {
            var mostrarFecha by remember { mutableStateOf(false) }
            var mostrarHora by remember { mutableStateOf(false) }
            var fecha by remember { mutableStateOf("(sin elegir)") }
            var hora by remember { mutableStateOf("(sin elegir)") }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Selector de fecha
                OutlinedButton(onClick = { mostrarFecha = true }) {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                    Text("Elegir fecha", modifier = Modifier.padding(start = 8.dp))
                }
                // Selector de hora
                OutlinedButton(onClick = { mostrarHora = true }) {
                    Icon(Icons.Default.Schedule, contentDescription = null)
                    Text("Elegir hora", modifier = Modifier.padding(start = 8.dp))
                }
            }
            Respuesta("Fecha: $fecha")
            Respuesta("Hora: $hora")

            if (mostrarFecha) {
                val estado = rememberDatePickerState()
                DatePickerDialog(
                    onDismissRequest = { mostrarFecha = false },
                    confirmButton = {
                        TextButton(onClick = {
                            estado.selectedDateMillis?.let { milisegundos ->
                                val formato = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale.forLanguageTag("es-MX"))
                                formato.timeZone = TimeZone.getTimeZone("UTC")
                                fecha = formato.format(milisegundos)
                            }
                            mostrarFecha = false
                        }) { Text("Aceptar") }
                    },
                    dismissButton = { TextButton(onClick = { mostrarFecha = false }) { Text("Cancelar") } }
                ) { DatePicker(state = estado) }
            }

            if (mostrarHora) {
                val estado = rememberTimePickerState(initialHour = 12, initialMinute = 0, is24Hour = true)
                AlertDialog(
                    onDismissRequest = { mostrarHora = false },
                    title = { Text("Selecciona una hora") },
                    text = { TimePicker(state = estado) },
                    confirmButton = {
                        TextButton(onClick = {
                            hora = String.format(Locale.US, "%02d:%02d", estado.hour, estado.minute)
                            mostrarHora = false
                        }) { Text("Aceptar") }
                    },
                    dismissButton = { TextButton(onClick = { mostrarHora = false }) { Text("Cancelar") } }
                )
            }
        }

        // Chips de filtro seleccionables
        TarjetaElemento(
            titulo = "Chips de filtro",
            descripcion = "Etiquetas compactas que se pueden activar o desactivar para filtrar contenido. Puedes seleccionar varias a la vez."
        ) {
            val filtros = listOf("Android", "Compose", "Flutter", "Kotlin", "Dart")
            val activos = remember { mutableStateListOf<String>() }
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                filtros.forEach { filtro ->
                    FilterChip(
                        selected = filtro in activos,
                        onClick = { if (filtro in activos) activos.remove(filtro) else activos.add(filtro) },
                        label = { Text(filtro) }
                    )
                }
            }
            Respuesta(if (activos.isEmpty()) "Sin filtros activos" else "Filtros activos: ${activos.joinToString(", ")}")
        }
    }
}
