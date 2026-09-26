package com.outlook.victoreduardo.compose_ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.outlook.victoreduardo.compose_ui.ui.theme.DegradadoFin
import com.outlook.victoreduardo.compose_ui.ui.theme.DegradadoInicio
import kotlinx.coroutines.launch

// Destinos de la app: inicio y las seis secciones
enum class Destino(val titulo: String, val icono: ImageVector) {
    Inicio("Inicio", Icons.Default.Home),
    Seccion1("1. Entrada de texto", Icons.Default.Edit),
    Seccion2("2. Botones y acciones", Icons.Default.Star),
    Seccion3("3. Elementos de selección", Icons.Default.Check),
    Seccion4("4. Listas y colecciones", Icons.AutoMirrored.Filled.List),
    Seccion5("5. Información y retroalimentación", Icons.Default.Info),
    Seccion6("6. Contenedores y estructura", Icons.Default.Dashboard)
}

// Estructura principal: menú lateral + barra superior + contenido
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppPrincipal(vm: CatalogoViewModel) {
    val estadoMenu = rememberDrawerState(DrawerValue.Closed)
    val alcance = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }
    var destino by rememberSaveable { mutableStateOf(Destino.Inicio) }
    var mostrarAcerca by remember { mutableStateOf(false) }

    // El botón atrás cierra el menú o regresa al inicio
    BackHandler(enabled = estadoMenu.isOpen || destino != Destino.Inicio) {
        if (estadoMenu.isOpen) {
            alcance.launch { estadoMenu.close() }
        } else {
            destino = Destino.Inicio
        }
    }

    CompositionLocalProvider(
        LocalSnackbar provides snackbar,
        LocalNavegar provides { nuevo -> destino = nuevo }
    ) {
        // Menú lateral
        ModalNavigationDrawer(
            drawerState = estadoMenu,
            drawerContent = {
                ModalDrawerSheet {
                    // Encabezado con degradado azul
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Brush.linearGradient(listOf(DegradadoInicio, DegradadoFin)))
                            .padding(start = 20.dp, end = 20.dp, top = 48.dp, bottom = 20.dp),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Text(
                            "UIvibe Compose",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            "Catálogo de elementos de interfaz con Jetpack Compose",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    // Opciones del menú
                    Destino.entries.forEach { opcion ->
                        NavigationDrawerItem(
                            label = { Text(opcion.titulo) },
                            icon = { Icon(opcion.icono, contentDescription = null) },
                            selected = destino == opcion,
                            onClick = {
                                destino = opcion
                                alcance.launch { estadoMenu.close() }
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            }
        ) {
            Scaffold(
                topBar = {
                    // Barra superior con título y acciones
                    TopAppBar(
                        title = { Text(destino.titulo, maxLines = 1) },
                        navigationIcon = {
                            IconButton(onClick = { alcance.launch { estadoMenu.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                            }
                        },
                        actions = {
                            IconButton(onClick = { mostrarAcerca = true }) {
                                Icon(Icons.Default.Info, contentDescription = "Acerca de")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                            navigationIconContentColor = MaterialTheme.colorScheme.primary,
                            actionIconContentColor = MaterialTheme.colorScheme.primary
                        )
                    )
                },
                snackbarHost = { SnackbarHost(snackbar) },
                containerColor = MaterialTheme.colorScheme.background
            ) { relleno ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(relleno),
                    contentAlignment = Alignment.TopStart
                ) {
                    when (destino) {
                        Destino.Inicio -> Inicio()
                        Destino.Seccion1 -> Seccion1(vm)
                        Destino.Seccion2 -> Seccion2()
                        Destino.Seccion3 -> Seccion3(vm)
                        Destino.Seccion4 -> Seccion4(vm)
                        Destino.Seccion5 -> Seccion5(vm)
                        Destino.Seccion6 -> Seccion6()
                    }
                }
            }
        }
    }

    // Diálogo "Acerca de" con los datos de la práctica
    if (mostrarAcerca) {
        AlertDialog(
            onDismissRequest = { mostrarAcerca = false },
            title = { Text("Acerca de UIvibe Compose") },
            text = {
                Text(
                    "Tarea 2: Elementos básicos de interfaz de usuario\n\n" +
                        "Versión: Jetpack Compose (Kotlin)\n" +
                        "Alumno: Moreno López Victor Eduardo\n" +
                        "Boleta: 2024630639\n" +
                        "Grupo: 7CV4\n" +
                        "Profesor: Gabriel Hurtado Avilés"
                )
            },
            confirmButton = {
                TextButton(onClick = { mostrarAcerca = false }) { Text("Cerrar") }
            }
        )
    }
}
