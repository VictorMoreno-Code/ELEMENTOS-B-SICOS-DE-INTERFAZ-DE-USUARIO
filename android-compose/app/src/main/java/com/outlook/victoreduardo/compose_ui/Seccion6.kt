@file:OptIn(ExperimentalMaterial3Api::class)

package com.outlook.victoreduardo.compose_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import kotlinx.coroutines.launch

// Caja de color usada en las demostraciones de contenedores
@Composable
private fun Caja(texto: String, fondo: Color, contenido: Color, modificador: Modifier = Modifier) {
    Box(
        modifier = modificador
            .clip(RoundedCornerShape(12.dp))
            .background(fondo),
        contentAlignment = Alignment.Center
    ) {
        Text(texto, color = contenido, fontWeight = FontWeight.Bold)
    }
}

// Sección 6: Contenedores y estructura
@Composable
fun Seccion6() {
    val colores = MaterialTheme.colorScheme
    val alcance = rememberCoroutineScope()

    PantallaSeccion("Sección 6: Contenedores y estructura", "Elementos que organizan a los demás dentro de la pantalla.") {

        // Distribución en fila y en columna
        TarjetaElemento(
            titulo = "Distribución en fila y en columna",
            descripcion = "Un mismo contenedor puede acomodar sus elementos en una fila (horizontal) o en una columna (vertical). Cambia la orientación con los botones y toca una caja."
        ) {
            var enFila by remember { mutableStateOf(true) }
            var respuesta by remember { mutableStateOf("Orientación: fila") }
            // Elegir la orientación
            SingleChoiceSegmentedButtonRow {
                SegmentedButton(
                    selected = enFila,
                    onClick = { enFila = true; respuesta = "Orientación: fila" },
                    shape = SegmentedButtonDefaults.itemShape(0, 2)
                ) { Text("Fila") }
                SegmentedButton(
                    selected = !enFila,
                    onClick = { enFila = false; respuesta = "Orientación: columna" },
                    shape = SegmentedButtonDefaults.itemShape(1, 2)
                ) { Text("Columna") }
            }
            // Tres cajas tocables dentro de una fila o una columna
            val cajas: @Composable () -> Unit = {
                Caja("A", colores.primary, colores.onPrimary, Modifier.size(64.dp).clickable { respuesta = "Tocaste la caja A" })
                Caja("B", colores.secondary, colores.onSecondary, Modifier.size(64.dp).clickable { respuesta = "Tocaste la caja B" })
                Caja("C", colores.tertiary, colores.onTertiary, Modifier.size(64.dp).clickable { respuesta = "Tocaste la caja C" })
            }
            if (enFila) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 12.dp)) { cajas() }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 12.dp)) { cajas() }
            }
            Respuesta(respuesta)
        }

        // Distribución superpuesta
        TarjetaElemento(
            titulo = "Distribución superpuesta",
            descripcion = "Los elementos se colocan unos encima de otros. Sirve para poner un texto sobre una imagen o un distintivo sobre un ícono. Toca una caja para traerla al frente."
        ) {
            // Orden de dibujo: la última caja de la lista queda al frente
            val orden = remember { mutableStateListOf(1, 2, 3) }
            Box(modifier = Modifier.fillMaxWidth().height(150.dp)) {
                orden.forEach { numero ->
                    val (fondo, texto) = when (numero) {
                        1 -> colores.primary to colores.onPrimary
                        2 -> colores.secondary to colores.onSecondary
                        else -> colores.tertiary to colores.onTertiary
                    }
                    Caja(
                        texto = "$numero",
                        fondo = fondo,
                        contenido = texto,
                        modificador = Modifier
                            .offset(x = ((numero - 1) * 40).dp, y = ((numero - 1) * 25).dp)
                            .size(100.dp)
                            .clickable {
                                orden.remove(numero)
                                orden.add(numero)
                            }
                    )
                }
            }
            Respuesta("Al frente: caja ${orden.last()}")
        }

        // Contenedor con desplazamiento vertical
        TarjetaElemento(
            titulo = "Contenedor con desplazamiento vertical",
            descripcion = "Cuando el contenido es más grande que el espacio disponible, un contenedor con desplazamiento permite moverse hacia arriba y abajo. Desliza el recuadro o usa los botones."
        ) {
            val desplazamiento = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(colores.surfaceVariant.copy(alpha = 0.4f))
                    .verticalScroll(desplazamiento)
                    .padding(12.dp)
            ) {
                (1..20).forEach { Text("Renglón $it de 20") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
                OutlinedButton(onClick = { alcance.launch { desplazamiento.animateScrollTo(0) } }) { Text("Ir al inicio") }
                OutlinedButton(onClick = { alcance.launch { desplazamiento.animateScrollTo(desplazamiento.maxValue) } }) { Text("Ir al final") }
            }
        }

        // Barra superior con título y acciones
        TarjetaElemento(
            titulo = "Barra superior",
            descripcion = "Barra en la parte superior con el título de la pantalla, un botón de navegación a la izquierda y acciones a la derecha. Los tres puntos abren las acciones extra."
        ) {
            var respuesta by remember { mutableStateOf("Toca una acción de la barra") }
            var menuAbierto by remember { mutableStateOf(false) }
            TopAppBar(
                title = { Text("Mi barra superior") },
                navigationIcon = {
                    IconButton(onClick = { respuesta = "Tocaste el botón de navegación (menú)" }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú")
                    }
                },
                actions = {
                    IconButton(onClick = { respuesta = "Acción: Buscar" }) { Icon(Icons.Default.Search, contentDescription = "Buscar") }
                    IconButton(onClick = { respuesta = "Acción: Favorito" }) { Icon(Icons.Default.Favorite, contentDescription = "Favorito") }
                    IconButton(onClick = { menuAbierto = true }) { Icon(Icons.Default.MoreVert, contentDescription = "Más opciones") }
                    DropdownMenu(expanded = menuAbierto, onDismissRequest = { menuAbierto = false }) {
                        DropdownMenuItem(text = { Text("Compartir") }, onClick = { respuesta = "Acción del menú: Compartir"; menuAbierto = false })
                        DropdownMenuItem(text = { Text("Ayuda") }, onClick = { respuesta = "Acción del menú: Ayuda"; menuAbierto = false })
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colores.primaryContainer,
                    titleContentColor = colores.onPrimaryContainer,
                    navigationIconContentColor = colores.onPrimaryContainer,
                    actionIconContentColor = colores.onPrimaryContainer
                ),
                windowInsets = androidx.compose.foundation.layout.WindowInsets(0.dp)
            )
            Respuesta(respuesta)
        }

        // Barra de navegación inferior
        TarjetaElemento(
            titulo = "Barra de navegación inferior",
            descripcion = "Permite cambiar entre las pantallas principales con un toque. Esta app usa un menú lateral para sus secciones; aquí ves una barra inferior funcional con un distintivo de avisos en «Perfil»."
        ) {
            var elegida by remember { mutableIntStateOf(0) }
            var avisosPerfil by remember { mutableIntStateOf(3) }
            val nombres = listOf("Inicio", "Favoritos", "Perfil")
            val iconos = listOf(Icons.Default.Home, Icons.Default.Favorite, Icons.Default.Person)
            // Pantalla que cambia con la barra
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(colores.surfaceVariant.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Text("Estás en: ${nombres[elegida]}", style = MaterialTheme.typography.titleMedium)
            }
            NavigationBar {
                nombres.forEachIndexed { indice, nombre ->
                    NavigationBarItem(
                        selected = elegida == indice,
                        onClick = {
                            elegida = indice
                            // Al entrar a Perfil se quitan los avisos
                            if (indice == 2) avisosPerfil = 0
                        },
                        icon = {
                            if (indice == 2 && avisosPerfil > 0) {
                                BadgedBox(badge = { Badge { Text(avisosPerfil.toString()) } }) {
                                    Icon(iconos[indice], contentDescription = nombre)
                                }
                            } else {
                                Icon(iconos[indice], contentDescription = nombre)
                            }
                        },
                        label = { Text(nombre) }
                    )
                }
            }
        }

        // Distribución con pesos proporcionales
        TarjetaElemento(
            titulo = "Distribución con pesos proporcionales",
            descripcion = "Con pesos (Modifier.weight) cada elemento recibe una parte proporcional del espacio. Mueve el deslizador para cambiar el peso de la caja central."
        ) {
            var peso by remember { mutableFloatStateOf(2f) }
            Row(modifier = Modifier.fillMaxWidth().height(64.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Caja("1", colores.primary, colores.onPrimary, Modifier.weight(1f).height(64.dp))
                Caja("${peso.toInt()}", colores.secondary, colores.onSecondary, Modifier.weight(peso).height(64.dp))
                Caja("1", colores.tertiary, colores.onTertiary, Modifier.weight(1f).height(64.dp))
            }
            Slider(value = peso, onValueChange = { peso = it }, valueRange = 1f..6f, steps = 4)
            Respuesta("Proporción 1 : ${peso.toInt()} : 1")
        }

        // Distribución con restricciones
        TarjetaElemento(
            titulo = "Distribución con restricciones",
            descripcion = "Con ConstraintLayout cada elemento se ancla a otros o a una guía. Aquí las dos cajas están ancladas a una guía vertical; mueve el deslizador para desplazar la guía."
        ) {
            var porcentaje by remember { mutableFloatStateOf(50f) }
            ConstraintLayout(modifier = Modifier.fillMaxWidth().height(80.dp)) {
                val (izquierda, derecha) = createRefs()
                // Guía vertical que reparte el espacio
                val guia = createGuidelineFromStart(porcentaje / 100f)
                // Caja anclada entre el borde izquierdo y la guía
                Caja(
                    "Izquierda", colores.primary, colores.onPrimary,
                    Modifier
                        .constrainAs(izquierda) {
                            start.linkTo(parent.start)
                            end.linkTo(guia, margin = 4.dp)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                        }
                        .height(64.dp)
                )
                // Caja anclada entre la guía y el borde derecho
                Caja(
                    "Derecha", colores.secondary, colores.onSecondary,
                    Modifier
                        .constrainAs(derecha) {
                            start.linkTo(guia, margin = 4.dp)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                        }
                        .height(64.dp)
                )
            }
            Slider(value = porcentaje, onValueChange = { porcentaje = it }, valueRange = 20f..80f, steps = 11)
            Respuesta("Guía al ${porcentaje.toInt()} % del ancho")
        }
    }
}
