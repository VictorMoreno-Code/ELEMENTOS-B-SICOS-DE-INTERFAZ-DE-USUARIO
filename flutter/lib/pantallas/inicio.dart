import 'package:flutter/material.dart';

import '../navegacion.dart';
import '../tema.dart';
import '../widgets/componentes.dart';

// Pantalla principal: presenta la app y permite entrar a las seis secciones
class Inicio extends StatelessWidget {
  const Inicio({super.key});

  @override
  Widget build(BuildContext context) {
    final tema = Theme.of(context);
    final irA = Navegador.of(context);

    final secciones = [
      (Destino.seccion1, 'Entrada de texto', 'Campos, validación, contraseña, teclados y búsqueda'),
      (Destino.seccion2, 'Botones y acciones', 'Botones, íconos, FAB, alternancia y carga'),
      (Destino.seccion3, 'Elementos de selección', 'Casillas, opciones, interruptor, deslizadores, fecha y chips'),
      (Destino.seccion4, 'Listas y colecciones', 'Listas, cuadrícula, detalle, deslizar para borrar y pestañas'),
      (Destino.seccion5, 'Información y retroalimentación', 'Textos, imágenes, progreso, mensajes, diálogo y bottom sheet'),
      (Destino.seccion6, 'Contenedores y estructura', 'Fila, columna, superpuesta, barra superior, navegación y pesos'),
    ];

    return SingleChildScrollView(
      padding: const EdgeInsets.all(16),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // Tarjeta de bienvenida con degradado azul
          Container(
            width: double.infinity,
            padding: const EdgeInsets.all(24),
            decoration: BoxDecoration(
              borderRadius: BorderRadius.circular(24),
              gradient: const LinearGradient(
                colors: [degradadoInicio, degradadoFin],
                begin: Alignment.topLeft,
                end: Alignment.bottomRight,
              ),
            ),
            child: const Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  'UIvision Flutter',
                  style: TextStyle(color: Colors.white, fontSize: 36, fontWeight: FontWeight.bold),
                ),
                SizedBox(height: 8),
                Text(
                  'Catálogo interactivo de elementos básicos de interfaz de usuario. Cada elemento muestra su nombre, una explicación breve y una demostración con la que puedes interactuar.',
                  style: TextStyle(color: Colors.white, fontSize: 16),
                ),
                SizedBox(height: 12),
                Text(
                  'Flutter · Dart · Material 3',
                  style: TextStyle(color: Colors.white, fontSize: 14, fontWeight: FontWeight.w600),
                ),
              ],
            ),
          ),
          const SizedBox(height: 24),
          Text('Secciones', style: tema.textTheme.titleLarge?.copyWith(fontWeight: FontWeight.bold)),
          const SizedBox(height: 12),
          // Tarjetas de las seis secciones
          for (var i = 0; i < secciones.length; i++) ...[
            Card(
              elevation: 0,
              margin: EdgeInsets.zero,
              color: tema.colorScheme.surface,
              clipBehavior: Clip.antiAlias,
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(16),
                side: BorderSide(color: tema.colorScheme.outlineVariant),
              ),
              child: InkWell(
                onTap: () => irA(secciones[i].$1),
                child: Padding(
                  padding: const EdgeInsets.all(16),
                  child: Row(
                    children: [
                      // Número de la sección
                      CircleAvatar(
                        radius: 22,
                        backgroundColor: tema.colorScheme.primaryContainer,
                        child: Text(
                          '${i + 1}',
                          style: TextStyle(
                            fontWeight: FontWeight.bold,
                            color: tema.colorScheme.onPrimaryContainer,
                          ),
                        ),
                      ),
                      const SizedBox(width: 16),
                      Expanded(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              secciones[i].$2,
                              style: tema.textTheme.titleMedium?.copyWith(fontWeight: FontWeight.bold),
                            ),
                            Text(
                              secciones[i].$3,
                              style: tema.textTheme.bodySmall?.copyWith(color: tema.colorScheme.onSurfaceVariant),
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
            const SizedBox(height: 12),
          ],
          const SizedBox(height: 4),
          // Explicación de la conexión entre secciones
          TarjetaElemento(
            titulo: 'Conexión entre secciones',
            descripcion:
                'Las secciones comparten datos: lo que agregues desde la Sección 1 aparece en la lista de la Sección 4, y el interruptor y el deslizador de la Sección 3 cambian lo que se muestra en la Sección 5.',
            hijos: [
              FilledButton.tonal(
                onPressed: () => irA(Destino.seccion1),
                child: const Text('Probar la conexión'),
              ),
            ],
          ),
        ],
      ),
    );
  }
}
