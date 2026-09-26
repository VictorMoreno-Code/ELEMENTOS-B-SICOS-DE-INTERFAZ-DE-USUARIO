import 'dart:async';

import 'package:flutter/material.dart';

import '../widgets/componentes.dart';

// Sección 2: Botones y acciones
class Seccion2 extends StatefulWidget {
  const Seccion2({super.key});

  @override
  State<Seccion2> createState() => _Seccion2State();
}

class _Seccion2State extends State<Seccion2> {
  // Botón relleno, con contorno y de solo texto
  int _relleno = 0;
  int _contorno = 0;
  int _texto = 0;
  String _respuestaBasicos = 'Pulsa un botón para ver la respuesta';

  // Botón con ícono
  bool _favorito = false;
  String _respuestaIconos = 'Toca un botón con ícono';

  // Botón de acción flotante
  int _toquesFab = 0;
  bool _fabExpandido = true;
  String _respuestaFab = 'Toca uno de los botones flotantes';

  // Alternancia y selector segmentado
  int _vista = 0;
  bool _recordatorio = false;

  // Botón deshabilitado y en carga
  bool _habilitado = false;
  bool _cargando = false;
  String _respuestaEstados = 'Prueba habilitar el botón o guardar';
  Timer? _temporizador;

  @override
  void dispose() {
    _temporizador?.cancel();
    super.dispose();
  }

  // Simula guardar durante 2 segundos
  void _guardar() {
    setState(() {
      _cargando = true;
      _respuestaEstados = 'Guardando cambios, espera un momento';
    });
    _temporizador = Timer(const Duration(seconds: 2), () {
      if (!mounted) return;
      setState(() {
        _cargando = false;
        _respuestaEstados = 'Cambios guardados correctamente';
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    const vistas = ['Día', 'Semana', 'Mes'];

    return PantallaSeccion(
      titulo: 'Sección 2: Botones y acciones',
      subtitulo: 'Elementos que ejecutan una acción al tocarlos. Todos responden con un mensaje visible.',
      hijos: [
        // Botón relleno, con contorno y de solo texto
        TarjetaElemento(
          titulo: 'Botón relleno, con contorno y de solo texto',
          descripcion:
              'Tres niveles de énfasis: el relleno es la acción principal, el de contorno una acción secundaria y el de solo texto la acción de menor importancia.',
          hijos: [
            Wrap(
              spacing: 8,
              runSpacing: 8,
              children: [
                // Botón relleno
                FilledButton(
                  onPressed: () => setState(() {
                    _relleno++;
                    _respuestaBasicos = 'Botón relleno pulsado $_relleno veces';
                  }),
                  child: const Text('Relleno'),
                ),
                // Botón con contorno
                OutlinedButton(
                  onPressed: () => setState(() {
                    _contorno++;
                    _respuestaBasicos = 'Botón con contorno pulsado $_contorno veces';
                  }),
                  child: const Text('Contorno'),
                ),
                // Botón de solo texto
                TextButton(
                  onPressed: () => setState(() {
                    _texto++;
                    _respuestaBasicos = 'Botón de solo texto pulsado $_texto veces';
                  }),
                  child: const Text('Texto'),
                ),
              ],
            ),
            Respuesta(_respuestaBasicos),
          ],
        ),

        // Botón con ícono
        TarjetaElemento(
          titulo: 'Botón con ícono',
          descripcion:
              'Los botones pueden llevar solo un ícono (compactos) o un ícono junto al texto (más claros). El corazón cambia de estado cada vez que lo tocas.',
          hijos: [
            Wrap(
              spacing: 8,
              runSpacing: 8,
              crossAxisAlignment: WrapCrossAlignment.center,
              children: [
                // Solo ícono (relleno)
                IconButton.filled(
                  tooltip: 'Favorito',
                  icon: Icon(_favorito ? Icons.favorite : Icons.favorite_border),
                  onPressed: () => setState(() {
                    _favorito = !_favorito;
                    _respuestaIconos =
                        _favorito ? 'Ícono de corazón: marcado como favorito' : 'Ícono de corazón: favorito quitado';
                  }),
                ),
                // Solo ícono (contorno)
                IconButton.outlined(
                  tooltip: 'Editar',
                  icon: const Icon(Icons.edit),
                  onPressed: () => setState(() => _respuestaIconos = 'Ícono de lápiz: modo de edición'),
                ),
                // Ícono más texto
                FilledButton.icon(
                  icon: const Icon(Icons.share),
                  label: const Text('Compartir'),
                  onPressed: () {
                    setState(() => _respuestaIconos = 'Botón con ícono y texto: compartir');
                    mostrarMensaje(context, 'Se compartió el contenido de ejemplo');
                  },
                ),
              ],
            ),
            Respuesta(_respuestaIconos),
          ],
        ),

        // Botón de acción flotante, normal y extendido
        TarjetaElemento(
          titulo: 'Botón de acción flotante (normal y extendido)',
          descripcion:
              'El botón flotante (FAB) destaca la acción principal de una pantalla. La versión extendida incluye texto; al tocarla se contrae y se vuelve a expandir.',
          hijos: [
            Padding(
              padding: const EdgeInsets.all(8),
              child: Wrap(
                spacing: 24,
                runSpacing: 12,
                crossAxisAlignment: WrapCrossAlignment.center,
                children: [
                  // FAB normal
                  FloatingActionButton(
                    heroTag: 'fab_normal',
                    tooltip: 'Agregar',
                    onPressed: () => setState(() {
                      _toquesFab++;
                      _respuestaFab = 'FAB normal: $_toquesFab toques';
                    }),
                    child: const Icon(Icons.add),
                  ),
                  // FAB extendido (se contrae a un botón solo con ícono)
                  _fabExpandido
                      ? FloatingActionButton.extended(
                          heroTag: 'fab_extendido',
                          icon: const Icon(Icons.add),
                          label: const Text('Nuevo'),
                          onPressed: () => setState(() {
                            _fabExpandido = false;
                            _respuestaFab = 'FAB extendido: contraído';
                          }),
                        )
                      : FloatingActionButton(
                          heroTag: 'fab_contraido',
                          onPressed: () => setState(() {
                            _fabExpandido = true;
                            _respuestaFab = 'FAB extendido: expandido';
                          }),
                          child: const Icon(Icons.add),
                        ),
                ],
              ),
            ),
            Respuesta(_respuestaFab),
          ],
        ),

        // Botón de alternancia (toggle) y selector segmentado
        TarjetaElemento(
          titulo: 'Botón de alternancia y selector segmentado',
          descripcion:
              'El selector segmentado permite elegir una sola opción entre varias visibles. El botón de alternancia cambia entre dos estados (activado y desactivado) cada vez que se toca.',
          hijos: [
            // Selector segmentado
            SegmentedButton<int>(
              segments: [
                for (var i = 0; i < vistas.length; i++) ButtonSegment<int>(value: i, label: Text(vistas[i])),
              ],
              selected: {_vista},
              onSelectionChanged: (seleccion) => setState(() => _vista = seleccion.first),
            ),
            const SizedBox(height: 12),
            // Botón de alternancia
            _recordatorio
                ? FilledButton.tonalIcon(
                    icon: const Icon(Icons.notifications),
                    label: const Text('Recordatorio'),
                    onPressed: () => setState(() => _recordatorio = false),
                  )
                : OutlinedButton.icon(
                    icon: const Icon(Icons.notifications),
                    label: const Text('Recordatorio'),
                    onPressed: () => setState(() => _recordatorio = true),
                  ),
            Respuesta('Vista elegida: ${vistas[_vista]} · Recordatorio: ${_recordatorio ? 'activado' : 'desactivado'}'),
          ],
        ),

        // Botón deshabilitado y botón en estado de carga
        TarjetaElemento(
          titulo: 'Botón deshabilitado y botón en carga',
          descripcion:
              'Un botón deshabilitado no responde al tocarlo. Un botón en carga bloquea la acción mientras se realiza una tarea y muestra un indicador de progreso.',
          hijos: [
            // Botón deshabilitado
            Row(
              children: [
                FilledButton(
                  // Con onPressed nulo el botón queda deshabilitado
                  onPressed: _habilitado
                      ? () => setState(() => _respuestaEstados = 'Ahora sí: el botón habilitado respondió')
                      : null,
                  child: Text(_habilitado ? 'Habilitado' : 'Deshabilitado'),
                ),
                TextButton(
                  onPressed: () => setState(() {
                    _habilitado = !_habilitado;
                    _respuestaEstados =
                        _habilitado ? 'Botón habilitado: ya puedes tocarlo' : 'Botón deshabilitado: no responde';
                  }),
                  child: Text(_habilitado ? 'Deshabilitar' : 'Habilitar'),
                ),
              ],
            ),
            const SizedBox(height: 12),
            // Botón en estado de carga
            Row(
              children: [
                FilledButton(
                  onPressed: _cargando ? null : _guardar,
                  child: Text(_cargando ? 'Guardando…' : 'Guardar cambios'),
                ),
                if (_cargando) ...[
                  const SizedBox(width: 16),
                  const SizedBox(width: 28, height: 28, child: CircularProgressIndicator(strokeWidth: 3)),
                ],
              ],
            ),
            Respuesta(_respuestaEstados),
          ],
        ),
      ],
    );
  }
}
