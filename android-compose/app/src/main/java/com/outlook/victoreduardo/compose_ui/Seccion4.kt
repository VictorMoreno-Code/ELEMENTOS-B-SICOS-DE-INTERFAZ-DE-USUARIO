@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.outlook.victoreduardo.compose_ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Card
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Filas de la lista con encabezados (dos tipos de elemento)
private sealed class FilaAgrupada {
    data class Encabezado(val titulo: String) : FilaAgrupada()
    data class Fila(val titulo: String, val detalle: String) : FilaAgrupada()
}

// Sección 4: Listas y colecciones
@Composable
fun Seccion4(vm: CatalogoViewModel) {
    val snackbar = LocalSnackbar.current
    val alcance = rememberCoroutineScope()

    PantallaSeccion("Sección 4: Listas y colecciones", "Elementos para mostrar muchos datos de forma ordenada.") {

        // Lista vertical (con detalle, deslizar para eliminar, actualizar y estado vacío)
        TarjetaElemento(
            titulo = "Lista vertical",
            descripcion = "Lista con más de 15 elementos. Toca uno para ver su detalle, deslízalo a un lado para eliminarlo (puedes deshacer) y arrastra hacia abajo desde el inicio para actualizar. Lo que agregues en la Sección 1 aparece arriba."
        ) {
            var detalle by remember { mutableStateOf<ElementoLista?>(null) }
            var actualizando by remember { mutableStateOf(false) }

            // Contador de elementos
            Text(
                "${vm.elementos.size} elementos en la lista",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (vm.elementos.isEmpty()) {
                // Estado vacío con mensaje e ilustración
                EstadoVacio(onRestaurar = { vm.restaurar() })
            } else {
                // Actualizar la lista arrastrando hacia abajo
                PullToRefreshBox(
                    isRefreshing = actualizando,
                    onRefresh = {
                        alcance.launch {
                            actualizando = true
                            delay(1200)
                            vm.actualizar()
                            actualizando = false
                            snackbar.showSnackbar("Lista actualizada", duration = SnackbarDuration.Short)
                        }
                    },
                    modifier = Modifier.height(360.dp)
                ) {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxSize()) {
                        items(vm.elementos, key = { it.id }) { elemento ->
                            FilaDeslizable(
                                elemento = elemento,
                                alTocar = { detalle = elemento },
                                alEliminar = {
                                    val posicion = vm.elementos.indexOf(elemento)
                                    vm.eliminar(elemento.id)
                                    alcance.launch {
                                        val resultado = snackbar.showSnackbar(
                                            message = "«${elemento.titulo}» eliminado",
                                            actionLabel = "Deshacer",
                                            duration = SnackbarDuration.Short
                                        )
                                        if (resultado == SnackbarResult.ActionPerformed) vm.insertar(posicion, elemento)
                                    }
                                }
                            )
                        }
                    }
                }
            }

            // Vaciar la lista para ver el estado vacío
            OutlinedButton(onClick = { vm.vaciar() }, modifier = Modifier.padding(top = 8.dp)) {
                Icon(Icons.Default.Delete, contentDescription = null)
                Text("Vaciar lista (ver estado vacío)", modifier = Modifier.padding(start = 8.dp))
            }

            // Detalle del elemento tocado
            detalle?.let { elemento ->
                AlertDialog(
                    onDismissRequest = { detalle = null },
                    title = { Text(elemento.titulo) },
                    text = { Text("${elemento.detalle}\n\nOrigen: ${elemento.origen}") },
                    confirmButton = { TextButton(onClick = { detalle = null }) { Text("Cerrar") } }
                )
            }
        }

        // Cuadrícula de elementos
        TarjetaElemento(
            titulo = "Cuadrícula",
            descripcion = "Organiza los elementos en filas y columnas. Es útil para galerías, catálogos o accesos rápidos. Toca un mosaico para seleccionarlo."
        ) {
            val mosaicos = (1..12).map { "Mosaico $it" }
            var seleccionado by remember { mutableStateOf<String?>(null) }
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = false,
                modifier = Modifier.height(4 * 88.dp + 3 * 8.dp)
            ) {
                items(mosaicos) { nombre ->
                    Card(
                        onClick = { seleccionado = nombre },
                        modifier = Modifier.height(88.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (seleccionado == nombre) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Text(nombre, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
            Respuesta(if (seleccionado == null) "Toca un mosaico" else "Seleccionaste: $seleccionado")
        }

        // Lista con encabezados de sección
        TarjetaElemento(
            titulo = "Lista con encabezados de sección",
            descripcion = "Una misma lista con dos tipos de elemento: encabezados que dividen los grupos y filas con la información. Desplázala para ver todos los grupos."
        ) {
            val filas = listOf(
                FilaAgrupada.Encabezado("Entrada y acciones"),
                FilaAgrupada.Fila("Campo de texto", "Sección 1"),
                FilaAgrupada.Fila("Botón relleno", "Sección 2"),
                FilaAgrupada.Fila("Botón flotante", "Sección 2"),
                FilaAgrupada.Encabezado("Selección"),
                FilaAgrupada.Fila("Casilla de verificación", "Sección 3"),
                FilaAgrupada.Fila("Interruptor", "Sección 3"),
                FilaAgrupada.Fila("Chips de filtro", "Sección 3"),
                FilaAgrupada.Encabezado("Colecciones"),
                FilaAgrupada.Fila("Lista vertical", "Sección 4"),
                FilaAgrupada.Fila("Cuadrícula", "Sección 4"),
                FilaAgrupada.Encabezado("Estructura"),
                FilaAgrupada.Fila("Barra superior", "Sección 6"),
                FilaAgrupada.Fila("Barra de navegación inferior", "Sección 6")
            )
            var elegida by remember { mutableStateOf<String?>(null) }
            LazyColumn(modifier = Modifier.height(260.dp)) {
                filas.forEach { fila ->
                    when (fila) {
                        is FilaAgrupada.Encabezado -> stickyHeader {
                            // Encabezado de grupo
                            Text(
                                fila.titulo,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                        is FilaAgrupada.Fila -> item {
                            // Fila con información
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(MaterialTheme.colorScheme.surface)
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                                    .clickable { elegida = "${fila.titulo} (${fila.detalle})" }
                            ) {
                                Text(fila.titulo, style = MaterialTheme.typography.bodyLarge)
                                Text(
                                    fila.detalle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
            Respuesta(if (elegida == null) "Toca una fila" else "Elegiste: $elegida")
        }

        // Pestañas con contenido deslizable
        TarjetaElemento(
            titulo = "Pestañas deslizables",
            descripcion = "Las pestañas dividen el contenido en varias vistas. Toca una pestaña o desliza el contenido hacia los lados para cambiar de página."
        ) {
            val titulos = listOf("Resumen", "Detalles", "Ajustes")
            val textos = listOf(
                "Primera página. Desliza hacia la izquierda para ver la siguiente.",
                "Segunda página. Las pestañas y el deslizamiento están sincronizados.",
                "Tercera página. Cada página puede tener sus propios elementos."
            )
            val megusta = remember { mutableStateListOf(0, 0, 0) }
            val paginador = rememberPagerState(pageCount = { titulos.size })
            PrimaryTabRow(selectedTabIndex = paginador.currentPage) {
                titulos.forEachIndexed { indice, titulo ->
                    Tab(
                        selected = paginador.currentPage == indice,
                        onClick = { alcance.launch { paginador.animateScrollToPage(indice) } },
                        text = { Text(titulo) }
                    )
                }
            }
            HorizontalPager(state = paginador, modifier = Modifier.height(190.dp)) { pagina ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(titulos[pagina], style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        textos[pagina],
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    FilledTonalButton(onClick = { megusta[pagina] = megusta[pagina] + 1 }, modifier = Modifier.padding(top = 12.dp)) {
                        Text("Me gusta (${megusta[pagina]})")
                    }
                }
            }
        }
    }
}

// Fila de la lista que se puede deslizar para eliminar
@Composable
private fun FilaDeslizable(elemento: ElementoLista, alTocar: () -> Unit, alEliminar: () -> Unit) {
    val estado = rememberSwipeToDismissBoxState(
        confirmValueChange = { valor ->
            if (valor != SwipeToDismissBoxValue.Settled) {
                alEliminar()
                true
            } else {
                false
            }
        }
    )
    SwipeToDismissBox(
        state = estado,
        backgroundContent = {
            // Fondo rojo con ícono de basura mientras se desliza
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(horizontal = 16.dp),
                contentAlignment = if (estado.dismissDirection == SwipeToDismissBoxValue.StartToEnd) Alignment.CenterStart else Alignment.CenterEnd
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.onErrorContainer)
            }
        }
    ) {
        OutlinedCard(
            onClick = alTocar,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                // Inicial del elemento
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        elemento.titulo.take(1).uppercase(),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(elemento.titulo, style = MaterialTheme.typography.titleSmall)
                    Text(
                        "Origen: ${elemento.origen}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// Estado vacío: ilustración dibujada con Canvas, mensaje y botón
@Composable
private fun EstadoVacio(onRestaurar: () -> Unit) {
    val colorFondo = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
    val colorInterior = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(360.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier.size(120.dp), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(colorFondo, radius = size.minDimension / 2, center = Offset(size.width / 2, size.height / 2))
                drawCircle(colorInterior, radius = size.minDimension / 3, center = Offset(size.width / 2, size.height / 2))
            }
            Icon(Icons.Default.Inbox, contentDescription = null, modifier = Modifier.size(56.dp), tint = MaterialTheme.colorScheme.primary)
        }
        Text("No hay elementos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
        Text(
            "Eliminaste todos los elementos de la lista.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
        Button(onClick = onRestaurar, modifier = Modifier.padding(top = 12.dp)) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Text("Restaurar lista", modifier = Modifier.padding(start = 8.dp))
        }
    }
}
