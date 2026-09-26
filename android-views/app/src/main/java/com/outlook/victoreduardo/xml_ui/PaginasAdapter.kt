package com.outlook.victoreduardo.xml_ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.outlook.victoreduardo.xml_ui.databinding.ItemPaginaBinding

// Contenido de una página de las pestañas
class PaginaInfo(val titulo: String, val texto: String)

// Adaptador del ViewPager2 (pestañas deslizables de la Sección 4)
class PaginasAdapter(
    private val paginas: List<PaginaInfo>
) : RecyclerView.Adapter<PaginasAdapter.Holder>() {

    class Holder(val binding: ItemPaginaBinding) : RecyclerView.ViewHolder(binding.root)

    // "Me gusta" de cada página
    private val megusta = IntArray(paginas.size)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemPaginaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val pagina = paginas[position]
        holder.binding.tvTituloPagina.text = pagina.titulo
        holder.binding.tvTextoPagina.text = pagina.texto
        holder.binding.btnMeGusta.text = "Me gusta (${megusta[position]})"
        holder.binding.btnMeGusta.setOnClickListener {
            megusta[position]++
            holder.binding.btnMeGusta.text = "Me gusta (${megusta[position]})"
        }
    }

    override fun getItemCount() = paginas.size
}
