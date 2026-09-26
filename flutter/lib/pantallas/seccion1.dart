import 'package:flutter/material.dart';

import '../estado.dart';
import '../navegacion.dart';
import '../widgets/componentes.dart';

// Ciudades para las sugerencias y la búsqueda
const List<String> _ciudades = [
  'Ciudad de México', 'Guadalajara', 'Monterrey', 'Puebla', 'Querétaro',
  'Toluca', 'Mérida', 'Tijuana', 'León', 'Oaxaca', 'Guanajuato', 'Morelia',
];

// Sección 1: Entrada de texto
class Seccion1 extends StatefulWidget {
  const Seccion1({super.key});

  @override
  State<Seccion1> createState() => _Seccion1State();
}

class _Seccion1State extends State<Seccion1> {
  final _simple = TextEditingController();
  String? _errorSimple;
  final _usuario = TextEditingController();
  final _clave = TextEditingController();
  bool _claveVisible = false;
  final _correo = TextEditingController();
  String? _ciudadElegida;
  final _busqueda = TextEditingController();

  @override
  void dispose() {
    _simple.dispose();
    _usuario.dispose();
    _clave.dispose();
    _correo.dispose();
    _busqueda.dispose();
    super.dispose();
  }

  // Valida el nombre de usuario (4 a 12 caracteres, sin espacios)
  String? _errorUsuario() {
    final texto = _usuario.text;
    if (texto.isEmpty) return null;
    if (texto.contains(' ')) return 'No se permiten espacios';
    if (texto.length < 4) return 'Escribe al menos 4 caracteres';
    if (texto.length > 12) return 'Máximo 12 caracteres';
    return null;
  }

  // Valida el formato del correo electrónico
  String? _errorCorreo() {
    final texto = _correo.text;
    if (texto.isEmpty) return null;
    final valido = RegExp(r'^[\w.+-]+@[\w-]+(\.[\w-]+)+$').hasMatch(texto);
    return valido ? null : 'Correo no válido';
  }

  @override
  Widget build(BuildContext context) {
    final estado = EstadoScope.of(context);
    final irA = Navegador.of(context);
    final errorUsuario = _errorUsuario();
    final errorClave = _clave.text.isNotEmpty && _clave.text.length < 8;
    final consulta = _busqueda.text.trim();
    final resultados = _ciudades.where((c) => c.toLowerCase().contains(consulta.toLowerCase())).toList();

    return PantallaSeccion(
      titulo: 'Sección 1: Entrada de texto',
      subtitulo: 'Elementos con los que el usuario escribe información.',
      hijos: [
        // Campo de texto simple con etiqueta o hint
        TarjetaElemento(
          titulo: 'Campo de texto simple',
          descripcion:
              'Campo básico con una etiqueta que sirve de guía. Escribe algo y agrégalo a la lista de la Sección 4 (conexión entre secciones).',
          hijos: [
            TextField(
              controller: _simple,
              onChanged: (_) => setState(() => _errorSimple = null),
              decoration: InputDecoration(
                labelText: 'Escribe un nombre o una nota',
                border: bordeCampo(),
                errorText: _errorSimple,
              ),
            ),
            // Eco del texto escrito
            Respuesta(_simple.text.isEmpty ? 'Escribiste: (nada todavía)' : 'Escribiste: ${_simple.text}'),
            const SizedBox(height: 8),
            // Botón que envía el texto a la lista de la Sección 4
            FilledButton.icon(
              icon: const Icon(Icons.add),
              label: const Text('Agregar a la lista (Sección 4)'),
              onPressed: () {
                final contenido = _simple.text.trim();
                if (contenido.isEmpty) {
                  setState(() => _errorSimple = 'Escribe algo antes de agregarlo');
                } else {
                  estado.agregar(contenido, 'Sección 1');
                  _simple.clear();
                  setState(() {});
                  mostrarMensaje(
                    context,
                    '«$contenido» se agregó a la lista de la Sección 4',
                    accion: 'Ver',
                    alAccion: () => irA(Destino.seccion4),
                  );
                }
              },
            ),
          ],
        ),

        // Campo con validación y mensaje de error visible
        TarjetaElemento(
          titulo: 'Campo con validación',
          descripcion:
              'Comprueba lo que se escribe mientras se teclea y muestra un mensaje de error cuando el dato no es válido. Prueba con menos de 4 caracteres o con espacios.',
          hijos: [
            TextField(
              controller: _usuario,
              onChanged: (_) => setState(() {}),
              decoration: InputDecoration(
                labelText: 'Nombre de usuario',
                helperText: 'De 4 a 12 caracteres, sin espacios',
                counterText: '${_usuario.text.length}/12',
                errorText: errorUsuario,
                border: bordeCampo(),
              ),
            ),
          ],
        ),

        // Campo de contraseña con opción para mostrar u ocultar el contenido
        TarjetaElemento(
          titulo: 'Campo de contraseña',
          descripcion:
              'Oculta lo que se escribe con puntos. Toca el ojo del extremo derecho para mostrar u ocultar la contraseña.',
          hijos: [
            TextField(
              controller: _clave,
              obscureText: !_claveVisible,
              onChanged: (_) => setState(() {}),
              decoration: InputDecoration(
                labelText: 'Contraseña',
                helperText: 'Mínimo 8 caracteres',
                errorText: errorClave ? 'La contraseña debe tener al menos 8 caracteres' : null,
                border: bordeCampo(),
                suffixIcon: IconButton(
                  tooltip: _claveVisible ? 'Ocultar contraseña' : 'Mostrar contraseña',
                  icon: Icon(_claveVisible ? Icons.visibility_off : Icons.visibility),
                  onPressed: () => setState(() => _claveVisible = !_claveVisible),
                ),
              ),
            ),
          ],
        ),

        // Campos con distintos tipos de teclado
        TarjetaElemento(
          titulo: 'Tipos de teclado',
          descripcion:
              'Cada campo abre un teclado distinto según el tipo de dato: numérico, de correo electrónico o de teléfono. El correo se valida al escribir.',
          hijos: [
            // Teclado numérico
            TextField(
              keyboardType: TextInputType.number,
              decoration: InputDecoration(
                labelText: 'Número (teclado numérico)',
                prefixIcon: const Icon(Icons.tag),
                border: bordeCampo(),
              ),
            ),
            const SizedBox(height: 12),
            // Teclado de correo electrónico
            TextField(
              controller: _correo,
              keyboardType: TextInputType.emailAddress,
              onChanged: (_) => setState(() {}),
              decoration: InputDecoration(
                labelText: 'Correo electrónico',
                prefixIcon: const Icon(Icons.email),
                errorText: _errorCorreo(),
                border: bordeCampo(),
              ),
            ),
            const SizedBox(height: 12),
            // Teclado de teléfono
            TextField(
              keyboardType: TextInputType.phone,
              decoration: InputDecoration(
                labelText: 'Teléfono',
                prefixIcon: const Icon(Icons.phone),
                border: bordeCampo(),
              ),
            ),
          ],
        ),

        // Campo multilínea
        TarjetaElemento(
          titulo: 'Campo multilínea',
          descripcion:
              'Permite escribir textos largos con varios renglones. El contador indica cuántos caracteres llevas del máximo permitido.',
          hijos: [
            TextField(
              keyboardType: TextInputType.multiline,
              minLines: 3,
              maxLines: 6,
              maxLength: 200,
              decoration: InputDecoration(
                labelText: 'Comentarios',
                alignLabelWithHint: true,
                border: bordeCampo(),
              ),
            ),
          ],
        ),

        // Campo con sugerencias automáticas
        TarjetaElemento(
          titulo: 'Campo con sugerencias automáticas',
          descripcion:
              'Mientras escribes, el campo propone opciones que coinciden con el texto. Escribe «Gua» o «Mon» y elige una sugerencia.',
          hijos: [
            Autocomplete<String>(
              optionsBuilder: (valor) {
                return _ciudades.where((c) => c.toLowerCase().contains(valor.text.toLowerCase()));
              },
              onSelected: (ciudad) => setState(() => _ciudadElegida = ciudad),
              fieldViewBuilder: (contexto, controlador, foco, alEnviar) {
                return TextField(
                  controller: controlador,
                  focusNode: foco,
                  decoration: InputDecoration(labelText: 'Ciudad', border: bordeCampo()),
                );
              },
            ),
            Respuesta('Ciudad elegida: ${_ciudadElegida ?? '(ninguna)'}'),
          ],
        ),

        // Barra de búsqueda
        TarjetaElemento(
          titulo: 'Barra de búsqueda',
          descripcion:
              'Campo redondeado para buscar. Filtra la lista de ciudades mientras escribes; el botón X borra el texto y la lupa del teclado confirma la búsqueda.',
          hijos: [
            TextField(
              controller: _busqueda,
              textInputAction: TextInputAction.search,
              onChanged: (_) => setState(() {}),
              onSubmitted: (_) => FocusScope.of(context).unfocus(),
              decoration: InputDecoration(
                labelText: 'Buscar ciudad',
                prefixIcon: const Icon(Icons.search),
                suffixIcon: _busqueda.text.isEmpty
                    ? null
                    : IconButton(
                        tooltip: 'Borrar texto',
                        icon: const Icon(Icons.clear),
                        onPressed: () => setState(_busqueda.clear),
                      ),
                border: bordeCampo(28),
              ),
            ),
            // Resultados de la búsqueda
            Respuesta(
              resultados.isEmpty
                  ? 'Sin resultados para «$consulta»'
                  : consulta.isEmpty
                      ? 'Resultados: ${_ciudades.length} ciudades'
                      : 'Resultados (${resultados.length}): ${resultados.join(', ')}',
            ),
          ],
        ),
      ],
    );
  }
}
