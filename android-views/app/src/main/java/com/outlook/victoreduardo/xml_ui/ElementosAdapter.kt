package com.outlook.victoreduardo.xml_ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.outlook.victoreduardo.xml_ui.databinding.ItemElementoListaBinding

// Adaptador de la lista vertical de la Sección 4
class ElementosAdapter(
    private val alTocar: (ElementoLista) -> Unit
) : ListAdapter<ElementoLista, ElementosAdapter.Holder>(Comparador) {

    // Guarda las vistas de una fila
    class Holder(val binding: ItemElementoListaBinding) : RecyclerView.ViewHolder(binding.root)

    // Compara elementos para animar los cambios de la lista
    object Comparador : DiffUtil.ItemCallback<ElementoLista>() {
        override fun areItemsTheSame(a: ElementoLista, b: ElementoLista) = a.id == b.id
        override fun areContentsTheSame(a: ElementoLista, b: ElementoLista) = a == b
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemElementoListaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val elemento = getItem(position)
        holder.binding.tvInicial.text = elemento.titulo.take(1).uppercase()
        holder.binding.tvTituloElemento.text = elemento.titulo
        holder.binding.tvOrigenElemento.text = "Origen: ${elemento.origen}"
        holder.binding.root.setOnClickListener { alTocar(elemento) }
    }

    // Devuelve el elemento de una posición (se usa al deslizar para eliminar)
    fun elementoEn(posicion: Int): ElementoLista = getItem(posicion)
}
