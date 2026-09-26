import 'package:flutter/material.dart';

import '../widgets/componentes.dart';

// Caja de color usada en las demostraciones de contenedores
class _Caja extends StatelessWidget {
  final String texto;
  final Color fondo;
  final Color contenido;
  final double? ancho;
  final double? alto;
  final VoidCallback? alTocar;

  const _Caja({
    required this.texto,
    required this.fondo,
    required this.contenido,
    this.ancho,
    this.alto,
    this.alTocar,
  });

  @override
  Widget build(BuildContext context) {
    return Material(
      color: fondo,
      borderRadius: BorderRadius.circular(12),
      child: InkWell(
        borderRadius: BorderRadius.circular(12),
        onTap: alTocar,
        child: SizedBox(
          width: ancho,
          height: alto,
          child: Center(
            child: Text(texto, style: TextStyle(color: contenido, fontWeight: FontWeight.bold)),
          ),
        ),
      ),
    );
  }
}

// Sección 6: Contenedores y estructura
class Seccion6 extends StatefulWidget {
  const Seccion6({super.key});

  @override
  State<Seccion6> createState() => _Seccion6State();
}

class _Seccion6State extends State<Seccion6> {
  bool _enFila = true;
  String _respuestaCajas = 'Orientación: fila';
  final List<int> _orden = [1, 2, 3];
  final ScrollController _desplazamiento = ScrollController();
  String _respuestaBarra = 'Toca una acción de la barra';
  int _pestana = 0;
  int _avisosPerfil = 3;
  double _peso = 2;
  double _porcentaje = 50;

  @override
  void dispose() {
    _desplazamiento.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final tema = Theme.of(context);
    final c = tema.colorScheme;
    const nombres = ['Inicio', 'Favoritos', 'Perfil'];

    // Colores de las tres cajas de demostración
    Color fondoDe(int n) => n == 1 ? c.primary : (n == 2 ? c.secondary : c.tertiary);
    Color textoDe(int n) => n == 1 ? c.onPrimary : (n == 2 ? c.onSecondary : c.onTertiary);

    // Tres cajas tocables (A, B y C)
    final cajasABC = [
      for (final (indice, letra) in ['A', 'B', 'C'].indexed)
        _Caja(
          texto: letra,
          fondo: fondoDe(indice + 1),
          contenido: textoDe(indice + 1),
          ancho: 64,
          alto: 64,
          alTocar: () => setState(() => _respuestaCajas = 'Tocaste la caja $letra'),
        ),
    ];

    return PantallaSeccion(
      titulo: 'Sección 6: Contenedores y estructura',
      subtitulo: 'Elementos que organizan a los demás dentro de la pantalla.',
      hijos: [
        // Distribución en fila y en columna
        TarjetaElemento(
          titulo: 'Distribución en fila y en columna',
          descripcion:
              'Un mismo contenedor puede acomodar sus elementos en una fila (horizontal) o en una columna (vertical). Cambia la orientación con los botones y toca una caja.',
          hijos: [
            // Elegir la orientación
            SegmentedButton<bool>(
              segments: const [
                ButtonSegment<bool>(value: true, label: Text('Fila')),
                ButtonSegment<bool>(value: false, label: Text('Columna')),
              ],
              selected: {_enFila},
              onSelectionChanged: (seleccion) => setState(() {
                _enFila = seleccion.first;
                _respuestaCajas = _enFila ? 'Orientación: fila' : 'Orientación: columna';
              }),
            ),
            const SizedBox(height: 12),
            // Contenedor con tres cajas (Row o Column)
            Flex(
              direction: _enFila ? Axis.horizontal : Axis.vertical,
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                for (final caja in cajasABC) Padding(padding: const EdgeInsets.all(4), child: caja),
              ],
            ),
            Respuesta(_respuestaCajas),
          ],
        ),

        // Distribución superpuesta
        TarjetaElemento(
          titulo: 'Distribución superpuesta',
          descripcion:
              'Los elementos se colocan unos encima de otros. Sirve para poner un texto sobre una imagen o un distintivo sobre un ícono. Toca una caja para traerla al frente.',
          hijos: [
            SizedBox(
              height: 150,
              width: double.infinity,
              // Stack: la última caja de la lista queda al frente
              child: Stack(
                children: [
                  for (final numero in _orden)
                    Positioned(
                      key: ValueKey(numero),
                      left: (numero - 1) * 40.0,
                      top: (numero - 1) * 25.0,
                      child: _Caja(
                        texto: '$numero',
                        fondo: fondoDe(numero),
                        contenido: textoDe(numero),
                        ancho: 100,
                        alto: 100,
                        alTocar: () => setState(() {
                          _orden.remove(numero);
                          _orden.add(numero);
                        }),
                      ),
                    ),
                ],
              ),
            ),
            Respuesta('Al frente: caja ${_orden.last}'),
          ],
        ),

        // Contenedor con desplazamiento vertical
        TarjetaElemento(
          titulo: 'Contenedor con desplazamiento vertical',
          descripcion:
              'Cuando el contenido es más grande que el espacio disponible, un contenedor con desplazamiento permite moverse hacia arriba y abajo. Desliza el recuadro o usa los botones.',
          hijos: [
            Container(
              height: 140,
              width: double.infinity,
              color: c.surfaceContainerHighest.withValues(alpha: 0.4),
              child: SingleChildScrollView(
                controller: _desplazamiento,
                padding: const EdgeInsets.all(12),
                child: SizedBox(
                  width: double.infinity,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [for (var i = 1; i <= 20; i++) Text('Renglón $i de 20')],
                  ),
                ),
              ),
            ),
            const SizedBox(height: 8),
            Row(
              children: [
                OutlinedButton(
                  onPressed: () => _desplazamiento.animateTo(
                    0,
                    duration: const Duration(milliseconds: 400),
                    curve: Curves.easeOut,
                  ),
                  child: const Text('Ir al inicio'),
                ),
                const SizedBox(width: 8),
                OutlinedButton(
                  onPressed: () => _desplazamiento.animateTo(
                    _desplazamiento.position.maxScrollExtent,
                    duration: const Duration(milliseconds: 400),
                    curve: Curves.easeOut,
                  ),
                  child: const Text('Ir al final'),
                ),
              ],
            ),
          ],
        ),

        // Barra superior con título y acciones
        TarjetaElemento(
          titulo: 'Barra superior',
          descripcion:
              'Barra en la parte superior con el título de la pantalla, un botón de navegación a la izquierda y acciones a la derecha. Los tres puntos abren las acciones extra.',
          hijos: [
            ClipRRect(
              borderRadius: BorderRadius.circular(12),
              child: AppBar(
                primary: false,
                backgroundColor: c.primaryContainer,
                foregroundColor: c.onPrimaryContainer,
                title: const Text('Mi barra superior'),
                leading: IconButton(
                  icon: const Icon(Icons.menu),
                  tooltip: 'Menú',
                  onPressed: () => setState(() => _respuestaBarra = 'Tocaste el botón de navegación (menú)'),
                ),
                actions: [
                  IconButton(
                    icon: const Icon(Icons.search),
                    tooltip: 'Buscar',
                    onPressed: () => setState(() => _respuestaBarra = 'Acción: Buscar'),
                  ),
                  IconButton(
                    icon: const Icon(Icons.favorite),
                    tooltip: 'Favorito',
                    onPressed: () => setState(() => _respuestaBarra = 'Acción: Favorito'),
                  ),
                  PopupMenuButton<String>(
                    onSelected: (opcion) => setState(() => _respuestaBarra = 'Acción del menú: $opcion'),
                    itemBuilder: (contexto) => const [
                      PopupMenuItem(value: 'Compartir', child: Text('Compartir')),
                      PopupMenuItem(value: 'Ayuda', child: Text('Ayuda')),
                    ],
                  ),
                ],
              ),
            ),
            Respuesta(_respuestaBarra),
          ],
        ),

        // Barra de navegación inferior
        TarjetaElemento(
          titulo: 'Barra de navegación inferior',
          descripcion:
              'Permite cambiar entre las pantallas principales con un toque. Esta app usa un menú lateral para sus secciones; aquí ves una barra inferior funcional con un distintivo de avisos en «Perfil».',
          hijos: [
            // Pantalla que cambia con la barra
            Container(
              height: 80,
              width: double.infinity,
              alignment: Alignment.center,
              color: c.surfaceContainerHighest.withValues(alpha: 0.4),
              child: Text('Estás en: ${nombres[_pestana]}', style: tema.textTheme.titleMedium),
            ),
            NavigationBar(
              selectedIndex: _pestana,
              onDestinationSelected: (indice) => setState(() {
                _pestana = indice;
                // Al entrar a Perfil se quitan los avisos
                if (indice == 2) _avisosPerfil = 0;
              }),
              destinations: [
                const NavigationDestination(icon: Icon(Icons.home), label: 'Inicio'),
                const NavigationDestination(icon: Icon(Icons.favorite), label: 'Favoritos'),
                NavigationDestination(
                  icon: Badge(
                    isLabelVisible: _avisosPerfil > 0,
                    label: Text('$_avisosPerfil'),
                    child: const Icon(Icons.person),
                  ),
                  label: 'Perfil',
                ),
              ],
            ),
          ],
        ),

        // Distribución con pesos proporcionales
        TarjetaElemento(
          titulo: 'Distribución con pesos proporcionales',
          descripcion:
              'Con pesos (Expanded con flex) cada elemento recibe una parte proporcional del espacio. Mueve el deslizador para cambiar el peso de la caja central.',
          hijos: [
            SizedBox(
              height: 64,
              child: Row(
                children: [
                  Expanded(flex: 1, child: _Caja(texto: '1', fondo: c.primary, contenido: c.onPrimary, alto: 64)),
                  const SizedBox(width: 4),
                  Expanded(
                    flex: _peso.toInt(),
                    child: _Caja(texto: '${_peso.toInt()}', fondo: c.secondary, contenido: c.onSecondary, alto: 64),
                  ),
                  const SizedBox(width: 4),
                  Expanded(flex: 1, child: _Caja(texto: '1', fondo: c.tertiary, contenido: c.onTertiary, alto: 64)),
                ],
              ),
            ),
            Slider(
              value: _peso,
              min: 1,
              max: 6,
              divisions: 5,
              label: '${_peso.toInt()}',
              onChanged: (valor) => setState(() => _peso = valor),
            ),
            Respuesta('Proporción 1 : ${_peso.toInt()} : 1'),
          ],
        ),

        // Distribución con restricciones (Flutter no tiene ConstraintLayout nativo)
        TarjetaElemento(
          titulo: 'Distribución con restricciones',
          descripcion:
              'Flutter no trae ConstraintLayout: aquí las restricciones se resuelven con LayoutBuilder y Positioned. Dos cajas se anclan a una guía vertical; mueve el deslizador para desplazar la guía.',
          hijos: [
            SizedBox(
              height: 80,
              child: LayoutBuilder(
                builder: (contexto, restricciones) {
                  // Posición de la guía según el ancho disponible
                  final guia = restricciones.maxWidth * _porcentaje / 100;
                  return Stack(
                    children: [
                      // Caja anclada entre el borde izquierdo y la guía
                      Positioned(
                        left: 0,
                        top: 8,
                        width: guia - 4,
                        height: 64,
                        child: _Caja(texto: 'Izquierda', fondo: c.primary, contenido: c.onPrimary),
                      ),
                      // Caja anclada entre la guía y el borde derecho
                      Positioned(
                        left: guia + 4,
                        right: 0,
                        top: 8,
                        height: 64,
                        child: _Caja(texto: 'Derecha', fondo: c.secondary, contenido: c.onSecondary),
                      ),
                    ],
                  );
                },
              ),
            ),
            Slider(
              value: _porcentaje,
              min: 20,
              max: 80,
              divisions: 12,
              label: '${_porcentaje.toInt()} %',
              onChanged: (valor) => setState(() => _porcentaje = valor),
            ),
            Respuesta('Guía al ${_porcentaje.toInt()} % del ancho'),
          ],
        ),
      ],
    );
  }
}
