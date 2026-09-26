package com.outlook.victoreduardo.xml_ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.withStyledAttributes
import com.google.android.material.card.MaterialCardView
import com.outlook.victoreduardo.xml_ui.databinding.ViewTarjetaElementoBinding

// Tarjeta de documentación: muestra el nombre del elemento, su explicación
// y, debajo, la demostración interactiva (los hijos que se ponen en el XML).
class TarjetaElemento @JvmOverloads constructor(
    context: Context,
    atributos: AttributeSet? = null,
    estiloPorDefecto: Int = com.google.android.material.R.attr.materialCardViewOutlinedStyle
) : MaterialCardView(context, atributos, estiloPorDefecto) {

    private val binding: ViewTarjetaElementoBinding
    private var listo = false

    init {
        // Esquinas redondeadas
        radius = resources.getDimension(R.dimen.radio_tarjeta)

        // Diseño interno de la tarjeta (título, descripción y zona de demostración)
        binding = ViewTarjetaElementoBinding.inflate(LayoutInflater.from(context), this, true)
        listo = true

        // Lee el título y la descripción escritos en el XML
        context.withStyledAttributes(atributos, R.styleable.TarjetaElemento) {
            binding.tvTitulo.text = getString(R.styleable.TarjetaElemento_titulo)
            binding.tvDescripcion.text = getString(R.styleable.TarjetaElemento_descripcion)
        }
    }

    // Los hijos declarados en el XML se colocan dentro de la zona de demostración
    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (listo) {
            binding.contenedorDemo.addView(child, index, params)
        } else {
            super.addView(child, index, params)
        }
    }
}
