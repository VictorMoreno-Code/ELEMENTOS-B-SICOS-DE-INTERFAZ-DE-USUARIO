@file:OptIn(ExperimentalMaterial3Api::class)

package com.outlook.victoreduardo.compose_ui

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

// Ciudades para las sugerencias y la búsqueda
private val ciudades = listOf(
    "Ciudad de México", "Guadalajara", "Monterrey", "Puebla", "Querétaro",
    "Toluca", "Mérida", "Tijuana", "León", "Oaxaca", "Guanajuato", "Morelia"
)

// Sección 1: Entrada de texto
@Composable
fun Seccion1(vm: CatalogoViewModel) {
    val snackbar = LocalSnackbar.current
    val navegar = LocalNavegar.current
    val alcance = rememberCoroutineScope()

    PantallaSeccion("Sección 1: Entrada de texto", "Elementos con los que el usuario escribe información.") {

        // Campo de texto simple con etiqueta o hint
        TarjetaElemento(
            titulo = "Campo de texto simple",
            descripcion = "Campo básico con una etiqueta que sirve de guía. Escribe algo y agrégalo a la lista de la Sección 4 (conexión entre secciones)."
        ) {
            var texto by remember { mutableStateOf("") }
            var error by remember { mutableStateOf<String?>(null) }
            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it; error = null },
                label = { Text("Escribe un nombre o una nota") },
                singleLine = true,
                isError = error != null,
                supportingText = error?.let { mensaje -> { Text(mensaje) } },
                shape = FormaCampo,
                modifier = Modifier.fillMaxWidth()
            )
            // Eco del texto escrito
            Respuesta(if (texto.isEmpty()) "Escribiste: (nada todavía)" else "Escribiste: $texto")
            // Botón que envía el texto a la lista de la Sección 4
            Button(
                onClick = {
                    val contenido = texto.trim()
                    if (contenido.isEmpty()) {
                        error = "Escribe algo antes de agregarlo"
                    } else {
                        vm.agregar(contenido, "Sección 1")
                        texto = ""
                        alcance.launch {
                            val resultado = snackbar.showSnackbar("«$contenido» se agregó a la lista de la Sección 4", "Ver")
                            if (resultado == SnackbarResult.ActionPerformed) navegar(Destino.Seccion4)
                        }
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Text("Agregar a la lista (Sección 4)", modifier = Modifier.padding(start = 8.dp))
            }
        }

        // Campo con validación y mensaje de error visible
        TarjetaElemento(
            titulo = "Campo con validación",
            descripcion = "Comprueba lo que se escribe mientras se teclea y muestra un mensaje de error cuando el dato no es válido. Prueba con menos de 4 caracteres o con espacios."
        ) {
            var usuario by remember { mutableStateOf("") }
            val error = when {
                usuario.isEmpty() -> null
                usuario.contains(" ") -> "No se permiten espacios"
                usuario.length < 4 -> "Escribe al menos 4 caracteres"
                usuario.length > 12 -> "Máximo 12 caracteres"
                else -> null
            }
            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Nombre de usuario") },
                singleLine = true,
                isError = error != null,
                supportingText = { Text((error ?: "De 4 a 12 caracteres, sin espacios") + "  ·  ${usuario.length}/12") },
                shape = FormaCampo,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Campo de contraseña con opción para mostrar u ocultar el contenido
        TarjetaElemento(
            titulo = "Campo de contraseña",
            descripcion = "Oculta lo que se escribe con puntos. Toca el ojo del extremo derecho para mostrar u ocultar la contraseña."
        ) {
            var clave by remember { mutableStateOf("") }
            var visible by remember { mutableStateOf(false) }
            val error = clave.isNotEmpty() && clave.length < 8
            OutlinedTextField(
                value = clave,
                onValueChange = { clave = it },
                label = { Text("Contraseña") },
                singleLine = true,
                isError = error,
                supportingText = { Text(if (error) "La contraseña debe tener al menos 8 caracteres" else "Mínimo 8 caracteres") },
                visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { visible = !visible }) {
                        Icon(
                            if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (visible) "Ocultar contraseña" else "Mostrar contraseña"
                        )
                    }
                },
                shape = FormaCampo,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Campos con distintos tipos de teclado
        TarjetaElemento(
            titulo = "Tipos de teclado",
            descripcion = "Cada campo abre un teclado distinto según el tipo de dato: numérico, de correo electrónico o de teléfono. El correo se valida al escribir."
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Teclado numérico
                var numero by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = numero,
                    onValueChange = { numero = it },
                    label = { Text("Número (teclado numérico)") },
                    leadingIcon = { Icon(Icons.Default.Tag, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = FormaCampo,
                    modifier = Modifier.fillMaxWidth()
                )
                // Teclado de correo electrónico
                var correo by remember { mutableStateOf("") }
                val correoInvalido = correo.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(correo).matches()
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo electrónico") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = correoInvalido,
                    supportingText = if (correoInvalido) { { Text("Correo no válido") } } else null,
                    singleLine = true,
                    shape = FormaCampo,
                    modifier = Modifier.fillMaxWidth()
                )
                // Teclado de teléfono
                var telefono by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono") },
                    leadingIcon = { Icon(Icons.Default.Call, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    shape = FormaCampo,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Campo multilínea
        TarjetaElemento(
            titulo = "Campo multilínea",
            descripcion = "Permite escribir textos largos con varios renglones. El contador indica cuántos caracteres llevas del máximo permitido."
        ) {
            var comentarios by remember { mutableStateOf("") }
            OutlinedTextField(
                value = comentarios,
                onValueChange = { if (it.length <= 200) comentarios = it },
                label = { Text("Comentarios") },
                minLines = 3,
                maxLines = 6,
                supportingText = { Text("${comentarios.length}/200") },
                shape = FormaCampo,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Campo con sugerencias automáticas
        TarjetaElemento(
            titulo = "Campo con sugerencias automáticas",
            descripcion = "Mientras escribes, el campo propone opciones que coinciden con el texto. Escribe «Gua» o «Mon» y elige una sugerencia."
        ) {
            var ciudad by remember { mutableStateOf("") }
            var elegida by remember { mutableStateOf<String?>(null) }
            var expandido by remember { mutableStateOf(false) }
            val sugerencias = ciudades.filter { it.contains(ciudad, ignoreCase = true) }
            ExposedDropdownMenuBox(
                expanded = expandido && sugerencias.isNotEmpty(),
                onExpandedChange = { expandido = it }
            ) {
                OutlinedTextField(
                    value = ciudad,
                    onValueChange = { ciudad = it; expandido = true },
                    label = { Text("Ciudad") },
                    singleLine = true,
                    shape = FormaCampo,
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandido && sugerencias.isNotEmpty(),
                    onDismissRequest = { expandido = false }
                ) {
                    sugerencias.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                ciudad = opcion
                                elegida = opcion
                                expandido = false
                            }
                        )
                    }
                }
            }
            Respuesta("Ciudad elegida: ${elegida ?: "(ninguna)"}")
        }

        // Barra de búsqueda
        TarjetaElemento(
            titulo = "Barra de búsqueda",
            descripcion = "Campo redondeado para buscar. Filtra la lista de ciudades mientras escribes; el botón X borra el texto y la lupa del teclado confirma la búsqueda."
        ) {
            var consulta by remember { mutableStateOf("") }
            val foco = LocalFocusManager.current
            OutlinedTextField(
                value = consulta,
                onValueChange = { consulta = it },
                label = { Text("Buscar ciudad") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (consulta.isNotEmpty()) {
                        IconButton(onClick = { consulta = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Borrar texto")
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { foco.clearFocus() }),
                shape = FormaBusqueda,
                modifier = Modifier.fillMaxWidth()
            )
            // Resultados de la búsqueda
            val resultados = ciudades.filter { it.contains(consulta.trim(), ignoreCase = true) }
            Respuesta(
                when {
                    resultados.isEmpty() -> "Sin resultados para «${consulta.trim()}»"
                    consulta.isBlank() -> "Resultados: ${ciudades.size} ciudades"
                    else -> "Resultados (${resultados.size}): ${resultados.joinToString(", ")}"
                }
            )
        }
    }
}
