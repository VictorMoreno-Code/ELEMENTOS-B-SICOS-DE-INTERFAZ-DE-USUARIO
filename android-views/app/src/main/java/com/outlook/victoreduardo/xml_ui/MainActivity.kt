package com.outlook.victoreduardo.xml_ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.outlook.victoreduardo.xml_ui.databinding.ActivityMainBinding
import kotlin.math.max

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Pantalla completa (la app dibuja debajo de las barras del sistema)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ajuste de márgenes para la barra de navegación del sistema y el teclado
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { vista, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val teclado = insets.getInsets(WindowInsetsCompat.Type.ime())
            vista.setPadding(barras.left, 0, barras.right, max(barras.bottom, teclado.bottom))
            insets
        }

        // Barra superior
        setSupportActionBar(binding.toolbar)

        // Navegación entre inicio y las seis secciones
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController

        // Todos los destinos son de nivel superior: muestran el ícono del menú lateral
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.inicioFragment,
                R.id.seccion1Fragment,
                R.id.seccion2Fragment,
                R.id.seccion3Fragment,
                R.id.seccion4Fragment,
                R.id.seccion5Fragment,
                R.id.seccion6Fragment
            ),
            binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Menú lateral conectado con el grafo de navegación
        binding.navView.setupWithNavController(navController)
    }

    // Crea las acciones de la barra superior
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // Atiende las acciones de la barra superior
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_acerca -> {
                mostrarAcercaDe()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Diálogo con los datos de la práctica
    private fun mostrarAcercaDe() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Acerca de UIViewcraft")
            .setMessage(
                "Tarea 2: Elementos básicos de interfaz de usuario\n\n" +
                    "Versión: Android nativo con Views y XML (Kotlin)\n" +
                    "Alumno: Moreno López Victor Eduardo\n" +
                    "Boleta: 2024630639\n" +
                    "Grupo: 7CV4\n" +
                    "Profesor: Gabriel Hurtado Avilés"
            )
            .setPositiveButton("Cerrar", null)
            .show()
    }

    // Botón de regreso / menú lateral
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
