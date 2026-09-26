package com.outlook.victoreduardo.xml_ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// Elemento de la lista de la Sección 4
data class ElementoLista(
    val id: Long,
    val titulo: String,
    val detalle: String,
    val origen: String
)

// Datos compartidos entre secciones (conexión entre secciones)
class CatalogoViewModel : ViewModel() {

    private var siguienteId = 1L
    private var contadorNovedades = 0

    // Lista de la Sección 4 (recibe lo que se captura en la Sección 1)
    private val _elementos = MutableLiveData<List<ElementoLista>>(crearListaInicial())
    val elementos: LiveData<List<ElementoLista>> = _elementos

    // Interruptor "Modo detallado" de la Sección 3 (cambia lo que se ve en la Sección 5)
    val modoDetallado = MutableLiveData(false)

    // Valor del deslizador de la Sección 3 (se muestra en el progreso de la Sección 5)
    val nivel = MutableLiveData(40)

    // Crea los 16 elementos iniciales de la lista
    private fun crearListaInicial(): List<ElementoLista> {
        val datos = listOf(
            "Campo de texto" to "Permite escribir una línea de texto.",
            "Botón relleno" to "Acción principal de una pantalla.",
            "Botón con contorno" to "Acción secundaria con borde.",
            "Botón de acción flotante" to "Acción destacada que flota sobre el contenido.",
            "Casilla de verificación" to "Marca una opción como activada o desactivada.",
            "Botón de opción" to "Permite elegir solo una opción de un grupo.",
            "Interruptor" to "Activa o desactiva un ajuste al instante.",
            "Deslizador" to "Elige un valor dentro de un rango.",
            "Lista desplegable" to "Muestra opciones al tocar el campo.",
            "Chip" to "Etiqueta compacta que puede filtrar contenido.",
            "Tarjeta" to "Agrupa información relacionada.",
            "Diálogo" to "Pide una confirmación al usuario.",
            "Snackbar" to "Mensaje breve con una acción opcional.",
            "Barra de progreso" to "Indica el avance de una tarea.",
            "Pestañas" to "Organizan contenido en varias vistas.",
            "Menú lateral" to "Permite navegar entre las secciones."
        )
        return datos.map { (titulo, detalle) ->
            ElementoLista(siguienteId++, titulo, detalle, "Catálogo inicial")
        }
    }

    // Agrega un elemento al inicio de la lista (lo usa la Sección 1)
    fun agregar(titulo: String, origen: String) {
        val nuevo = ElementoLista(siguienteId++, titulo, "Elemento agregado por el usuario.", origen)
        _elementos.value = listOf(nuevo) + (_elementos.value ?: emptyList())
    }

    // Elimina un elemento por su id (deslizar para borrar)
    fun eliminar(id: Long) {
        _elementos.value = (_elementos.value ?: emptyList()).filter { it.id != id }
    }

    // Vuelve a insertar un elemento en su posición (deshacer)
    fun insertar(posicion: Int, elemento: ElementoLista) {
        val lista = (_elementos.value ?: emptyList()).toMutableList()
        lista.add(posicion.coerceIn(0, lista.size), elemento)
        _elementos.value = lista
    }

    // Simula la actualización de la lista (pull-to-refresh)
    fun actualizar() {
        contadorNovedades++
        val nuevo = ElementoLista(
            siguienteId++,
            "Novedad $contadorNovedades",
            "Elemento cargado al actualizar la lista.",
            "Actualización"
        )
        _elementos.value = listOf(nuevo) + (_elementos.value ?: emptyList())
    }

    // Elimina todos los elementos (para mostrar el estado vacío)
    fun vaciar() {
        _elementos.value = emptyList()
    }

    // Restaura la lista original (estado vacío)
    fun restaurar() {
        _elementos.value = crearListaInicial()
    }
}
