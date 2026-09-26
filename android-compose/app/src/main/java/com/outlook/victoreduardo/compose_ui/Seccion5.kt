@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.outlook.victoreduardo.compose_ui

import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

// Sección 5: Información y retroalimentación
@Composable
fun Seccion5(vm: CatalogoViewModel) {
    val contexto = LocalContext.current
    val snackbar = LocalSnackbar.current
    val navegar = LocalNavegar.current
    val alcance = rememberCoroutineScope()

    PantallaSeccion("Sección 5: Información y retroalimentación", "Elementos que muestran información o avisan al usuario de lo que pasa.") {

        // Conexión con la Sección 3
        TarjetaElemento(
            titulo = "Conexión con la Sección 3",
            descripcion = "Esta sección cambia según lo que elijas en la Sección 3: el interruptor «Modo detallado» muestra información adicional y el deslizador controla la barra de progreso lineal."
        ) {
            Respuesta(
                if (vm.modoDetallado) "Modo detallado: ACTIVADO (se muestra información adicional)"
                else "Modo detallado: desactivado (actívalo en la Sección 3)"
            )
            FilledTonalButton(onClick = { navegar(Destino.Seccion3) }, modifier = Modifier.padding(top = 8.dp)) {
                Text("Cambiar en la Sección 3")
            }
        }

        // Textos con distintos estilos, tamaños y énfasis
        TarjetaElemento(
            titulo = "Textos con distintos estilos",
            descripcion = "El texto puede cambiar de tamaño, grosor, inclinación y color para dar jerarquía a la información. Prueba los botones para modificar el texto de muestra."
        ) {
            var negrita by remember { mutableStateOf(false) }
            var cursiva by remember { mutableStateOf(false) }
            var subrayado by remember { mutableStateOf(false) }
            var tamano by remember { mutableFloatStateOf(22f) }

            // Texto de muestra que cambia con los botones
            Text(
                "Texto de muestra",
                fontSize = tamano.sp,
                fontWeight = if (negrita) FontWeight.Bold else FontWeight.Normal,
                fontStyle = if (cursiva) FontStyle.Italic else FontStyle.Normal,
                textDecoration = if (subrayado) TextDecoration.Underline else TextDecoration.None
            )
            // Énfasis: negrita, cursiva y subrayado (selección múltiple)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = negrita, onClick = { negrita = !negrita }, label = { Text("Negrita") })
                FilterChip(selected = cursiva, onClick = { cursiva = !cursiva }, label = { Text("Cursiva") })
                FilterChip(selected = subrayado, onClick = { subrayado = !subrayado }, label = { Text("Subrayado") })
            }
            // Tamaño del texto
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                OutlinedButton(onClick = { tamano = (tamano - 2f).coerceIn(12f, 40f) }) { Text("A-") }
                OutlinedButton(onClick = { tamano = (tamano + 2f).coerceIn(12f, 40f) }, modifier = Modifier.padding(start = 8.dp)) { Text("A+") }
                Text(
                    "Tamaño: ${tamano.toInt()} sp",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            // Galería de estilos
            Text("Titular grande y en negritas", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text("Subtítulo de tamaño mediano", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 4.dp))
            Text("Cuerpo de texto normal, ideal para párrafos y explicaciones largas.", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 4.dp))
            Text(
                "Nota pequeña en cursiva",
                style = MaterialTheme.typography.bodySmall,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
            // Texto con varios estilos mezclados (negrita, color y tachado)
            val colorDestacado = MaterialTheme.colorScheme.secondary
            val mezclado: AnnotatedString = buildAnnotatedString {
                append("Un mismo texto con ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("negrita") }
                append(", ")
                withStyle(SpanStyle(color = colorDestacado)) { append("color") }
                append(" y ")
                withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) { append("tachado") }
                append(".")
            }
            Text(mezclado, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 4.dp))
        }

        // Imagen local e imagen desde una URL
        TarjetaElemento(
            titulo = "Imagen local e imagen desde URL",
            descripcion = "Una imagen puede venir de los recursos de la app (local) o descargarse desde internet (URL). Cambia el modo de escalado para ver cómo se ajusta al espacio disponible."
        ) {
            val modos = listOf(
                Triple("Recortar", ContentScale.Crop, "recortar para llenar (Crop)"),
                Triple("Ajustar", ContentScale.Fit, "ajustar completa (Fit)"),
                Triple("Estirar", ContentScale.FillBounds, "estirar al espacio (FillBounds)")
            )
            var modo by remember { mutableIntStateOf(0) }
            var indiceImagen by remember { mutableIntStateOf(0) }
            var imagenUrl by remember { mutableStateOf<ImageBitmap?>(null) }
            var cargando by remember { mutableStateOf(true) }
            var mensajeUrl by remember { mutableStateOf("Cargando imagen desde internet…") }
            val idsImagen = listOf(10, 1015, 1018, 1039)

            // Descarga la imagen de internet cada vez que cambia el índice
            LaunchedEffect(indiceImagen) {
                cargando = true
                mensajeUrl = "Cargando imagen desde internet…"
                val bitmap = withContext(Dispatchers.IO) {
                    try {
                        val conexion = URL("https://picsum.photos/id/${idsImagen[indiceImagen]}/800/450").openConnection() as HttpURLConnection
                        conexion.connectTimeout = 8000
                        conexion.readTimeout = 8000
                        val imagen = conexion.inputStream.use { BitmapFactory.decodeStream(it) }
                        conexion.disconnect()
                        imagen
                    } catch (e: Exception) {
                        null
                    }
                }
                cargando = false
                if (bitmap != null) {
                    imagenUrl = bitmap.asImageBitmap()
                    mensajeUrl = "Imagen cargada desde picsum.photos (foto ${indiceImagen + 1} de ${idsImagen.size})"
                } else {
                    mensajeUrl = "No se pudo cargar la imagen. Revisa tu conexión a internet."
                }
            }

            // Modo de escalado (se aplica a las dos imágenes)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                modos.forEachIndexed { indice, (nombre, _, _) ->
                    FilterChip(selected = modo == indice, onClick = { modo = indice }, label = { Text(nombre) })
                }
            }
            Respuesta("Modo: ${modos[modo].third}")

            // Imagen local
            Text("Imagen local (recurso de la app)", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(top = 12.dp))
            Image(
                painter = painterResource(R.drawable.img_catalogo),
                contentDescription = "Imagen local del catálogo",
                contentScale = modos[modo].second,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            // Imagen desde URL
            Text("Imagen cargada desde una URL", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(top = 12.dp))
            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                imagenUrl?.let {
                    Image(
                        bitmap = it,
                        contentDescription = "Imagen descargada de internet",
                        contentScale = modos[modo].second,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                    )
                }
                if (cargando) CircularProgressIndicator()
            }
            Respuesta(mensajeUrl)
            OutlinedButton(
                onClick = { indiceImagen = (indiceImagen + 1) % idsImagen.size },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Text("Cargar otra imagen", modifier = Modifier.padding(start = 8.dp))
            }
            // Información adicional (solo en modo detallado)
            if (vm.modoDetallado) {
                Text(
                    "Modo detallado: la imagen local se redujo a 1200 px de ancho para no pesar de más; la imagen remota es de 800 × 450 px.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        // Indicadores de progreso
        TarjetaElemento(
            titulo = "Indicadores de progreso",
            descripcion = "Indican que algo está en curso. El modo determinado muestra el avance exacto; el indeterminado se usa cuando no se sabe cuánto falta. La barra lineal determinada refleja el deslizador de la Sección 3."
        ) {
            var progresoSimulado by remember { mutableIntStateOf(0) }
            var simulando by remember { mutableStateOf(false) }

            // Simulación de carga: avanza de 5 en 5 hasta llegar a 100
            LaunchedEffect(simulando) {
                if (simulando) {
                    progresoSimulado = 0
                    while (progresoSimulado < 100) {
                        delay(150)
                        progresoSimulado += 5
                    }
                    simulando = false
                }
            }

            // Lineal determinado (valor del deslizador de la Sección 3)
            Text("Lineal determinado: ${vm.nivel} % (valor del deslizador de la Sección 3)", style = MaterialTheme.typography.labelLarge)
            LinearProgressIndicator(
                progress = { vm.nivel / 100f },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .height(8.dp)
            )
            // Lineal indeterminado
            Text("Lineal indeterminado", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(top = 16.dp))
            LinearProgressIndicator(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .height(8.dp)
            )
            // Circulares
            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CircularProgressIndicator(progress = { progresoSimulado / 100f }, modifier = Modifier.size(56.dp), strokeWidth = 6.dp)
                Text("Circular determinado: $progresoSimulado %", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(horizontal = 12.dp))
                CircularProgressIndicator(modifier = Modifier.size(56.dp), strokeWidth = 6.dp)
            }
            FilledTonalButton(onClick = { simulando = true }, enabled = !simulando, modifier = Modifier.padding(top = 8.dp)) {
                Text("Simular carga")
            }
        }

        // Mensaje emergente (toast) y mensaje con acción (snackbar)
        TarjetaElemento(
            titulo = "Toast y Snackbar",
            descripcion = "El toast es un aviso breve que desaparece solo. El snackbar aparece en la parte inferior y puede incluir una acción, como «Deshacer»."
        ) {
            var respuesta by remember { mutableStateOf("Toca un botón para ver el mensaje") }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Toast
                OutlinedButton(onClick = {
                    Toast.makeText(contexto, "Este es un mensaje emergente (toast)", Toast.LENGTH_SHORT).show()
                    respuesta = "Se mostró un toast"
                }) { Text("Mostrar toast") }
                // Snackbar con acción
                Button(onClick = {
                    respuesta = "Se mostró un snackbar con acción"
                    alcance.launch {
                        val resultado = snackbar.showSnackbar("Mensaje con acción (snackbar)", "Deshacer")
                        if (resultado == SnackbarResult.ActionPerformed) respuesta = "Tocaste la acción «Deshacer» del snackbar"
                    }
                }) { Text("Mostrar snackbar") }
            }
            Respuesta(respuesta)
        }

        // Diálogo de confirmación
        TarjetaElemento(
            titulo = "Diálogo de confirmación",
            descripcion = "Ventana que interrumpe al usuario para pedirle una decisión importante, como confirmar una eliminación. Hay que responder para continuar."
        ) {
            var mostrar by remember { mutableStateOf(false) }
            var respuesta by remember { mutableStateOf("Sin respuesta todavía") }
            Button(onClick = { mostrar = true }) { Text("Abrir diálogo") }
            Respuesta(respuesta)
            if (mostrar) {
                AlertDialog(
                    onDismissRequest = { mostrar = false },
                    title = { Text("¿Eliminar elemento?") },
                    text = { Text("Esta acción no se puede deshacer. ¿Quieres continuar?") },
                    dismissButton = {
                        TextButton(onClick = { mostrar = false; respuesta = "Respuesta: cancelaste la acción" }) { Text("Cancelar") }
                    },
                    confirmButton = {
                        TextButton(onClick = { mostrar = false; respuesta = "Respuesta: confirmaste la eliminación" }) { Text("Eliminar") }
                    }
                )
            }
        }

        // Hoja inferior (bottom sheet)
        TarjetaElemento(
            titulo = "Hoja inferior (bottom sheet)",
            descripcion = "Panel que sube desde la parte inferior con opciones adicionales, sin salir de la pantalla actual. Se cierra deslizándolo hacia abajo o tocando fuera."
        ) {
            var mostrar by remember { mutableStateOf(false) }
            var respuesta by remember { mutableStateOf("Sin opción elegida") }
            FilledTonalButton(onClick = { mostrar = true }) { Text("Abrir hoja inferior") }
            Respuesta(respuesta)
            if (mostrar) {
                ModalBottomSheet(
                    onDismissRequest = { mostrar = false },
                    sheetState = rememberModalBottomSheetState()
                ) {
                    Text(
                        "Opciones del elemento",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                    // Opción: compartir
                    ListItem(
                        headlineContent = { Text("Compartir") },
                        leadingContent = { Icon(Icons.Default.Share, contentDescription = null) },
                        modifier = Modifier.clickable { respuesta = "Elegiste: Compartir"; mostrar = false }
                    )
                    // Opción: editar
                    ListItem(
                        headlineContent = { Text("Editar") },
                        leadingContent = { Icon(Icons.Default.Edit, contentDescription = null) },
                        modifier = Modifier.clickable { respuesta = "Elegiste: Editar"; mostrar = false }
                    )
                    // Opción: eliminar
                    ListItem(
                        headlineContent = { Text("Eliminar") },
                        leadingContent = { Icon(Icons.Default.Delete, contentDescription = null) },
                        modifier = Modifier
                            .clickable { respuesta = "Elegiste: Eliminar"; mostrar = false }
                            .padding(bottom = 24.dp)
                    )
                }
            }
        }

        // Tarjeta, separador y distintivo numérico
        TarjetaElemento(
            titulo = "Tarjeta, separador y distintivo numérico",
            descripcion = "La tarjeta agrupa información relacionada (tócala para marcarla), el separador divide contenido y el distintivo numérico (badge) indica cuántos avisos pendientes hay."
        ) {
            var marcada by remember { mutableStateOf(false) }
            var separador by remember { mutableStateOf(true) }
            var avisos by remember { mutableIntStateOf(3) }

            // Tarjeta seleccionable
            Card(
                onClick = { marcada = !marcada },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (marcada) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column16 {
                    Text("Tarjeta de ejemplo", style = MaterialTheme.typography.titleSmall)
                    Text(
                        if (marcada) "Tarjeta marcada" else "Tarjeta sin marcar",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    // Información adicional (solo en modo detallado)
                    if (vm.modoDetallado) {
                        Text(
                            "Modo detallado: esta línea solo aparece cuando activas el interruptor de la Sección 3.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            // Separador
            Text("Contenido de arriba", modifier = Modifier.padding(top = 16.dp))
            if (separador) HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) else Box(Modifier.height(17.dp))
            Text("Contenido de abajo")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Mostrar separador")
                Switch(checked = separador, onCheckedChange = { separador = it })
            }

            // Distintivo numérico (badge)
            Row(modifier = Modifier.padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                BadgedBox(
                    badge = { if (avisos > 0) Badge { Text(if (avisos > 99) "99+" else avisos.toString()) } }
                ) {
                    Icon(Icons.Default.Notifications, contentDescription = "Avisos pendientes", modifier = Modifier.size(32.dp))
                }
                OutlinedButton(onClick = { avisos++ }, modifier = Modifier.padding(start = 16.dp)) { Text("+1") }
                TextButton(onClick = { avisos = 0 }, modifier = Modifier.padding(start = 8.dp)) { Text("Reiniciar") }
            }
        }
    }
}

// Contenedor con relleno para el contenido de una tarjeta
@Composable
private fun Column16(contenido: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit) {
    androidx.compose.foundation.layout.Column(modifier = Modifier.padding(16.dp), content = contenido)
}
