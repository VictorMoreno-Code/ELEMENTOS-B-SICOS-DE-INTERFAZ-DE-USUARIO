import 'package:flutter/material.dart';

// Colores del degradado del encabezado
const Color degradadoInicio = Color(0xFF0B57D0);
const Color degradadoFin = Color(0xFF29B6F6);

// Tema claro: paleta blanquiazul
final ThemeData temaClaro = ThemeData(
  useMaterial3: true,
  scaffoldBackgroundColor: const Color(0xFFF5F9FF),
  colorScheme: const ColorScheme(
    brightness: Brightness.light,
    primary: Color(0xFF0B57D0),
    onPrimary: Color(0xFFFFFFFF),
    primaryContainer: Color(0xFFD3E3FD),
    onPrimaryContainer: Color(0xFF041E49),
    secondary: Color(0xFF0288D1),
    onSecondary: Color(0xFFFFFFFF),
    secondaryContainer: Color(0xFFC9EBFF),
    onSecondaryContainer: Color(0xFF002B45),
    tertiary: Color(0xFF4C6EF5),
    onTertiary: Color(0xFFFFFFFF),
    tertiaryContainer: Color(0xFFDDE4FF),
    onTertiaryContainer: Color(0xFF0A1F6B),
    error: Color(0xFFBA1A1A),
    onError: Color(0xFFFFFFFF),
    errorContainer: Color(0xFFFFDAD6),
    onErrorContainer: Color(0xFF410002),
    surface: Color(0xFFFFFFFF),
    onSurface: Color(0xFF10203A),
    surfaceContainerHighest: Color(0xFFE1E9F5),
    onSurfaceVariant: Color(0xFF44546F),
    outline: Color(0xFF74839C),
    outlineVariant: Color(0xFFC4D0E2),
  ),
);

// Tema oscuro: paleta azul marino
final ThemeData temaOscuro = ThemeData(
  useMaterial3: true,
  scaffoldBackgroundColor: const Color(0xFF0A1628),
  colorScheme: const ColorScheme(
    brightness: Brightness.dark,
    primary: Color(0xFFA8C7FA),
    onPrimary: Color(0xFF062E6F),
    primaryContainer: Color(0xFF0842A0),
    onPrimaryContainer: Color(0xFFD3E3FD),
    secondary: Color(0xFF7FCFFF),
    onSecondary: Color(0xFF003450),
    secondaryContainer: Color(0xFF004B70),
    onSecondaryContainer: Color(0xFFC9EBFF),
    tertiary: Color(0xFFB7C4FF),
    onTertiary: Color(0xFF0A1F6B),
    tertiaryContainer: Color(0xFF2C3E9E),
    onTertiaryContainer: Color(0xFFDDE4FF),
    error: Color(0xFFFFB4AB),
    onError: Color(0xFF690005),
    errorContainer: Color(0xFF93000A),
    onErrorContainer: Color(0xFFFFDAD6),
    surface: Color(0xFF0F1F38),
    onSurface: Color(0xFFE3EAF6),
    surfaceContainerHighest: Color(0xFF2A3B57),
    onSurfaceVariant: Color(0xFFB6C3DA),
    outline: Color(0xFF8090AA),
    outlineVariant: Color(0xFF34455F),
  ),
);
