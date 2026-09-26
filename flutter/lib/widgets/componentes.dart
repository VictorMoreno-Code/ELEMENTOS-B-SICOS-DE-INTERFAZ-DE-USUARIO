import 'package:flutter/material.dart';

// Forma de los campos de texto
OutlineInputBorder bordeCampo([double radio = 12]) =>
    OutlineInputBorder(borderRadius: BorderRadius.circular(radio));

// Pantalla de una sección: título, subtítulo y contenido con desplazamiento
class PantallaSeccion extends StatelessWidget {
  final String titulo;
  final String subtitulo;
  final List<Widget> hijos;

  const PantallaSeccion({
    super.key,
    required this.titulo,
    required this.subtitulo,
    required this.hijos,
  });

  @override
  Widget build(BuildContext context) {
    final tema = Theme.of(context);
    return SingleChildScrollView(
      padding: const EdgeInsets.all(16),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            titulo,
            style: tema.textTheme.headlineSmall?.copyWith(
              fontWeight: FontWeight.bold,
              color: tema.colorScheme.primary,
            ),
          ),
          const SizedBox(height: 4),
          Text(
            subtitulo,
            style: tema.textTheme.bodyMedium?.copyWith(color: tema.colorScheme.onSurfaceVariant),
          ),
          const SizedBox(height: 16),
          for (final hijo in hijos) ...[hijo, const SizedBox(height: 16)],
        ],
      ),
    );
  }
}

// Tarjeta de documentación: nombre, explicación y demostración interactiva
class TarjetaElemento extends StatelessWidget {
  final String titulo;
  final String descripcion;
  final List<Widget> hijos;

  const TarjetaElemento({
    super.key,
    required this.titulo,
    required this.descripcion,
    required this.hijos,
  });

  @override
  Widget build(BuildContext context) {
    final tema = Theme.of(context);
    return Card(
      elevation: 0,
      margin: EdgeInsets.zero,
      color: tema.colorScheme.surface,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(20),
        side: BorderSide(color: tema.colorScheme.outlineVariant),
      ),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Nombre del elemento
            Text(
              titulo,
              style: tema.textTheme.titleMedium?.copyWith(
                fontWeight: FontWeight.bold,
                color: tema.colorScheme.primary,
              ),
            ),
            const SizedBox(height: 4),
            // Explicación del elemento
            Text(
              descripcion,
              style: tema.textTheme.bodyMedium?.copyWith(color: tema.colorScheme.onSurfaceVariant),
            ),
            const Padding(padding: EdgeInsets.symmetric(vertical: 12), child: Divider(height: 1)),
            // Demostración
            ...hijos,
          ],
        ),
      ),
    );
  }
}

// Texto de respuesta visible al interactuar con una demostración
class Respuesta extends StatelessWidget {
  final String texto;

  const Respuesta(this.texto, {super.key});

  @override
  Widget build(BuildContext context) {
    final tema = Theme.of(context);
    return Padding(
      padding: const EdgeInsets.only(top: 8),
      child: Text(
        texto,
        style: tema.textTheme.bodyMedium?.copyWith(
          fontWeight: FontWeight.bold,
          color: tema.colorScheme.secondary,
        ),
      ),
    );
  }
}

// Muestra un mensaje breve en la parte inferior
void mostrarMensaje(BuildContext context, String mensaje, {String? accion, VoidCallback? alAccion}) {
  final mensajero = ScaffoldMessenger.of(context);
  mensajero.hideCurrentSnackBar();
  mensajero.showSnackBar(
    SnackBar(
      content: Text(mensaje),
      duration: const Duration(seconds: 3),
      action: accion == null ? null : SnackBarAction(label: accion, onPressed: alAccion ?? () {}),
    ),
  );
}
