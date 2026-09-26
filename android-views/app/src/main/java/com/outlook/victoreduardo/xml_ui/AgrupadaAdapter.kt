package com.outlook.victoreduardo.xml_ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.outlook.victoreduardo.xml_ui.databinding.ItemEncabezadoBinding
import com.outlook.victoreduardo.xml_ui.databinding.ItemFilaBinding

// Los dos tipos de elemento de la lista con encabezados
sealed class FilaAgrupada {
    data class Encabezado(val titulo: String) : FilaAgrupada()
    data class Fila(val titulo: String, val detalle: String) : FilaAgrupada()
}

// Adaptador de la lista con encabezados de sección (Sección 4)
class AgrupadaAdapter(
    private val filas: List<FilaAgrupada>,
    private val alTocar: (FilaAgrupada.Fila) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private class HolderEncabezado(val binding: ItemEncabezadoBinding) : RecyclerView.ViewHolder(binding.root)
    private class HolderFila(val binding: ItemFilaBinding) : RecyclerView.ViewHolder(binding.root)

    // Tipo 0 = encabezado, tipo 1 = fila
    override fun getItemViewType(position: Int): Int =
        if (filas[position] is FilaAgrupada.Encabezado) 0 else 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == 0) {
            HolderEncabezado(ItemEncabezadoBinding.inflate(inflater, parent, false))
        } else {
            HolderFila(ItemFilaBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val elemento = filas[position]) {
            is FilaAgrupada.Encabezado -> {
                (holder as HolderEncabezado).binding.tvEncabezado.text = elemento.titulo
            }
            is FilaAgrupada.Fila -> {
                val fila = holder as HolderFila
                fila.binding.tvTituloFila.text = elemento.titulo
                fila.binding.tvDetalleFila.text = elemento.detalle
                fila.binding.root.setOnClickListener { alTocar(elemento) }
            }
        }
    }

    override fun getItemCount() = filas.size
}
