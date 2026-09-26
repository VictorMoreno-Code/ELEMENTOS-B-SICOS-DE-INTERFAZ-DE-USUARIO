package com.outlook.victoreduardo.xml_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.outlook.victoreduardo.xml_ui.databinding.FragmentSeccion2Binding

// Sección 2: Botones y acciones
class Seccion2Fragment : Fragment() {

    private var _binding: FragmentSeccion2Binding? = null
    private val binding get() = _binding!!

    // Contadores de pulsaciones
    private var vecesRelleno = 0
    private var vecesContorno = 0
    private var vecesTexto = 0
    private var vecesFab = 0
    private var favorito = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeccion2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarBotonesBasicos()
        configurarBotonesConIcono()
        configurarBotonesFlotantes()
        configurarAlternancia()
        configurarEstadosDeBoton()
    }

    // Botón relleno, con contorno y de solo texto
    private fun configurarBotonesBasicos() {
        binding.btnRelleno.setOnClickListener {
            vecesRelleno++
            binding.tvRespuestaBasicos.text = "Botón relleno pulsado $vecesRelleno veces"
        }
        binding.btnContorno.setOnClickListener {
            vecesContorno++
            binding.tvRespuestaBasicos.text = "Botón con contorno pulsado $vecesContorno veces"
        }
        binding.btnTexto.setOnClickListener {
            vecesTexto++
            binding.tvRespuestaBasicos.text = "Botón de solo texto pulsado $vecesTexto veces"
        }
    }

    // Botón con ícono (solo ícono y con ícono más texto)
    private fun configurarBotonesConIcono() {
        binding.btnIconoFavorito.setOnClickListener {
            favorito = !favorito
            binding.tvRespuestaIconos.text =
                if (favorito) "Ícono de corazón: marcado como favorito" else "Ícono de corazón: favorito quitado"
        }
        binding.btnIconoEditar.setOnClickListener {
            binding.tvRespuestaIconos.text = "Ícono de lápiz: modo de edición"
        }
        binding.btnIconoTexto.setOnClickListener {
            Snackbar.make(binding.root, "Se compartió el contenido de ejemplo", Snackbar.LENGTH_SHORT).show()
            binding.tvRespuestaIconos.text = "Botón con ícono y texto: compartir"
        }
    }

    // Botón de acción flotante, normal y extendido
    private fun configurarBotonesFlotantes() {
        binding.fabNormal.setOnClickListener {
            vecesFab++
            binding.tvRespuestaFab.text = "FAB normal: $vecesFab toques"
        }
        binding.fabExtendido.setOnClickListener {
            // Contrae o expande el botón extendido
            if (binding.fabExtendido.isExtended) {
                binding.fabExtendido.shrink()
                binding.tvRespuestaFab.text = "FAB extendido: contraído"
            } else {
                binding.fabExtendido.extend()
                binding.tvRespuestaFab.text = "FAB extendido: expandido"
            }
        }
    }

    // Botón de alternancia (toggle) y selector segmentado
    private fun configurarAlternancia() {
        // Selector segmentado
        binding.grupoSegmentado.addOnButtonCheckedListener { _, botonId, estaMarcado ->
            if (estaMarcado) {
                val vista = when (botonId) {
                    R.id.btnDia -> "Día"
                    R.id.btnSemana -> "Semana"
                    else -> "Mes"
                }
                binding.tvRespuestaAlternancia.text = "Vista elegida: $vista"
            }
        }

        // Botón de alternancia
        binding.btnAlternar.addOnCheckedChangeListener { _, marcado ->
            binding.tvRespuestaAlternancia.text =
                if (marcado) "Recordatorio: activado" else "Recordatorio: desactivado"
        }
    }

    // Botón deshabilitado y botón en estado de carga
    private fun configurarEstadosDeBoton() {
        // El botón deshabilitado no responde; este otro cambia su estado
        binding.btnDeshabilitado.setOnClickListener {
            binding.tvRespuestaEstados.text = "Ahora sí: el botón habilitado respondió"
        }
        binding.btnCambiarEstado.setOnClickListener {
            val habilitar = !binding.btnDeshabilitado.isEnabled
            binding.btnDeshabilitado.isEnabled = habilitar
            binding.btnDeshabilitado.text = if (habilitar) "Habilitado" else "Deshabilitado"
            binding.btnCambiarEstado.text = if (habilitar) "Deshabilitar" else "Habilitar"
            binding.tvRespuestaEstados.text =
                if (habilitar) "Botón habilitado: ya puedes tocarlo" else "Botón deshabilitado: no responde"
        }

        // Botón en estado de carga: simula guardar durante 2 segundos
        binding.btnCarga.setOnClickListener {
            binding.btnCarga.isEnabled = false
            binding.btnCarga.text = "Guardando…"
            binding.progresoBoton.visibility = View.VISIBLE
            binding.tvRespuestaEstados.text = "Guardando cambios, espera un momento"

            binding.btnCarga.postDelayed({
                if (_binding != null) {
                    binding.btnCarga.isEnabled = true
                    binding.btnCarga.text = "Guardar cambios"
                    binding.progresoBoton.visibility = View.GONE
                    binding.tvRespuestaEstados.text = "Cambios guardados correctamente"
                }
            }, 2000)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
