package com.outlook.victoreduardo.compose_ui.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Esquema de colores del tema claro
private val EsquemaClaro = lightColorScheme(
    primary = AzulPrimario,
    onPrimary = SuperficieClara,
    primaryContainer = AzulContenedor,
    onPrimaryContainer = AzulSobreContenedor,
    secondary = Celeste,
    onSecondary = SuperficieClara,
    secondaryContainer = CelesteContenedor,
    onSecondaryContainer = CelesteSobreContenedor,
    tertiary = Indigo,
    onTertiary = SuperficieClara,
    tertiaryContainer = IndigoContenedor,
    onTertiaryContainer = IndigoSobreContenedor,
    background = FondoClaro,
    onBackground = TextoClaro,
    surface = SuperficieClara,
    onSurface = TextoClaro,
    surfaceVariant = VarianteClara,
    onSurfaceVariant = TextoVarianteClaro,
    outline = BordeClaro,
    outlineVariant = BordeVarianteClaro
)

// Esquema de colores del tema oscuro
private val EsquemaOscuro = darkColorScheme(
    primary = AzulPrimarioOscuro,
    onPrimary = AzulSobrePrimarioOscuro,
    primaryContainer = AzulContenedorOscuro,
    onPrimaryContainer = AzulSobreContenedorOscuro,
    secondary = CelesteOscuro,
    onSecondary = CelesteSobreOscuro,
    secondaryContainer = CelesteContenedorOscuro,
    onSecondaryContainer = CelesteSobreContenedorOscuro,
    tertiary = IndigoOscuro,
    onTertiary = IndigoSobreOscuro,
    tertiaryContainer = IndigoContenedorOscuro,
    onTertiaryContainer = IndigoSobreContenedorOscuro,
    background = FondoOscuro,
    onBackground = TextoOscuro,
    surface = SuperficieOscura,
    onSurface = TextoOscuro,
    surfaceVariant = VarianteOscura,
    onSurfaceVariant = TextoVarianteOscuro,
    outline = BordeOscuro,
    outlineVariant = BordeVarianteOscuro
)

// Tema de la app: sigue el modo claro u oscuro del sistema
@Composable
fun ComposeUITheme(
    modoOscuro: Boolean = isSystemInDarkTheme(),
    contenido: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (modoOscuro) EsquemaOscuro else EsquemaClaro,
        typography = Typography,
        content = contenido
    )
}
