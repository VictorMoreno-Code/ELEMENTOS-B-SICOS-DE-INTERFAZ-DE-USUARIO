import 'package:flutter/material.dart';

import '../estado.dart';
import '../widgets/componentes.dart';

// Sección 4: Listas y colecciones
class Seccion4 extends StatefulWidget {
  const Seccion4({super.key});

  @override
  State<Seccion4> createState() => _Seccion4State();
}

class _Seccion4State extends State<Seccion4> {
  String? _mosaico;
  String? _filaElegida;
  final List<int> _megusta = [0, 0, 0];

  static const _titulosPestanas = ['Resumen', 'Detalles', 'Ajustes'];
  static const _textosPestanas = [
    'Primera página. Desliza hacia la izquierda para ver la siguiente.',
    'Segunda página. Las pestañas y el deslizamiento están sincronizados.',
    'Tercera página. Cada página puede tener sus propios elementos.',
  ];

  // Filas de la lista con encabezados: 'E' = encabezado, 'F' = fila
  static const _agrupadas = [
    ['E', 'Entrada y acciones', ''],
    ['F', 'Campo de texto', 'Sección 1'],
    ['F', 'Botón relleno', 'Sección 2'],
    ['F', 'Botón flotante', 'Sección 2'],
    ['E', 'Selección', ''],
    ['F', 'Casilla de verificación', 'Sección 3'],
    ['F', 'Interruptor', 'Sección 3'],
    ['F', 'Chips de filtro', 'Sección 3'],
    ['E', 'Colecciones', ''],
    ['F', 'Lista vertical', 'Sección 4'],
    ['F', 'Cuadrícula', 'Sección 4'],
    ['E', 'Estructura', ''],
    ['F', 'Barra superior', 'Sección 6'],
    ['F', 'Barra de navegación inferior', 'Sección 6'],
  ];

  // Muestra el detalle de un elemento de la lista
  void _mostrarDetalle(ElementoLista elemento) {
    showDialog<void>(
      context: context,
      builder: (contexto) => AlertDialog(
        title: Text(elemento.titulo),
        content: Text('${elemento.detalle}\n\nOrigen: ${elemento.origen}'),
        actions: [
          TextButton(onPressed: () => Navigator.pop(contexto), child: const Text('Cerrar')),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final estado = EstadoScope.of(context);
    final tema = Theme.of(context);
    final colores = tema.colorScheme;
    final elementos = estado.elementos;

    return PantallaSeccion(
      titulo: 'Sección 4: Listas y colecciones',
      subtitulo: 'Elementos para mostrar muchos datos de forma ordenada.',
      hijos: [
        // Lista vertical (con detalle, deslizar para eliminar, actualizar y estado vacío)
        TarjetaElemento(
          titulo: 'Lista vertical',
          descripcion:
              'Lista con más de 15 elementos. Toca uno para ver su detalle, deslízalo a un lado para eliminarlo (puedes deshacer) y arrastra hacia abajo desde el inicio para actualizar. Lo que agregues en la Sección 1 aparece arriba.',
          hijos: [
            // Contador de elementos
            Text(
              '${elementos.length} elementos en la lista',
              style: tema.textTheme.labelLarge?.copyWith(color: colores.secondary),
            ),
            const SizedBox(height: 8),
            SizedBox(
              height: 360,
              child: elementos.isEmpty
                  // Estado vacío con mensaje e ilustración
                  ? _EstadoVacio(alRestaurar: estado.restaurar)
                  // Actualizar la lista arrastrando hacia abajo
                  : RefreshIndicator(
                      onRefresh: () async {
                        await Future<void>.delayed(const Duration(milliseconds: 1200));
                        estado.actualizar();
                        if (context.mounted) mostrarMensaje(context, 'Lista actualizada');
                      },
                      child: ListView.builder(
                        physics: const AlwaysScrollableScrollPhysics(),
                        itemCount: elementos.length,
                        itemBuilder: (contexto, indice) {
                          final elemento = elementos[indice];
                          // Deslizar un elemento para eliminarlo
                          return Dismissible(
                            key: ValueKey(elemento.id),
                            background: _FondoBorrado(alineacion: Alignment.centerLeft),
                            secondaryBackground: _FondoBorrado(alineacion: Alignment.centerRight),
                            onDismissed: (_) {
                              estado.eliminar(elemento.id);
                              mostrarMensaje(
                                context,
                                '«${elemento.titulo}» eliminado',
                                accion: 'Deshacer',
                                alAccion: () => estado.insertar(indice, elemento),
                              );
                            },
                            child: Padding(
                              padding: const EdgeInsets.only(bottom: 8),
                              child: Card(
                                elevation: 0,
                                margin: EdgeInsets.zero,
                                color: colores.surface,
                                clipBehavior: Clip.antiAlias,
                                shape: RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(14),
                                  side: BorderSide(color: colores.outlineVariant),
                                ),
                                child: ListTile(
                                  onTap: () => _mostrarDetalle(elemento),
                                  leading: CircleAvatar(
                                    backgroundColor: colores.primaryContainer,
                                    child: Text(
                                      elemento.titulo.substring(0, 1).toUpperCase(),
                                      style: TextStyle(
                                        fontWeight: FontWeight.bold,
                                        color: colores.onPrimaryContainer,
                                      ),
                                    ),
                                  ),
                                  title: Text(elemento.titulo),
                                  subtitle: Text('Origen: ${elemento.origen}'),
                                ),
                              ),
                            ),
                          );
                        },
                      ),
                    ),
            ),
            const SizedBox(height: 8),
            // Vaciar la lista para ver el estado vacío
            OutlinedButton.icon(
              icon: const Icon(Icons.delete),
              label: const Text('Vaciar lista (ver estado vacío)'),
              onPressed: estado.vaciar,
            ),
          ],
        ),

        // Cuadrícula de elementos
        TarjetaElemento(
          titulo: 'Cuadrícula',
          descripcion:
              'Organiza los elementos en filas y columnas. Es útil para galerías, catálogos o accesos rápidos. Toca un mosaico para seleccionarlo.',
          hijos: [
            GridView.builder(
              shrinkWrap: true,
              physics: const NeverScrollableScrollPhysics(),
              itemCount: 12,
              gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 3,
                mainAxisSpacing: 8,
                crossAxisSpacing: 8,
                mainAxisExtent: 88,
              ),
              itemBuilder: (contexto, indice) {
                final nombre = 'Mosaico ${indice + 1}';
                return Card(
                  elevation: 0,
                  margin: EdgeInsets.zero,
                  clipBehavior: Clip.antiAlias,
                  color: _mosaico == nombre ? colores.primaryContainer : colores.surfaceContainerHighest,
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
                  child: InkWell(
                    onTap: () => setState(() => _mosaico = nombre),
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Icon(Icons.star, color: colores.primary),
                        Text(nombre, style: tema.textTheme.labelMedium),
                      ],
                    ),
                  ),
                );
              },
            ),
            Respuesta(_mosaico == null ? 'Toca un mosaico' : 'Seleccionaste: $_mosaico'),
          ],
        ),

        // Lista con encabezados de sección
        TarjetaElemento(
          titulo: 'Lista con encabezados de sección',
          descripcion:
              'Una misma lista con dos tipos de elemento: encabezados que dividen los grupos y filas con la información. Desplázala para ver todos los grupos.',
          hijos: [
            SizedBox(
              height: 260,
              child: ListView.builder(
                itemCount: _agrupadas.length,
                itemBuilder: (contexto, indice) {
                  final fila = _agrupadas[indice];
                  if (fila[0] == 'E') {
                    // Encabezado de grupo
                    return Container(
                      width: double.infinity,
                      color: colores.primaryContainer,
                      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                      child: Text(
                        fila[1],
                        style: tema.textTheme.labelLarge?.copyWith(
                          fontWeight: FontWeight.bold,
                          color: colores.onPrimaryContainer,
                        ),
                      ),
                    );
                  }
                  // Fila con información
                  return ListTile(
                    title: Text(fila[1]),
                    subtitle: Text(fila[2]),
                    onTap: () => setState(() => _filaElegida = '${fila[1]} (${fila[2]})'),
                  );
                },
              ),
            ),
            Respuesta(_filaElegida == null ? 'Toca una fila' : 'Elegiste: $_filaElegida'),
          ],
        ),

        // Pestañas con contenido deslizable
        TarjetaElemento(
          titulo: 'Pestañas deslizables',
          descripcion:
              'Las pestañas dividen el contenido en varias vistas. Toca una pestaña o desliza el contenido hacia los lados para cambiar de página.',
          hijos: [
            DefaultTabController(
              length: _titulosPestanas.length,
              child: Column(
                children: [
                  TabBar(tabs: [for (final titulo in _titulosPestanas) Tab(text: titulo)]),
                  SizedBox(
                    height: 190,
                    child: TabBarView(
                      children: [
                        for (var i = 0; i < _titulosPestanas.length; i++)
                          Padding(
                            padding: const EdgeInsets.all(16),
                            child: Column(
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: [
                                Text(
                                  _titulosPestanas[i],
                                  style: tema.textTheme.titleMedium?.copyWith(fontWeight: FontWeight.bold),
                                ),
                                const SizedBox(height: 8),
                                Text(
                                  _textosPestanas[i],
                                  textAlign: TextAlign.center,
                                  style: tema.textTheme.bodyMedium?.copyWith(color: colores.onSurfaceVariant),
                                ),
                                const SizedBox(height: 12),
                                FilledButton.tonal(
                                  onPressed: () => setState(() => _megusta[i]++),
                                  child: Text('Me gusta (${_megusta[i]})'),
                                ),
                              ],
                            ),
                          ),
                      ],
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ],
    );
  }
}

// Fondo rojo con ícono de basura que aparece al deslizar un elemento
class _FondoBorrado extends StatelessWidget {
  final Alignment alineacion;

  const _FondoBorrado({required this.alineacion});

  @override
  Widget build(BuildContext context) {
    final colores = Theme.of(context).colorScheme;
    return Container(
      margin: const EdgeInsets.only(bottom: 8),
      padding: const EdgeInsets.symmetric(horizontal: 16),
      alignment: alineacion,
      decoration: BoxDecoration(
        color: colores.errorContainer,
        borderRadius: BorderRadius.circular(14),
      ),
      child: Icon(Icons.delete, color: colores.onErrorContainer),
    );
  }
}

// Estado vacío: ilustración dibujada con círculos e ícono, mensaje y botón
class _EstadoVacio extends StatelessWidget {
  final VoidCallback alRestaurar;

  const _EstadoVacio({required this.alRestaurar});

  @override
  Widget build(BuildContext context) {
    final tema = Theme.of(context);
    final colores = tema.colorScheme;
    return Center(
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          // Ilustración
          Container(
            width: 120,
            height: 120,
            decoration: BoxDecoration(shape: BoxShape.circle, color: colores.secondary.withValues(alpha: 0.2)),
            child: Center(
              child: Container(
                width: 80,
                height: 80,
                decoration: BoxDecoration(shape: BoxShape.circle, color: colores.primary.withValues(alpha: 0.3)),
                child: Icon(Icons.inbox, size: 48, color: colores.primary),
              ),
            ),
          ),
          const SizedBox(height: 8),
          Text('No hay elementos', style: tema.textTheme.titleMedium?.copyWith(fontWeight: FontWeight.bold)),
          const SizedBox(height: 4),
          Text(
            'Eliminaste todos los elementos de la lista.',
            style: tema.textTheme.bodyMedium?.copyWith(color: colores.onSurfaceVariant),
          ),
          const SizedBox(height: 12),
          FilledButton.icon(
            icon: const Icon(Icons.refresh),
            label: const Text('Restaurar lista'),
            onPressed: alRestaurar,
          ),
        ],
      ),
    );
  }
}
