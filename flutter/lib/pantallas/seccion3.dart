import 'package:flutter/material.dart';

import '../estado.dart';
import '../widgets/componentes.dart';

// Sección 3: Elementos de selección
class Seccion3 extends StatefulWidget {
  const Seccion3({super.key});

  @override
  State<Seccion3> createState() => _Seccion3State();
}

class _Seccion3State extends State<Seccion3> {
  static const _meses = [
    'enero', 'febrero', 'marzo', 'abril', 'mayo', 'junio',
    'julio', 'agosto', 'septiembre', 'octubre', 'noviembre', 'diciembre',
  ];
  static const _avisos = ['Avisos por correo', 'Avisos por mensaje SMS', 'Avisos en la aplicación'];
  static const _envios = ['Envío rápido (1 día)', 'Envío estándar (3 días)', 'Envío económico (7 días)'];
  static const _carreras = [
    'Ingeniería en Sistemas Computacionales',
    'Ingeniería en Inteligencia Artificial',
    'Licenciatura en Ciencia de Datos',
  ];
  static const _filtros = ['Android', 'Compose', 'Flutter', 'Kotlin', 'Dart'];

  bool _terminos = false;
  final List<bool> _hijas = [false, true, false];
  int _envio = 1;
  bool _notificaciones = false;
  RangeValues _rango = const RangeValues(20, 70);
  String? _carrera;
  String _fecha = '(sin elegir)';
  String _hora = '(sin elegir)';
  final Set<String> _activos = {};

  // Estado de la casilla principal: marcada, desmarcada o indeterminada (null)
  bool? get _estadoPadre {
    final marcadas = _hijas.where((h) => h).length;
    if (marcadas == 0) return false;
    if (marcadas == _hijas.length) return true;
    return null;
  }

  // Abre el selector de fecha
  Future<void> _elegirFecha() async {
    final fecha = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(2020),
      lastDate: DateTime(2035),
      helpText: 'Selecciona una fecha',
      cancelText: 'Cancelar',
      confirmText: 'Aceptar',
    );
    if (fecha != null) {
      setState(() => _fecha = '${fecha.day} de ${_meses[fecha.month - 1]} de ${fecha.year}');
    }
  }

  // Abre el selector de hora (formato de 24 horas)
  Future<void> _elegirHora() async {
    final hora = await showTimePicker(
      context: context,
      initialTime: const TimeOfDay(hour: 12, minute: 0),
      helpText: 'Selecciona una hora',
      cancelText: 'Cancelar',
      confirmText: 'Aceptar',
      builder: (contexto, hijo) => MediaQuery(
        data: MediaQuery.of(contexto).copyWith(alwaysUse24HourFormat: true),
        child: hijo!,
      ),
    );
    if (hora != null) {
      setState(() {
        _hora = '${hora.hour.toString().padLeft(2, '0')}:${hora.minute.toString().padLeft(2, '0')}';
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    final estado = EstadoScope.of(context);
    final tema = Theme.of(context);
    final marcadas = _hijas.where((h) => h).length;

    return PantallaSeccion(
      titulo: 'Sección 3: Elementos de selección',
      subtitulo: 'Elementos para elegir una o varias opciones.',
      hijos: [
        // Casilla de verificación (con estado indeterminado)
        TarjetaElemento(
          titulo: 'Casilla de verificación',
          descripcion:
              'Permite marcar o desmarcar una opción. La casilla «Seleccionar todos los avisos» tiene tres estados: marcada, desmarcada e indeterminada (cuando solo algunas opciones están marcadas).',
          hijos: [
            // Casilla simple
            CheckboxListTile(
              contentPadding: EdgeInsets.zero,
              controlAffinity: ListTileControlAffinity.leading,
              title: const Text('Acepto los términos y condiciones'),
              value: _terminos,
              onChanged: (valor) => setState(() => _terminos = valor ?? false),
            ),
            // Casilla principal con estado indeterminado
            CheckboxListTile(
              contentPadding: EdgeInsets.zero,
              controlAffinity: ListTileControlAffinity.leading,
              tristate: true,
              title: const Text('Seleccionar todos los avisos'),
              value: _estadoPadre,
              onChanged: (_) => setState(() {
                final marcarTodas = _estadoPadre != true;
                for (var i = 0; i < _hijas.length; i++) {
                  _hijas[i] = marcarTodas;
                }
              }),
            ),
            // Casillas hijas
            for (var i = 0; i < _avisos.length; i++)
              Padding(
                padding: const EdgeInsets.only(left: 32),
                child: CheckboxListTile(
                  contentPadding: EdgeInsets.zero,
                  controlAffinity: ListTileControlAffinity.leading,
                  title: Text(_avisos[i]),
                  value: _hijas[i],
                  onChanged: (valor) => setState(() => _hijas[i] = valor ?? false),
                ),
              ),
            Respuesta('${_terminos ? 'Términos aceptados' : 'Términos sin aceptar'} · Avisos activados: $marcadas de ${_hijas.length}'),
          ],
        ),

        // Grupo de botones de opción
        TarjetaElemento(
          titulo: 'Botones de opción (radio)',
          descripcion:
              'Muestra varias opciones de las que solo se puede elegir una. Al elegir una, la anterior se desmarca automáticamente.',
          hijos: [
            RadioGroup<int>(
              groupValue: _envio,
              onChanged: (valor) => setState(() => _envio = valor ?? _envio),
              child: Column(
                children: [
                  for (var i = 0; i < _envios.length; i++)
                    RadioListTile<int>(
                      contentPadding: EdgeInsets.zero,
                      value: i,
                      title: Text(_envios[i]),
                    ),
                ],
              ),
            ),
            Respuesta('Elegiste: ${_envios[_envio]}'),
          ],
        ),

        // Interruptor (switch)
        TarjetaElemento(
          titulo: 'Interruptor (switch)',
          descripcion:
              'Activa o desactiva un ajuste al instante. El «Modo detallado» está conectado con la Sección 5: al activarlo, allá aparece información adicional.',
          hijos: [
            // Interruptor conectado con la Sección 5
            SwitchListTile(
              contentPadding: EdgeInsets.zero,
              title: const Text('Modo detallado (afecta a la Sección 5)'),
              value: estado.modoDetallado,
              onChanged: (valor) => estado.modoDetallado = valor,
            ),
            // Interruptor independiente
            SwitchListTile(
              contentPadding: EdgeInsets.zero,
              title: const Text('Recibir notificaciones'),
              value: _notificaciones,
              onChanged: (valor) => setState(() => _notificaciones = valor),
            ),
            Respuesta(
              'Modo detallado: ${estado.modoDetallado ? 'activado' : 'desactivado'} · '
              'Notificaciones: ${_notificaciones ? 'activadas' : 'desactivadas'}',
            ),
          ],
        ),

        // Deslizador de valor único y deslizador de rango
        TarjetaElemento(
          titulo: 'Deslizador simple y de rango',
          descripcion:
              'El deslizador simple elige un valor y el de rango elige un mínimo y un máximo. El valor del deslizador simple se muestra como progreso en la Sección 5.',
          hijos: [
            // Deslizador de valor único
            Text('Nivel: ${estado.nivel} (se muestra en la Sección 5)', style: tema.textTheme.labelLarge),
            Slider(
              value: estado.nivel.toDouble(),
              min: 0,
              max: 100,
              divisions: 100,
              label: '${estado.nivel}',
              onChanged: (valor) => estado.nivel = valor.round(),
            ),
            // Deslizador de rango
            Text('Rango: de ${_rango.start.round()} a ${_rango.end.round()}', style: tema.textTheme.labelLarge),
            RangeSlider(
              values: _rango,
              min: 0,
              max: 100,
              divisions: 100,
              labels: RangeLabels('${_rango.start.round()}', '${_rango.end.round()}'),
              onChanged: (valores) => setState(() => _rango = valores),
            ),
          ],
        ),

        // Lista desplegable de selección
        TarjetaElemento(
          titulo: 'Lista desplegable',
          descripcion:
              'Muestra una lista de opciones al tocar el campo y guarda la que elijas. Ocupa poco espacio cuando hay muchas opciones.',
          hijos: [
            DropdownMenu<String>(
              expandedInsets: EdgeInsets.zero,
              label: const Text('Carrera'),
              dropdownMenuEntries: [
                for (final carrera in _carreras) DropdownMenuEntry<String>(value: carrera, label: carrera),
              ],
              onSelected: (valor) => setState(() => _carrera = valor),
            ),
            Respuesta(_carrera == null ? 'Toca el campo para elegir una carrera' : 'Elegiste: $_carrera'),
          ],
        ),

        // Selector de fecha y selector de hora
        TarjetaElemento(
          titulo: 'Selector de fecha y de hora',
          descripcion:
              'Abren un calendario o un reloj para elegir una fecha o una hora sin tener que escribirlas. El resultado aparece debajo.',
          hijos: [
            Wrap(
              spacing: 8,
              runSpacing: 8,
              children: [
                // Selector de fecha
                OutlinedButton.icon(
                  icon: const Icon(Icons.calendar_today),
                  label: const Text('Elegir fecha'),
                  onPressed: _elegirFecha,
                ),
                // Selector de hora
                OutlinedButton.icon(
                  icon: const Icon(Icons.schedule),
                  label: const Text('Elegir hora'),
                  onPressed: _elegirHora,
                ),
              ],
            ),
            Respuesta('Fecha: $_fecha'),
            Respuesta('Hora: $_hora'),
          ],
        ),

        // Chips de filtro seleccionables
        TarjetaElemento(
          titulo: 'Chips de filtro',
          descripcion:
              'Etiquetas compactas que se pueden activar o desactivar para filtrar contenido. Puedes seleccionar varias a la vez.',
          hijos: [
            Wrap(
              spacing: 8,
              runSpacing: 4,
              children: [
                for (final filtro in _filtros)
                  FilterChip(
                    label: Text(filtro),
                    selected: _activos.contains(filtro),
                    onSelected: (seleccionado) => setState(() {
                      if (seleccionado) {
                        _activos.add(filtro);
                      } else {
                        _activos.remove(filtro);
                      }
                    }),
                  ),
              ],
            ),
            Respuesta(_activos.isEmpty ? 'Sin filtros activos' : 'Filtros activos: ${_activos.join(', ')}'),
          ],
        ),
      ],
    );
  }
}
