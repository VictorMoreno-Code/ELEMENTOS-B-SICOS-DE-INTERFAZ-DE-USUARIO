package com.outlook.victoreduardo.xml_ui

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.outlook.victoreduardo.xml_ui.databinding.FragmentSeccion1Binding

// Sección 1: Entrada de texto
class Seccion1Fragment : Fragment() {

    private var _binding: FragmentSeccion1Binding? = null
    private val binding get() = _binding!!

    // Datos compartidos con las demás secciones
    private val vm by lazy { ViewModelProvider(requireActivity())[CatalogoViewModel::class.java] }

    // Ciudades para las sugerencias y la búsqueda
    private val ciudades = listOf(
        "Ciudad de México", "Guadalajara", "Monterrey", "Puebla", "Querétaro",
        "Toluca", "Mérida", "Tijuana", "León", "Oaxaca", "Guanajuato", "Morelia"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeccion1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarCampoSimple()
        configurarValidacion()
        configurarContrasena()
        configurarCorreo()
        configurarSugerencias()
        configurarBusqueda()
    }

    // Campo de texto simple
    private fun configurarCampoSimple() {
        // Muestra lo que se escribe mientras se teclea
        binding.etSimple.doAfterTextChanged { texto ->
            binding.tilSimple.error = null
            val contenido = texto?.toString().orEmpty()
            binding.tvEcoSimple.text =
                if (contenido.isEmpty()) "Escribiste: (nada todavía)" else "Escribiste: $contenido"
        }

        // Botón: agregar el texto a la lista de la Sección 4
        binding.btnAgregarLista.setOnClickListener {
            val contenido = binding.etSimple.text?.toString().orEmpty().trim()
            if (contenido.isEmpty()) {
                binding.tilSimple.error = "Escribe algo antes de agregarlo"
            } else {
                vm.agregar(contenido, "Sección 1")
                binding.etSimple.text?.clear()
                Snackbar.make(binding.root, "«$contenido» se agregó a la lista de la Sección 4", Snackbar.LENGTH_LONG)
                    .setAction("Ver") { findNavController().navigate(R.id.seccion4Fragment) }
                    .show()
            }
        }
    }

    // Campo con validación y mensaje de error visible
    private fun configurarValidacion() {
        binding.etValidacion.doAfterTextChanged { texto ->
            val usuario = texto?.toString().orEmpty()
            binding.tilValidacion.error = when {
                usuario.isEmpty() -> null
                usuario.contains(" ") -> "No se permiten espacios"
                usuario.length < 4 -> "Escribe al menos 4 caracteres"
                usuario.length > 12 -> "Máximo 12 caracteres"
                else -> null
            }
        }
    }

    // Campo de contraseña (el ícono del ojo lo pone el TextInputLayout)
    private fun configurarContrasena() {
        binding.etContrasena.doAfterTextChanged { texto ->
            val clave = texto?.toString().orEmpty()
            binding.tilContrasena.error =
                if (clave.isNotEmpty() && clave.length < 8) "La contraseña debe tener al menos 8 caracteres" else null
        }
    }

    // Campo de correo con validación de formato
    private fun configurarCorreo() {
        binding.etCorreo.doAfterTextChanged { texto ->
            val correo = texto?.toString().orEmpty()
            binding.tilCorreo.error =
                if (correo.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(correo).matches()) "Correo no válido" else null
        }
    }

    // Campo con sugerencias automáticas
    private fun configurarSugerencias() {
        val adaptador = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, ciudades)
        binding.acCiudad.setAdapter(adaptador)
        binding.acCiudad.setOnItemClickListener { _, _, posicion, _ ->
            val elegida = adaptador.getItem(posicion)
            binding.tvCiudadElegida.text = "Ciudad elegida: $elegida"
        }
    }

    // Barra de búsqueda
    private fun configurarBusqueda() {
        binding.tvResultadosBusqueda.text = "Resultados: ${ciudades.size} ciudades"

        // Filtra la lista mientras se escribe
        binding.etBusqueda.doAfterTextChanged { texto ->
            val consulta = texto?.toString().orEmpty().trim()
            val resultados = ciudades.filter { it.contains(consulta, ignoreCase = true) }
            binding.tvResultadosBusqueda.text = when {
                resultados.isEmpty() -> "Sin resultados para «$consulta»"
                consulta.isEmpty() -> "Resultados: ${ciudades.size} ciudades"
                else -> "Resultados (${resultados.size}): ${resultados.joinToString(", ")}"
            }
        }

        // Al tocar la lupa del teclado se oculta el teclado
        binding.etBusqueda.setOnEditorActionListener { vista, accion, _ ->
            if (accion == EditorInfo.IME_ACTION_SEARCH) {
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(vista.windowToken, 0)
                vista.clearFocus()
                true
            } else {
                false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
