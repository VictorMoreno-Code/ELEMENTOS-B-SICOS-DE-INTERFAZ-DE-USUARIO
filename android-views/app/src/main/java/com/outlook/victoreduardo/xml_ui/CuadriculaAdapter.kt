package com.outlook.victoreduardo.xml_ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.outlook.victoreduardo.xml_ui.databinding.ItemCuadriculaBinding

// Adaptador de la cuadrícula de la Sección 4
class CuadriculaAdapter(
    private val nombres: List<String>,
    private val alTocar: (String) -> Unit
) : RecyclerView.Adapter<CuadriculaAdapter.Holder>() {

    class Holder(val binding: ItemCuadriculaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemCuadriculaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val nombre = nombres[position]
        holder.binding.tvNombreMosaico.text = nombre
        holder.binding.root.setOnClickListener { alTocar(nombre) }
    }

    override fun getItemCount() = nombres.size
}
