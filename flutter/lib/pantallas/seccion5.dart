import 'dart:async';

import 'package:flutter/material.dart';

import '../estado.dart';
import '../navegacion.dart';
import '../widgets/componentes.dart';

// Sección 5: Información y retroalimentación
class Seccion5 extends StatefulWidget {
  const Seccion5({super.key});

  @override
  State<Seccion5> createState() => _Seccion5State();
}

class _Seccion5State extends State<Seccion5> {
  // Textos con estilos
  bool _negrita = false;
  bool _cursiva = false;
  bool _subrayado = false;
  double _tamano = 22;

  // Imágenes
  static const _modos = [
    ('Recortar', BoxFit.cover, 'recortar para llenar (cover)'),
    ('Ajustar', BoxFit.contain, 'ajustar completa (contain)'),
    ('Estirar', BoxFit.fill, 'estirar al espacio (fill)'),
  ];
  static const _idsImagen = [10, 1015, 1018, 1039];
  int _modo = 0;
  int _indiceImagen = 0;

  // Progreso simulado
  int _progresoSimulado = 0;
  Timer? _temporizador;

  // Mensajes y diálogos
  String _respuestaMensajes = 'Toca un botón para ver el mensaje';
  String _respuestaDialogo = 'Sin respuesta todavía';
  String _respuestaHoja = 'Sin opción elegida';

  // Tarjeta, separador y badge
  bool _tarjetaMarcada = false;
  bool _mostrarSeparador = true;
  int _avisos = 3;

  @override
  void dispose() {
    _temporizador?.cancel();
    super.dispose();
  }

  // Avanza el progreso circular de 5 en 5 hasta llegar a 100
  void _simularCarga() {
    _temporizador?.cancel();
    setState(() => _progresoSimulado = 0);
    _temporizador = Timer.periodic(const Duration(milliseconds: 150), (t) {
      if (!mounted) {
        t.cancel();
        return;
      }
      setState(() => _progresoSimulado += 5);
      if (_progresoSimulado >= 100) t.cancel();
    });
  }

  // Muestra el diálogo de confirmación
  Future<void> _abrirDialogo() async {
    final confirmado = await showDialog<bool>(
      context: context,
      builder: (contexto) => AlertDialog(
        title: const Text('¿Eliminar elemento?'),
        content: const Text('Esta acción no se puede deshacer. ¿Quieres continuar?'),
        actions: [
          TextButton(onPressed: () => Navigator.pop(contexto, false), child: const Text('Cancelar')),
          TextButton(onPressed: () => Navigator.pop(contexto, true), child: const Text('Eliminar')),
        ],
      ),
    );
    if (!mounted) return;
    setState(() {
      _respuestaDialogo = confirmado == true
          ? 'Respuesta: confirmaste la eliminación'
          : 'Respuesta: cancelaste la acción';
    });
  }

  // Muestra la hoja inferior (bottom sheet)
  Future<void> _abrirHoja() async {
    final opcion = await showModalBottomSheet<String>(
      context: context,
      showDragHandle: true,
      builder: (contexto) => SafeArea(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 8),
              child: Align(
                alignment: Alignment.centerLeft,
                child: Text(
                  'Opciones del elemento',
                  style: Theme.of(contexto).textTheme.titleMedium?.copyWith(
                        fontWeight: FontWeight.bold,
                        color: Theme.of(contexto).colorScheme.primary,
                      ),
                ),
              ),
            ),
            // Opción: compartir
            ListTile(
              leading: const Icon(Icons.share),
              title: const Text('Compartir'),
              onTap: () => Navigator.pop(contexto, 'Compartir'),
            ),
            // Opción: editar
            ListTile(
              leading: const Icon(Icons.edit),
              title: const Text('Editar'),
              onTap: () => Navigator.pop(contexto, 'Editar'),
            ),
            // Opción: eliminar
            ListTile(
              leading: const Icon(Icons.delete),
              title: const Text('Eliminar'),
              onTap: () => Navigator.pop(contexto, 'Eliminar'),
            ),
          ],
        ),
      ),
    );
    if (opcion != null && mounted) setState(() => _respuestaHoja = 'Elegiste: $opcion');
  }

  @override
  Widget build(BuildContext context) {
    final estado = EstadoScope.of(context);
    final irA = Navegador.of(context);
    final tema = Theme.of(context);
    final colores = tema.colorScheme;
    final modo = _modos[_modo];
    final urlImagen = 'https://picsum.photos/id/${_idsImagen[_indiceImagen]}/800/450';

    return PantallaSeccion(
      titulo: 'Sección 5: Información y retroalimentación',
      subtitulo: 'Elementos que muestran información o avisan al usuario de lo que pasa.',
      hijos: [
        // Conexión con la Sección 3
        TarjetaElemento(
          titulo: 'Conexión con la Sección 3',
          descripcion:
              'Esta sección cambia según lo que elijas en la Sección 3: el interruptor «Modo detallado» muestra información adicional y el deslizador controla la barra de progreso lineal.',
          hijos: [
            Respuesta(
              estado.modoDetallado
                  ? 'Modo detallado: ACTIVADO (se muestra información adicional)'
                  : 'Modo detallado: desactivado (actívalo en la Sección 3)',
            ),
            const SizedBox(height: 8),
            FilledButton.tonal(
              onPressed: () => irA(Destino.seccion3),
              child: const Text('Cambiar en la Sección 3'),
            ),
          ],
        ),

        // Textos con distintos estilos, tamaños y énfasis
        TarjetaElemento(
          titulo: 'Textos con distintos estilos',
          descripcion:
              'El texto puede cambiar de tamaño, grosor, inclinación y color para dar jerarquía a la información. Prueba los botones para modificar el texto de muestra.',
          hijos: [
            // Texto de muestra que cambia con los botones
            Text(
              'Texto de muestra',
              style: TextStyle(
                fontSize: _tamano,
                fontWeight: _negrita ? FontWeight.bold : FontWeight.normal,
                fontStyle: _cursiva ? FontStyle.italic : FontStyle.normal,
                decoration: _subrayado ? TextDecoration.underline : TextDecoration.none,
              ),
            ),
            const SizedBox(height: 8),
            // Énfasis: negrita, cursiva y subrayado (selección múltiple)
            Wrap(
              spacing: 8,
              children: [
                FilterChip(
                  label: const Text('Negrita'),
                  selected: _negrita,
                  onSelected: (v) => setState(() => _negrita = v),
                ),
                FilterChip(
                  label: const Text('Cursiva'),
                  selected: _cursiva,
                  onSelected: (v) => setState(() => _cursiva = v),
                ),
                FilterChip(
                  label: const Text('Subrayado'),
                  selected: _subrayado,
                  onSelected: (v) => setState(() => _subrayado = v),
                ),
              ],
            ),
            const SizedBox(height: 8),
            // Tamaño del texto
            Row(
              children: [
                OutlinedButton(
                  onPressed: () => setState(() => _tamano = (_tamano - 2).clamp(12, 40).toDouble()),
                  child: const Text('A-'),
                ),
                const SizedBox(width: 8),
                OutlinedButton(
                  onPressed: () => setState(() => _tamano = (_tamano + 2).clamp(12, 40).toDouble()),
                  child: const Text('A+'),
                ),
                const SizedBox(width: 12),
                Text(
                  'Tamaño: ${_tamano.toInt()} sp',
                  style: tema.textTheme.labelLarge?.copyWith(color: colores.secondary),
                ),
              ],
            ),
            const Padding(padding: EdgeInsets.symmetric(vertical: 12), child: Divider(height: 1)),
            // Galería de estilos
            Text(
              'Titular grande y en negritas',
              style: tema.textTheme.headlineSmall?.copyWith(fontWeight: FontWeight.bold, color: colores.primary),
            ),
            const SizedBox(height: 4),
            Text('Subtítulo de tamaño mediano', style: tema.textTheme.titleMedium),
            const SizedBox(height: 4),
            Text(
              'Cuerpo de texto normal, ideal para párrafos y explicaciones largas.',
              style: tema.textTheme.bodyMedium,
            ),
            const SizedBox(height: 4),
            Text(
              'Nota pequeña en cursiva',
              style: tema.textTheme.bodySmall?.copyWith(
                fontStyle: FontStyle.italic,
                color: colores.onSurfaceVariant,
              ),
            ),
            const SizedBox(height: 4),
            // Texto con varios estilos mezclados (negrita, color y tachado)
            Text.rich(
              TextSpan(
                style: tema.textTheme.bodyMedium,
                children: [
                  const TextSpan(text: 'Un mismo texto con '),
                  const TextSpan(text: 'negrita', style: TextStyle(fontWeight: FontWeight.bold)),
                  const TextSpan(text: ', '),
                  TextSpan(text: 'color', style: TextStyle(color: colores.secondary)),
                  const TextSpan(text: ' y '),
                  const TextSpan(text: 'tachado', style: TextStyle(decoration: TextDecoration.lineThrough)),
                  const TextSpan(text: '.'),
                ],
              ),
            ),
          ],
        ),

        // Imagen local e imagen desde una URL
        TarjetaElemento(
          titulo: 'Imagen local e imagen desde URL',
          descripcion:
              'Una imagen puede venir de los recursos de la app (local) o descargarse desde internet (URL). Cambia el modo de escalado para ver cómo se ajusta al espacio disponible.',
          hijos: [
            // Modo de escalado (se aplica a las dos imágenes)
            Wrap(
              spacing: 8,
              children: [
                for (var i = 0; i < _modos.length; i++)
                  ChoiceChip(
                    label: Text(_modos[i].$1),
                    selected: _modo == i,
                    onSelected: (_) => setState(() => _modo = i),
                  ),
              ],
            ),
            Respuesta('Modo: ${modo.$3}'),
            // Imagen local
            const SizedBox(height: 12),
            Text('Imagen local (recurso de la app)', style: tema.textTheme.labelLarge),
            const SizedBox(height: 8),
            Container(
              height: 160,
              width: double.infinity,
              color: colores.surfaceContainerHighest,
              child: Image.asset(
                'assets/img_catalogo.jpg',
                fit: modo.$2,
                semanticLabel: 'Imagen local del catálogo',
              ),
            ),
            // Imagen desde URL
            const SizedBox(height: 12),
            Text('Imagen cargada desde una URL', style: tema.textTheme.labelLarge),
            const SizedBox(height: 8),
            Container(
              height: 160,
              width: double.infinity,
              color: colores.surfaceContainerHighest,
              child: Image.network(
                urlImagen,
                key: ValueKey(urlImagen),
                fit: modo.$2,
                semanticLabel: 'Imagen descargada de internet',
                // Indicador mientras se descarga
                loadingBuilder: (contexto, imagen, progreso) {
                  if (progreso == null) return imagen;
                  return const Center(child: CircularProgressIndicator());
                },
                // Mensaje si no hay internet
                errorBuilder: (contexto, error, pila) => const Center(
                  child: Padding(
                    padding: EdgeInsets.all(16),
                    child: Text(
                      'No se pudo cargar la imagen. Revisa tu conexión a internet.',
                      textAlign: TextAlign.center,
                    ),
                  ),
                ),
              ),
            ),
            Respuesta('Imagen de picsum.photos (foto ${_indiceImagen + 1} de ${_idsImagen.length})'),
            const SizedBox(height: 8),
            OutlinedButton.icon(
              icon: const Icon(Icons.refresh),
              label: const Text('Cargar otra imagen'),
              onPressed: () => setState(() => _indiceImagen = (_indiceImagen + 1) % _idsImagen.length),
            ),
            // Información adicional (solo en modo detallado)
            if (estado.modoDetallado)
              Padding(
                padding: const EdgeInsets.only(top: 8),
                child: Text(
                  'Modo detallado: la imagen local se redujo a 1200 px de ancho para no pesar de más; la imagen remota es de 800 × 450 px.',
                  style: tema.textTheme.bodySmall?.copyWith(color: colores.tertiary),
                ),
              ),
          ],
        ),

        // Indicadores de progreso
        TarjetaElemento(
          titulo: 'Indicadores de progreso',
          descripcion:
              'Indican que algo está en curso. El modo determinado muestra el avance exacto; el indeterminado se usa cuando no se sabe cuánto falta. La barra lineal determinada refleja el deslizador de la Sección 3.',
          hijos: [
            // Lineal determinado (valor del deslizador de la Sección 3)
            Text(
              'Lineal determinado: ${estado.nivel} % (valor del deslizador de la Sección 3)',
              style: tema.textTheme.labelLarge,
            ),
            const SizedBox(height: 8),
            LinearProgressIndicator(
              value: estado.nivel / 100,
              minHeight: 8,
              borderRadius: BorderRadius.circular(4),
            ),
            // Lineal indeterminado
            const SizedBox(height: 16),
            Text('Lineal indeterminado', style: tema.textTheme.labelLarge),
            const SizedBox(height: 8),
            LinearProgressIndicator(minHeight: 8, borderRadius: BorderRadius.circular(4)),
            // Circulares
            const SizedBox(height: 16),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                SizedBox(
                  width: 56,
                  height: 56,
                  child: CircularProgressIndicator(value: _progresoSimulado / 100, strokeWidth: 6),
                ),
                Expanded(
                  child: Text(
                    'Circular determinado: $_progresoSimulado %',
                    textAlign: TextAlign.center,
                    style: tema.textTheme.labelLarge,
                  ),
                ),
                const SizedBox(width: 56, height: 56, child: CircularProgressIndicator(strokeWidth: 6)),
              ],
            ),
            const SizedBox(height: 8),
            FilledButton.tonal(
              onPressed: (_temporizador?.isActive ?? false) ? null : _simularCarga,
              child: const Text('Simular carga'),
            ),
          ],
        ),

        // Mensaje emergente (toast) y mensaje con acción (snackbar)
        TarjetaElemento(
          titulo: 'Toast y Snackbar',
          descripcion:
              'El toast es un aviso breve que desaparece solo. Flutter no trae un toast nativo, así que se imita con un SnackBar flotante y corto. El snackbar normal puede incluir una acción, como «Deshacer».',
          hijos: [
            Wrap(
              spacing: 8,
              runSpacing: 8,
              children: [
                // Toast (SnackBar flotante y corto, sin acción)
                OutlinedButton(
                  onPressed: () {
                    final mensajero = ScaffoldMessenger.of(context);
                    mensajero.hideCurrentSnackBar();
                    mensajero.showSnackBar(
                      const SnackBar(
                        content: Text('Este es un mensaje emergente (toast)'),
                        duration: Duration(seconds: 1),
                        behavior: SnackBarBehavior.floating,
                      ),
                    );
                    setState(() => _respuestaMensajes = 'Se mostró un toast');
                  },
                  child: const Text('Mostrar toast'),
                ),
                // Snackbar con acción
                FilledButton(
                  onPressed: () {
                    setState(() => _respuestaMensajes = 'Se mostró un snackbar con acción');
                    mostrarMensaje(
                      context,
                      'Mensaje con acción (snackbar)',
                      accion: 'Deshacer',
                      alAccion: () => setState(() => _respuestaMensajes = 'Tocaste la acción «Deshacer» del snackbar'),
                    );
                  },
                  child: const Text('Mostrar snackbar'),
                ),
              ],
            ),
            Respuesta(_respuestaMensajes),
          ],
        ),

        // Diálogo de confirmación
        TarjetaElemento(
          titulo: 'Diálogo de confirmación',
          descripcion:
              'Ventana que interrumpe al usuario para pedirle una decisión importante, como confirmar una eliminación. Hay que responder para continuar.',
          hijos: [
            FilledButton(onPressed: _abrirDialogo, child: const Text('Abrir diálogo')),
            Respuesta(_respuestaDialogo),
          ],
        ),

        // Hoja inferior (bottom sheet)
        TarjetaElemento(
          titulo: 'Hoja inferior (bottom sheet)',
          descripcion:
              'Panel que sube desde la parte inferior con opciones adicionales, sin salir de la pantalla actual. Se cierra deslizándolo hacia abajo o tocando fuera.',
          hijos: [
            FilledButton.tonal(onPressed: _abrirHoja, child: const Text('Abrir hoja inferior')),
            Respuesta(_respuestaHoja),
          ],
        ),

        // Tarjeta, separador y distintivo numérico
        TarjetaElemento(
          titulo: 'Tarjeta, separador y distintivo numérico',
          descripcion:
              'La tarjeta agrupa información relacionada (tócala para marcarla), el separador divide contenido y el distintivo numérico (badge) indica cuántos avisos pendientes hay.',
          hijos: [
            // Tarjeta seleccionable
            Card(
              elevation: 0,
              margin: EdgeInsets.zero,
              clipBehavior: Clip.antiAlias,
              color: _tarjetaMarcada ? colores.primaryContainer : colores.surfaceContainerHighest,
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
              child: InkWell(
                onTap: () => setState(() => _tarjetaMarcada = !_tarjetaMarcada),
                child: Padding(
                  padding: const EdgeInsets.all(16),
                  child: SizedBox(
                    width: double.infinity,
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text('Tarjeta de ejemplo', style: tema.textTheme.titleSmall),
                        const SizedBox(height: 4),
                        Text(
                          _tarjetaMarcada ? 'Tarjeta marcada' : 'Tarjeta sin marcar',
                          style: tema.textTheme.bodyMedium?.copyWith(color: colores.onSurfaceVariant),
                        ),
                        // Información adicional (solo en modo detallado)
                        if (estado.modoDetallado)
                          Padding(
                            padding: const EdgeInsets.only(top: 8),
                            child: Text(
                              'Modo detallado: esta línea solo aparece cuando activas el interruptor de la Sección 3.',
                              style: tema.textTheme.bodySmall?.copyWith(color: colores.tertiary),
                            ),
                          ),
                      ],
                    ),
                  ),
                ),
              ),
            ),
            // Separador
            const SizedBox(height: 16),
            const Text('Contenido de arriba'),
            _mostrarSeparador ? const Divider(height: 17) : const SizedBox(height: 17),
            const Text('Contenido de abajo'),
            SwitchListTile(
              contentPadding: EdgeInsets.zero,
              title: const Text('Mostrar separador'),
              value: _mostrarSeparador,
              onChanged: (v) => setState(() => _mostrarSeparador = v),
            ),
            // Distintivo numérico (badge)
            Row(
              children: [
                Badge(
                  isLabelVisible: _avisos > 0,
                  label: Text(_avisos > 99 ? '99+' : '$_avisos'),
                  child: const Icon(Icons.notifications, size: 32),
                ),
                const SizedBox(width: 16),
                OutlinedButton(onPressed: () => setState(() => _avisos++), child: const Text('+1')),
                const SizedBox(width: 8),
                TextButton(onPressed: () => setState(() => _avisos = 0), child: const Text('Reiniciar')),
              ],
            ),
          ],
        ),
      ],
    );
  }
}
