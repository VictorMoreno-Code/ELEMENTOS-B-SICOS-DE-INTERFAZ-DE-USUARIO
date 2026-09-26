package com.outlook.victoreduardo.xml_ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.outlook.victoreduardo.xml_ui.databinding.FragmentSeccion5Binding
import com.outlook.victoreduardo.xml_ui.databinding.SheetOpcionesBinding
import java.net.HttpURLConnection
import java.net.URL

// Sección 5: Información y retroalimentación
class Seccion5Fragment : Fragment() {

    private var _binding: FragmentSeccion5Binding? = null
    private val binding get() = _binding!!

    // Datos compartidos con las demás secciones
    private val vm by lazy { ViewModelProvider(requireActivity())[CatalogoViewModel::class.java] }

    // Estado de las demostraciones
    private var tamanoTexto = 22f
    private var avisos = 3
    private var progresoSimulado = 0
    private var indiceImagen = 0
    private val idsImagen = listOf(10, 1015, 1018, 1039)
    private val manejador = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeccion5Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarConexionConSeccion3()
        configurarTextos()
        configurarImagenes()
        configurarProgreso()
        configurarMensajes()
        configurarDialogo()
        configurarHojaInferior()
        configurarTarjetaSeparadorYBadge()
    }

    // Conexión con la Sección 3: modo detallado y valor del deslizador
    private fun configurarConexionConSeccion3() {
        binding.btnIrSeccion3.setOnClickListener { findNavController().navigate(R.id.seccion3Fragment) }

        // Interruptor "Modo detallado"
        vm.modoDetallado.observe(viewLifecycleOwner) { detallado ->
            val visibilidad = if (detallado) View.VISIBLE else View.GONE
            binding.tvDetalleImagen.visibility = visibilidad
            binding.tvDetalleTarjeta.visibility = visibilidad
            binding.tvEstadoConexion.text =
                if (detallado) "Modo detallado: ACTIVADO (se muestra información adicional)"
                else "Modo detallado: desactivado (actívalo en la Sección 3)"
        }

        // Deslizador de la Sección 3 → barra de progreso lineal
        vm.nivel.observe(viewLifecycleOwner) { nivel ->
            binding.linealDeterminado.setProgressCompat(nivel, true)
            binding.tvNivel.text = "Lineal determinado: $nivel % (valor del deslizador de la Sección 3)"
        }
    }

    // Textos con distintos estilos, tamaños y énfasis
    private fun configurarTextos() {
        // Negrita, cursiva y subrayado (selección múltiple)
        binding.grupoEstilos.addOnButtonCheckedListener { _, _, _ ->
            val negrita = binding.btnNegrita.isChecked
            val cursiva = binding.btnCursiva.isChecked
            val subrayado = binding.btnSubrayado.isChecked

            val estilo = when {
                negrita && cursiva -> Typeface.BOLD_ITALIC
                negrita -> Typeface.BOLD
                cursiva -> Typeface.ITALIC
                else -> Typeface.NORMAL
            }
            binding.tvMuestra.setTypeface(null, estilo)
            binding.tvMuestra.paintFlags = if (subrayado) {
                binding.tvMuestra.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            } else {
                binding.tvMuestra.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
            }
        }

        // Tamaño del texto
        binding.tvTamano.text = "Tamaño: ${tamanoTexto.toInt()} sp"
        binding.btnMas.setOnClickListener { cambiarTamano(2f) }
        binding.btnMenos.setOnClickListener { cambiarTamano(-2f) }

        // Texto con varios estilos mezclados (negrita, color y tachado)
        val frase = "Un mismo texto con negrita, color y tachado."
        val texto = SpannableString(frase)
        val colorDestacado = MaterialColors.getColor(binding.root, com.google.android.material.R.attr.colorSecondary)
        aplicarEstilo(texto, frase, "negrita", StyleSpan(Typeface.BOLD))
        aplicarEstilo(texto, frase, "color", ForegroundColorSpan(colorDestacado))
        aplicarEstilo(texto, frase, "tachado", StrikethroughSpan())
        binding.tvSpans.text = texto
    }

    // Aplica un estilo a una palabra dentro de una frase
    private fun aplicarEstilo(texto: SpannableString, frase: String, palabra: String, estilo: Any) {
        val inicio = frase.indexOf(palabra)
        if (inicio >= 0) {
            texto.setSpan(estilo, inicio, inicio + palabra.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    // Cambia el tamaño del texto de muestra entre 12 y 40 sp
    private fun cambiarTamano(cambio: Float) {
        tamanoTexto = (tamanoTexto + cambio).coerceIn(12f, 40f)
        binding.tvMuestra.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoTexto)
        binding.tvTamano.text = "Tamaño: ${tamanoTexto.toInt()} sp"
    }

    // Imagen local e imagen cargada desde una URL
    private fun configurarImagenes() {
        // Modo de escalado (se aplica a las dos imágenes)
        binding.grupoEscala.addOnButtonCheckedListener { _, botonId, marcado ->
            if (marcado) {
                val modo: ImageView.ScaleType
                val nombre: String
                when (botonId) {
                    R.id.btnRecortar -> {
                        modo = ImageView.ScaleType.CENTER_CROP
                        nombre = "recortar para llenar (centerCrop)"
                    }
                    R.id.btnAjustar -> {
                        modo = ImageView.ScaleType.FIT_CENTER
                        nombre = "ajustar completa (fitCenter)"
                    }
                    else -> {
                        modo = ImageView.ScaleType.FIT_XY
                        nombre = "estirar al espacio (fitXY)"
                    }
                }
                binding.imgLocal.scaleType = modo
                binding.imgUrl.scaleType = modo
                binding.tvModoEscala.text = "Modo: $nombre"
            }
        }

        // Imagen desde URL
        binding.btnOtraImagen.setOnClickListener {
            indiceImagen = (indiceImagen + 1) % idsImagen.size
            cargarImagenUrl()
        }
        cargarImagenUrl()
    }

    // Descarga la imagen de internet en un hilo aparte y la muestra
    private fun cargarImagenUrl() {
        val direccion = "https://picsum.photos/id/${idsImagen[indiceImagen]}/800/450"
        binding.progresoUrl.visibility = View.VISIBLE
        binding.tvEstadoUrl.text = "Cargando imagen desde internet…"

        Thread {
            val imagen: Bitmap? = try {
                val conexion = URL(direccion).openConnection() as HttpURLConnection
                conexion.connectTimeout = 8000
                conexion.readTimeout = 8000
                val bitmap = conexion.inputStream.use { BitmapFactory.decodeStream(it) }
                conexion.disconnect()
                bitmap
            } catch (e: Exception) {
                null
            }

            activity?.runOnUiThread {
                if (_binding != null) {
                    binding.progresoUrl.visibility = View.GONE
                    if (imagen != null) {
                        binding.imgUrl.setImageBitmap(imagen)
                        binding.tvEstadoUrl.text = "Imagen cargada desde picsum.photos (foto ${indiceImagen + 1} de ${idsImagen.size})"
                    } else {
                        binding.tvEstadoUrl.text = "No se pudo cargar la imagen. Revisa tu conexión a internet."
                    }
                }
            }
        }.start()
    }

    // Indicadores de progreso
    private fun configurarProgreso() {
        binding.btnSimular.setOnClickListener {
            binding.btnSimular.isEnabled = false
            progresoSimulado = 0
            manejador.post(pasoDeSimulacion)
        }
    }

    // Avanza el progreso circular de 5 en 5 hasta llegar a 100
    private val pasoDeSimulacion = object : Runnable {
        override fun run() {
            if (_binding == null) return
            progresoSimulado += 5
            binding.circularDeterminado.setProgressCompat(progresoSimulado, true)
            binding.tvPorcentajeSimulado.text = "Circular determinado: $progresoSimulado %"
            if (progresoSimulado < 100) {
                manejador.postDelayed(this, 150)
            } else {
                binding.btnSimular.isEnabled = true
            }
        }
    }

    // Mensaje emergente (toast) y mensaje con acción (snackbar)
    private fun configurarMensajes() {
        binding.btnToast.setOnClickListener {
            Toast.makeText(requireContext(), "Este es un mensaje emergente (toast)", Toast.LENGTH_SHORT).show()
            binding.tvRespuestaMensajes.text = "Se mostró un toast"
        }
        binding.btnSnackbar.setOnClickListener {
            Snackbar.make(binding.root, "Mensaje con acción (snackbar)", Snackbar.LENGTH_LONG)
                .setAction("Deshacer") {
                    binding.tvRespuestaMensajes.text = "Tocaste la acción «Deshacer» del snackbar"
                }
                .show()
            binding.tvRespuestaMensajes.text = "Se mostró un snackbar con acción"
        }
    }

    // Diálogo de confirmación
    private fun configurarDialogo() {
        binding.btnDialogo.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("¿Eliminar elemento?")
                .setMessage("Esta acción no se puede deshacer. ¿Quieres continuar?")
                .setNegativeButton("Cancelar") { _, _ ->
                    binding.tvRespuestaDialogo.text = "Respuesta: cancelaste la acción"
                }
                .setPositiveButton("Eliminar") { _, _ ->
                    binding.tvRespuestaDialogo.text = "Respuesta: confirmaste la eliminación"
                }
                .show()
        }
    }

    // Hoja inferior (bottom sheet)
    private fun configurarHojaInferior() {
        binding.btnHoja.setOnClickListener {
            val hoja = BottomSheetDialog(requireContext())
            val contenido = SheetOpcionesBinding.inflate(layoutInflater)
            hoja.setContentView(contenido.root)

            contenido.opcionCompartir.setOnClickListener {
                binding.tvRespuestaHoja.text = "Elegiste: Compartir"
                hoja.dismiss()
            }
            contenido.opcionEditar.setOnClickListener {
                binding.tvRespuestaHoja.text = "Elegiste: Editar"
                hoja.dismiss()
            }
            contenido.opcionEliminar.setOnClickListener {
                binding.tvRespuestaHoja.text = "Elegiste: Eliminar"
                hoja.dismiss()
            }
            hoja.show()
        }
    }

    // Tarjeta, separador y distintivo numérico (badge)
    private fun configurarTarjetaSeparadorYBadge() {
        // Tarjeta que se marca al tocarla
        binding.cardEjemplo.setOnClickListener {
            binding.cardEjemplo.isChecked = !binding.cardEjemplo.isChecked
            binding.tvEstadoTarjeta.text =
                if (binding.cardEjemplo.isChecked) "Tarjeta marcada" else "Tarjeta sin marcar"
        }

        // Separador que se muestra u oculta
        binding.swDivisor.setOnCheckedChangeListener { _, marcado ->
            binding.divisorEjemplo.visibility = if (marcado) View.VISIBLE else View.INVISIBLE
        }

        // Distintivo numérico
        actualizarBadge()
        binding.btnSumarBadge.setOnClickListener {
            avisos++
            actualizarBadge()
        }
        binding.btnReiniciarBadge.setOnClickListener {
            avisos = 0
            actualizarBadge()
        }
    }

    // Muestra el número de avisos (se oculta cuando es cero)
    private fun actualizarBadge() {
        binding.tvBadge.text = if (avisos > 99) "99+" else avisos.toString()
        binding.tvBadge.visibility = if (avisos == 0) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        manejador.removeCallbacksAndMessages(null)
        _binding = null
    }
}
