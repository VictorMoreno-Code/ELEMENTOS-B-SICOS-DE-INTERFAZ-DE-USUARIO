package com.outlook.victoreduardo.xml_ui

import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.outlook.victoreduardo.xml_ui.databinding.FragmentSeccion4Binding

// Sección 4: Listas y colecciones
class Seccion4Fragment : Fragment() {

    private var _binding: FragmentSeccion4Binding? = null
    private val binding get() = _binding!!

    // Datos compartidos con las demás secciones
    private val vm by lazy { ViewModelProvider(requireActivity())[CatalogoViewModel::class.java] }

    private lateinit var adaptador: ElementosAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeccion4Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarListaVertical()
        configurarCuadricula()
        configurarListaAgrupada()
        configurarPestanas()
    }

    // Lista vertical con detalle, deslizar para eliminar, actualizar y estado vacío
    private fun configurarListaVertical() {
        // Al tocar un elemento se muestra su detalle
        adaptador = ElementosAdapter { elemento ->
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(elemento.titulo)
                .setMessage("${elemento.detalle}\n\nOrigen: ${elemento.origen}")
                .setPositiveButton("Cerrar", null)
                .show()
        }
        binding.rvLista.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLista.adapter = adaptador

        // Observa la lista compartida (aquí llegan los elementos agregados en la Sección 1)
        vm.elementos.observe(viewLifecycleOwner) { lista ->
            val crecio = lista.size > adaptador.itemCount
            adaptador.submitList(lista) {
                if (crecio && _binding != null) binding.rvLista.scrollToPosition(0)
            }
            binding.tvContador.text = "${lista.size} elementos en la lista"

            // Estado vacío cuando no quedan elementos
            val vacia = lista.isEmpty()
            binding.estadoVacio.visibility = if (vacia) View.VISIBLE else View.GONE
            binding.swipeRefresh.visibility = if (vacia) View.GONE else View.VISIBLE
        }

        // Deslizar un elemento para eliminarlo
        val colorFondoBorrado = MaterialColors.getColor(binding.root, com.google.android.material.R.attr.colorErrorContainer)
        val pincel = Paint().apply { color = colorFondoBorrado }
        val gestos = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val posicion = viewHolder.bindingAdapterPosition
                if (posicion == RecyclerView.NO_POSITION) return
                val elemento = adaptador.elementoEn(posicion)
                vm.eliminar(elemento.id)
                Snackbar.make(binding.root, "«${elemento.titulo}» eliminado", Snackbar.LENGTH_LONG)
                    .setAction("Deshacer") { vm.insertar(posicion, elemento) }
                    .show()
            }

            // Dibuja un fondo de color detrás del elemento mientras se desliza
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val fila = viewHolder.itemView
                if (dX > 0) {
                    c.drawRect(fila.left.toFloat(), fila.top.toFloat(), fila.left + dX, fila.bottom.toFloat(), pincel)
                } else if (dX < 0) {
                    c.drawRect(fila.right + dX, fila.top.toFloat(), fila.right.toFloat(), fila.bottom.toFloat(), pincel)
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
        ItemTouchHelper(gestos).attachToRecyclerView(binding.rvLista)

        // Actualizar la lista arrastrando hacia abajo
        binding.swipeRefresh.setColorSchemeColors(
            MaterialColors.getColor(binding.root, androidx.appcompat.R.attr.colorPrimary)
        )
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.postDelayed({
                if (_binding != null) {
                    vm.actualizar()
                    binding.swipeRefresh.isRefreshing = false
                    Snackbar.make(binding.root, "Lista actualizada", Snackbar.LENGTH_SHORT).show()
                }
            }, 1200)
        }

        // Botones para vaciar y restaurar la lista
        binding.btnVaciar.setOnClickListener { vm.vaciar() }
        binding.btnRestaurar.setOnClickListener { vm.restaurar() }
    }

    // Cuadrícula de elementos
    private fun configurarCuadricula() {
        val mosaicos = (1..12).map { "Mosaico $it" }
        binding.rvCuadricula.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvCuadricula.adapter = CuadriculaAdapter(mosaicos) { nombre ->
            binding.tvRespuestaCuadricula.text = "Seleccionaste: $nombre"
        }
    }

    // Lista con encabezados de sección (dos tipos de elemento)
    private fun configurarListaAgrupada() {
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
        binding.rvAgrupada.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAgrupada.adapter = AgrupadaAdapter(filas) { fila ->
            binding.tvRespuestaAgrupada.text = "Elegiste: ${fila.titulo} (${fila.detalle})"
        }
    }

    // Pestañas con contenido deslizable
    private fun configurarPestanas() {
        val paginas = listOf(
            PaginaInfo("Resumen", "Primera página. Desliza hacia la izquierda para ver la siguiente."),
            PaginaInfo("Detalles", "Segunda página. Las pestañas y el deslizamiento están sincronizados."),
            PaginaInfo("Ajustes", "Tercera página. Cada página puede tener sus propios elementos.")
        )
        binding.viewPager.adapter = PaginasAdapter(paginas)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { pestana, posicion ->
            pestana.text = paginas[posicion].titulo
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
