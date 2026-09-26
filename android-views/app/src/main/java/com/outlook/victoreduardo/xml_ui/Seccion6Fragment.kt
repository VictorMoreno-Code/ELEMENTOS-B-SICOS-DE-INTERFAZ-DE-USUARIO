package com.outlook.victoreduardo.xml_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.outlook.victoreduardo.xml_ui.databinding.FragmentSeccion6Binding

// Sección 6: Contenedores y estructura
class Seccion6Fragment : Fragment() {

    private var _binding: FragmentSeccion6Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeccion6Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarFilaYColumna()
        configurarSuperpuesta()
        configurarDesplazamiento()
        configurarBarraSuperior()
        configurarNavegacionInferior()
        configurarPesos()
        configurarRestricciones()
    }

    // Distribución en fila y en columna
    private fun configurarFilaYColumna() {
        // Cambia la orientación del contenedor
        binding.grupoOrientacion.addOnButtonCheckedListener { _, botonId, marcado ->
            if (marcado) {
                val enFila = botonId == R.id.btnFila
                binding.contenedorCajas.orientation = if (enFila) LinearLayout.HORIZONTAL else LinearLayout.VERTICAL
                binding.tvRespuestaCajas.text = if (enFila) "Orientación: fila" else "Orientación: columna"
            }
        }

        // Al tocar una caja se muestra su nombre
        binding.cajaA.setOnClickListener { binding.tvRespuestaCajas.text = "Tocaste la caja A" }
        binding.cajaB.setOnClickListener { binding.tvRespuestaCajas.text = "Tocaste la caja B" }
        binding.cajaC.setOnClickListener { binding.tvRespuestaCajas.text = "Tocaste la caja C" }
    }

    // Distribución superpuesta
    private fun configurarSuperpuesta() {
        val cajas = listOf(binding.pila1, binding.pila2, binding.pila3)
        cajas.forEachIndexed { indice, caja ->
            caja.setOnClickListener {
                // Trae la caja tocada al frente de la pila
                (caja.parent as ViewGroup).bringChildToFront(caja)
                binding.tvRespuestaPila.text = "Al frente: caja ${indice + 1}"
            }
        }
    }

    // Contenedor con desplazamiento vertical
    private fun configurarDesplazamiento() {
        binding.tvContenidoScroll.text = (1..20).joinToString("\n") { "Renglón $it de 20" }
        binding.btnIrInicio.setOnClickListener { binding.scrollDemo.smoothScrollTo(0, 0) }
        binding.btnIrFinal.setOnClickListener {
            binding.scrollDemo.smoothScrollTo(0, binding.tvContenidoScroll.height)
        }
    }

    // Barra superior con título y acciones
    private fun configurarBarraSuperior() {
        // Acciones de la barra (buscar, favorito y menú de tres puntos)
        binding.toolbarDemo.inflateMenu(R.menu.menu_demo_toolbar)
        binding.toolbarDemo.setNavigationOnClickListener {
            binding.tvRespuestaBarra.text = "Tocaste el botón de navegación (menú)"
        }
        binding.toolbarDemo.setOnMenuItemClickListener { accion ->
            binding.tvRespuestaBarra.text = when (accion.itemId) {
                R.id.demo_buscar -> "Acción: Buscar"
                R.id.demo_favorito -> "Acción: Favorito"
                R.id.demo_compartir -> "Acción del menú: Compartir"
                else -> "Acción del menú: Ayuda"
            }
            true
        }
    }

    // Barra de navegación inferior
    private fun configurarNavegacionInferior() {
        // Distintivo con el número de avisos en "Perfil"
        binding.bottomNavDemo.getOrCreateBadge(R.id.demo_perfil).number = 3

        binding.bottomNavDemo.setOnItemSelectedListener { opcion ->
            val nombre = when (opcion.itemId) {
                R.id.demo_inicio -> "Inicio"
                R.id.demo_favoritos -> "Favoritos"
                else -> {
                    // Al entrar a Perfil se quitan los avisos
                    binding.bottomNavDemo.removeBadge(R.id.demo_perfil)
                    "Perfil"
                }
            }
            binding.tvPantallaNav.text = "Estás en: $nombre"
            true
        }
    }

    // Distribución con pesos proporcionales
    private fun configurarPesos() {
        binding.sliderPeso.addOnChangeListener { _, valor, _ ->
            val parametros = binding.cajaPesoCentro.layoutParams as LinearLayout.LayoutParams
            parametros.weight = valor
            binding.cajaPesoCentro.layoutParams = parametros
            binding.tvRespuestaPeso.text = "Proporción 1 : ${valor.toInt()} : 1"
        }
    }

    // Distribución con restricciones (guía en ConstraintLayout)
    private fun configurarRestricciones() {
        binding.sliderGuia.addOnChangeListener { _, valor, _ ->
            binding.guia.setGuidelinePercent(valor / 100f)
            binding.tvRespuestaGuia.text = "Guía al ${valor.toInt()} % del ancho"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
