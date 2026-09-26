import 'package:flutter/material.dart';
import 'package:flutter_localizations/flutter_localizations.dart';

import 'estado.dart';
import 'navegacion.dart';
import 'pantallas/inicio.dart';
import 'pantallas/seccion1.dart';
import 'pantallas/seccion2.dart';
import 'pantallas/seccion3.dart';
import 'pantallas/seccion4.dart';
import 'pantallas/seccion5.dart';
import 'pantallas/seccion6.dart';
import 'tema.dart';

void main() {
  runApp(const AppUIvision());
}

// Aplicación principal: tema claro/oscuro según el sistema y textos en español
class AppUIvision extends StatefulWidget {
  const AppUIvision({super.key});

  @override
  State<AppUIvision> createState() => _AppUIvisionState();
}

class _AppUIvisionState extends State<AppUIvision> {
  // Datos compartidos entre las secciones
  final Estado _estado = Estado();

  @override
  void dispose() {
    _estado.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return EstadoScope(
      estado: _estado,
      child: MaterialApp(
        title: 'UIvision Flutter',
        debugShowCheckedModeBanner: false,
        theme: temaClaro,
        darkTheme: temaOscuro,
        themeMode: ThemeMode.system,
        // Idioma español (también para los selectores de fecha y hora)
        locale: const Locale('es', 'MX'),
        supportedLocales: const [Locale('es', 'MX')],
        localizationsDelegates: const [
          GlobalMaterialLocalizations.delegate,
          GlobalWidgetsLocalizations.delegate,
          GlobalCupertinoLocalizations.delegate,
        ],
        home: const AppPrincipal(),
      ),
    );
  }
}

// Estructura principal: menú lateral + barra superior + contenido
class AppPrincipal extends StatefulWidget {
  const AppPrincipal({super.key});

  @override
  State<AppPrincipal> createState() => _AppPrincipalState();
}

class _AppPrincipalState extends State<AppPrincipal> {
  Destino _destino = Destino.inicio;

  // Cambia de sección
  void _irA(Destino nuevo) => setState(() => _destino = nuevo);

  // Contenido de la sección elegida
  Widget _contenido() {
    switch (_destino) {
      case Destino.inicio:
        return const Inicio();
      case Destino.seccion1:
        return const Seccion1();
      case Destino.seccion2:
        return const Seccion2();
      case Destino.seccion3:
        return const Seccion3();
      case Destino.seccion4:
        return const Seccion4();
      case Destino.seccion5:
        return const Seccion5();
      case Destino.seccion6:
        return const Seccion6();
    }
  }

  // Diálogo "Acerca de" con los datos de la práctica
  void _mostrarAcerca() {
    showDialog<void>(
      context: context,
      builder: (contexto) => AlertDialog(
        title: const Text('Acerca de UIvision Flutter'),
        content: const Text(
          'Tarea 2: Elementos básicos de interfaz de usuario\n\n'
          'Versión: Flutter (Dart)\n'
          'Alumno: Moreno López Victor Eduardo\n'
          'Boleta: 2024630639\n'
          'Grupo: 7CV4\n'
          'Profesor: Gabriel Hurtado Avilés',
        ),
        actions: [
          TextButton(onPressed: () => Navigator.pop(contexto), child: const Text('Cerrar')),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final colores = Theme.of(context).colorScheme;
    return Navegador(
      irA: _irA,
      // El botón atrás regresa al inicio antes de cerrar la app
      child: PopScope(
        canPop: _destino == Destino.inicio,
        onPopInvokedWithResult: (seSalio, resultado) {
          if (!seSalio) _irA(Destino.inicio);
        },
        child: Scaffold(
          // Barra superior con título y acciones
          appBar: AppBar(
            title: Text(_destino.titulo, maxLines: 1, overflow: TextOverflow.ellipsis),
            backgroundColor: colores.surface,
            foregroundColor: colores.primary,
            scrolledUnderElevation: 0,
            actions: [
              IconButton(
                icon: const Icon(Icons.info),
                tooltip: 'Acerca de',
                onPressed: _mostrarAcerca,
              ),
            ],
          ),
          // Menú lateral
          drawer: Drawer(
            child: ListView(
              padding: EdgeInsets.zero,
              children: [
                // Encabezado con degradado azul
                Container(
                  width: double.infinity,
                  padding: const EdgeInsets.fromLTRB(20, 56, 20, 20),
                  decoration: const BoxDecoration(
                    gradient: LinearGradient(
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
                        style: TextStyle(color: Colors.white, fontSize: 28, fontWeight: FontWeight.bold),
                      ),
                      SizedBox(height: 4),
                      Text(
                        'Catálogo de elementos de interfaz con Flutter',
                        style: TextStyle(color: Colors.white, fontSize: 14),
                      ),
                    ],
                  ),
                ),
                // Opciones del menú
                for (final opcion in Destino.values)
                  ListTile(
                    leading: Icon(opcion.icono),
                    title: Text(opcion.titulo),
                    selected: _destino == opcion,
                    onTap: () {
                      Navigator.pop(context);
                      _irA(opcion);
                    },
                  ),
              ],
            ),
          ),
          body: _contenido(),
        ),
      ),
    );
  }
}
