import 'package:flutter/material.dart';

// Elemento de la lista de la Sección 4
class ElementoLista {
  final int id;
  final String titulo;
  final String detalle;
  final String origen;

  const ElementoLista(this.id, this.titulo, this.detalle, this.origen);
}

// Datos compartidos entre secciones (conexión entre secciones)
class Estado extends ChangeNotifier {
  int _siguienteId = 1;
  int _contadorNovedades = 0;

  // Lista de la Sección 4 (recibe lo que se captura en la Sección 1)
  late List<ElementoLista> elementos = _crearListaInicial();

  // Interruptor "Modo detallado" de la Sección 3 (cambia lo que se ve en la Sección 5)
  bool _modoDetallado = false;
  bool get modoDetallado => _modoDetallado;
  set modoDetallado(bool valor) {
    _modoDetallado = valor;
    notifyListeners();
  }

  // Valor del deslizador de la Sección 3 (se muestra en el progreso de la Sección 5)
  int _nivel = 40;
  int get nivel => _nivel;
  set nivel(int valor) {
    _nivel = valor;
    notifyListeners();
  }

  // Crea los 16 elementos iniciales de la lista
  List<ElementoLista> _crearListaInicial() {
    const datos = [
      ['Campo de texto', 'Permite escribir una línea de texto.'],
      ['Botón relleno', 'Acción principal de una pantalla.'],
      ['Botón con contorno', 'Acción secundaria con borde.'],
      ['Botón de acción flotante', 'Acción destacada que flota sobre el contenido.'],
      ['Casilla de verificación', 'Marca una opción como activada o desactivada.'],
      ['Botón de opción', 'Permite elegir solo una opción de un grupo.'],
      ['Interruptor', 'Activa o desactiva un ajuste al instante.'],
      ['Deslizador', 'Elige un valor dentro de un rango.'],
      ['Lista desplegable', 'Muestra opciones al tocar el campo.'],
      ['Chip', 'Etiqueta compacta que puede filtrar contenido.'],
      ['Tarjeta', 'Agrupa información relacionada.'],
      ['Diálogo', 'Pide una confirmación al usuario.'],
      ['Snackbar', 'Mensaje breve con una acción opcional.'],
      ['Barra de progreso', 'Indica el avance de una tarea.'],
      ['Pestañas', 'Organizan contenido en varias vistas.'],
      ['Menú lateral', 'Permite navegar entre las secciones.'],
    ];
    return datos
        .map((d) => ElementoLista(_siguienteId++, d[0], d[1], 'Catálogo inicial'))
        .toList();
  }

  // Agrega un elemento al inicio de la lista (lo usa la Sección 1)
  void agregar(String titulo, String origen) {
    final nuevo = ElementoLista(_siguienteId++, titulo, 'Elemento agregado por el usuario.', origen);
    elementos = [nuevo, ...elementos];
    notifyListeners();
  }

  // Elimina un elemento por su id (deslizar para borrar)
  void eliminar(int id) {
    elementos = elementos.where((e) => e.id != id).toList();
    notifyListeners();
  }

  // Vuelve a insertar un elemento en su posición (deshacer)
  void insertar(int posicion, ElementoLista elemento) {
    final lista = [...elementos];
    lista.insert(posicion.clamp(0, lista.length), elemento);
    elementos = lista;
    notifyListeners();
  }

  // Simula la actualización de la lista (pull-to-refresh)
  void actualizar() {
    _contadorNovedades++;
    final nuevo = ElementoLista(
      _siguienteId++,
      'Novedad $_contadorNovedades',
      'Elemento cargado al actualizar la lista.',
      'Actualización',
    );
    elementos = [nuevo, ...elementos];
    notifyListeners();
  }

  // Elimina todos los elementos (para mostrar el estado vacío)
  void vaciar() {
    elementos = [];
    notifyListeners();
  }

  // Restaura la lista original
  void restaurar() {
    elementos = _crearListaInicial();
    notifyListeners();
  }
}

// Permite que cualquier pantalla lea y modifique el estado compartido
class EstadoScope extends InheritedNotifier<Estado> {
  const EstadoScope({super.key, required Estado estado, required super.child})
      : super(notifier: estado);

  static Estado of(BuildContext context) {
    return context.dependOnInheritedWidgetOfExactType<EstadoScope>()!.notifier!;
  }
}
