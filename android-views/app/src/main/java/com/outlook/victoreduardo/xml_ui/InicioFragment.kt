package com.outlook.victoreduardo.xml_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.outlook.victoreduardo.xml_ui.databinding.FragmentInicioBinding
import com.outlook.victoreduardo.xml_ui.databinding.ItemSeccionBinding

// Pantalla principal: presenta la app y permite entrar a las seis secciones
class InicioFragment : Fragment() {

    private var _binding: FragmentInicioBinding? = null
    private val binding get() = _binding!!

    // Datos de cada tarjeta de sección
    private class Seccion(val numero: Int, val nombre: String, val resumen: String, val destino: Int)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInicioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Las seis secciones del catálogo
        val secciones = listOf(
            Seccion(1, "Entrada de texto", "Campos, validación, contraseña, teclados y búsqueda", R.id.seccion1Fragment),
            Seccion(2, "Botones y acciones", "Botones, íconos, FAB, alternancia y carga", R.id.seccion2Fragment),
            Seccion(3, "Elementos de selección", "Casillas, opciones, interruptor, deslizadores, fecha y chips", R.id.seccion3Fragment),
            Seccion(4, "Listas y colecciones", "Listas, cuadrícula, detalle, deslizar para borrar y pestañas", R.id.seccion4Fragment),
            Seccion(5, "Información y retroalimentación", "Textos, imágenes, progreso, mensajes, diálogo y bottom sheet", R.id.seccion5Fragment),
            Seccion(6, "Contenedores y estructura", "Fila, columna, superpuesta, barra superior, navegación y pesos", R.id.seccion6Fragment)
        )

        // Tarjetas de sección
        for (seccion in secciones) {
            val item = ItemSeccionBinding.inflate(layoutInflater, binding.contenedorSecciones, false)
            item.tvNumero.text = seccion.numero.toString()
            item.tvNombre.text = seccion.nombre
            item.tvResumen.text = seccion.resumen
            item.root.setOnClickListener { findNavController().navigate(seccion.destino) }
            binding.contenedorSecciones.addView(item.root)
        }

        // Botón para probar la conexión entre secciones
        binding.btnProbarConexion.setOnClickListener {
            findNavController().navigate(R.id.seccion1Fragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
