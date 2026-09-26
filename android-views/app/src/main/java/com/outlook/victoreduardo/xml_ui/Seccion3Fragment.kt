package com.outlook.victoreduardo.xml_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.outlook.victoreduardo.xml_ui.databinding.FragmentSeccion3Binding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

// Sección 3: Elementos de selección
class Seccion3Fragment : Fragment() {

    private var _binding: FragmentSeccion3Binding? = null
    private val binding get() = _binding!!

    // Datos compartidos con las demás secciones
    private val vm by lazy { ViewModelProvider(requireActivity())[CatalogoViewModel::class.java] }

    // Evita que los cambios hechos por código disparen los listeners
    private var bloqueo = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeccion3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarCasillas()
        configurarRadio()
        configurarInterruptores()
        configurarDeslizadores()
        configurarListaDesplegable()
        configurarFechaYHora()
        configurarChips()
    }

    // Casilla de verificación (incluye el estado indeterminado)
    private fun configurarCasillas() {
        // Casilla simple
        binding.cbTerminos.setOnCheckedChangeListener { _, marcada ->
            binding.tvRespuestaCasillas.text =
                if (marcada) "Términos aceptados" else "Términos sin aceptar"
        }

        val hijas = listOf(binding.cbHijo1, binding.cbHijo2, binding.cbHijo3)

        // Casilla principal: marca o desmarca todas las hijas
        binding.cbPadre.addOnCheckedStateChangedListener { _, estado ->
            if (!bloqueo) {
                bloqueo = true
                val marcar = estado == MaterialCheckBox.STATE_CHECKED
                hijas.forEach { it.isChecked = marcar }
                bloqueo = false
                actualizarCasillaPrincipal(hijas)
            }
        }

        // Casillas hijas: recalculan el estado de la principal
        hijas.forEach { casilla ->
            casilla.setOnCheckedChangeListener { _, _ ->
                if (!bloqueo) actualizarCasillaPrincipal(hijas)
            }
        }

        actualizarCasillaPrincipal(hijas)
    }

    // Calcula si la casilla principal está marcada, desmarcada o indeterminada
    private fun actualizarCasillaPrincipal(hijas: List<MaterialCheckBox>) {
        val marcadas = hijas.count { it.isChecked }
        bloqueo = true
        binding.cbPadre.checkedState = when (marcadas) {
            0 -> MaterialCheckBox.STATE_UNCHECKED
            hijas.size -> MaterialCheckBox.STATE_CHECKED
            else -> MaterialCheckBox.STATE_INDETERMINATE
        }
        bloqueo = false
        binding.tvRespuestaCasillas.text = "Avisos activados: $marcadas de ${hijas.size}"
    }

    // Grupo de botones de opción mutuamente excluyentes
    private fun configurarRadio() {
        binding.rgEnvio.setOnCheckedChangeListener { _, idElegido ->
            val texto = when (idElegido) {
                R.id.rbRapido -> "Envío rápido (1 día)"
                R.id.rbEstandar -> "Envío estándar (3 días)"
                else -> "Envío económico (7 días)"
            }
            binding.tvRespuestaRadio.text = "Elegiste: $texto"
        }
    }

    // Interruptor (switch)
    private fun configurarInterruptores() {
        // Interruptor conectado con la Sección 5
        binding.swDetallado.isChecked = vm.modoDetallado.value == true
        binding.tvRespuestaSwitch.text = textoInterruptores()
        binding.swDetallado.setOnCheckedChangeListener { _, marcado ->
            vm.modoDetallado.value = marcado
            binding.tvRespuestaSwitch.text = textoInterruptores()
        }

        // Interruptor independiente
        binding.swNotificaciones.setOnCheckedChangeListener { _, _ ->
            binding.tvRespuestaSwitch.text = textoInterruptores()
        }
    }

    // Texto de estado de los dos interruptores
    private fun textoInterruptores(): String {
        val detallado = if (binding.swDetallado.isChecked) "activado" else "desactivado"
        val avisos = if (binding.swNotificaciones.isChecked) "activadas" else "desactivadas"
        return "Modo detallado: $detallado · Notificaciones: $avisos"
    }

    // Deslizador de valor único y deslizador de rango
    private fun configurarDeslizadores() {
        // Deslizador simple (su valor se muestra en la Sección 5)
        binding.sliderUnico.value = (vm.nivel.value ?: 40).toFloat()
        binding.tvValorSlider.text = "Nivel: ${binding.sliderUnico.value.toInt()} (se muestra en la Sección 5)"
        binding.sliderUnico.addOnChangeListener { _, valor, _ ->
            vm.nivel.value = valor.toInt()
            binding.tvValorSlider.text = "Nivel: ${valor.toInt()} (se muestra en la Sección 5)"
        }

        // Deslizador de rango
        actualizarTextoRango()
        binding.sliderRango.addOnChangeListener { _, _, _ -> actualizarTextoRango() }
    }

    // Muestra el mínimo y el máximo elegidos en el deslizador de rango
    private fun actualizarTextoRango() {
        val valores = binding.sliderRango.values
        binding.tvValorRango.text = "Rango: de ${valores[0].toInt()} a ${valores[1].toInt()}"
    }

    // Lista desplegable de selección
    private fun configurarListaDesplegable() {
        val carreras = listOf(
            "Ingeniería en Sistemas Computacionales",
            "Ingeniería en Inteligencia Artificial",
            "Licenciatura en Ciencia de Datos"
        )
        val adaptador = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, carreras)
        binding.acCarrera.setAdapter(adaptador)
        binding.acCarrera.setOnItemClickListener { _, _, posicion, _ ->
            binding.tvRespuestaCarrera.text = "Elegiste: ${carreras[posicion]}"
        }
        binding.tvRespuestaCarrera.text = "Toca el campo para elegir una carrera"
    }

    // Selector de fecha y selector de hora
    private fun configurarFechaYHora() {
        // Selector de fecha
        binding.btnFecha.setOnClickListener {
            val selectorFecha = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecciona una fecha")
                .setPositiveButtonText("Aceptar")
                .setNegativeButtonText("Cancelar")
                .build()
            selectorFecha.addOnPositiveButtonClickListener { milisegundos ->
                val formato = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale.forLanguageTag("es-MX"))
                formato.timeZone = TimeZone.getTimeZone("UTC")
                binding.tvRespuestaFecha.text = "Fecha: ${formato.format(milisegundos)}"
            }
            selectorFecha.show(childFragmentManager, "selector_fecha")
        }

        // Selector de hora
        binding.btnHora.setOnClickListener {
            val selectorHora = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Selecciona una hora")
                .setPositiveButtonText("Aceptar")
                .setNegativeButtonText("Cancelar")
                .build()
            selectorHora.addOnPositiveButtonClickListener {
                binding.tvRespuestaHora.text =
                    "Hora: " + String.format(Locale.US, "%02d:%02d", selectorHora.hour, selectorHora.minute)
            }
            selectorHora.show(childFragmentManager, "selector_hora")
        }
    }

    // Chips de filtro seleccionables
    private fun configurarChips() {
        binding.chipGroupFiltros.setOnCheckedStateChangeListener { grupo, idsMarcados ->
            val nombres = idsMarcados.map { grupo.findViewById<Chip>(it).text.toString() }
            binding.tvRespuestaChips.text =
                if (nombres.isEmpty()) "Sin filtros activos" else "Filtros activos: ${nombres.joinToString(", ")}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
