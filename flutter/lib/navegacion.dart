import 'package:flutter/material.dart';

// Destinos de la app: inicio y las seis secciones
enum Destino {
  inicio('Inicio', Icons.home),
  seccion1('1. Entrada de texto', Icons.edit),
  seccion2('2. Botones y acciones', Icons.star),
  seccion3('3. Elementos de selección', Icons.check),
  seccion4('4. Listas y colecciones', Icons.list),
  seccion5('5. Información y retroalimentación', Icons.info),
  seccion6('6. Contenedores y estructura', Icons.dashboard);

  final String titulo;
  final IconData icono;
  const Destino(this.titulo, this.icono);
}

// Permite navegar a otra sección desde cualquier pantalla
class Navegador extends InheritedWidget {
  final void Function(Destino) irA;

  const Navegador({super.key, required this.irA, required super.child});

  static void Function(Destino) of(BuildContext context) {
    return context.getInheritedWidgetOfExactType<Navegador>()!.irA;
  }

  @override
  bool updateShouldNotify(Navegador oldWidget) => false;
}
