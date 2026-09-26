# Tarea 2. Elementos básicos de interfaz de usuario

Catálogo interactivo de elementos de interfaz de usuario, construido tres veces con tecnologías distintas para identificar los componentes básicos de una interfaz móvil, sus equivalencias entre plataformas y las diferencias entre los enfoques de construcción de interfaces.

## Datos de identificación

| Dato | Valor |
|---|---|
| Alumno | Moreno López Victor Eduardo |
| Boleta | 2024630639 |
| Grupo | 7CV4 |
| Profesor | Gabriel Hurtado Avilés |
| Tarea | Tarea 2. Elementos básicos de interfaz de usuario |
| Fecha | 25 de septiembre de 2026 |

## Descripción de la aplicación

La aplicación es un manual vivo de componentes de interfaz. Cada elemento aparece dentro de una tarjeta que muestra su nombre, una explicación de dos o tres líneas y una demostración con la que el usuario puede interactuar. Todo el contenido está en español y la interfaz sigue el modo claro u oscuro del sistema, con una paleta blanquiazul.

Cada versión tiene una pantalla de inicio y seis secciones:

1. Entrada de texto
2. Botones y acciones
3. Elementos de selección
4. Listas y colecciones
5. Información y retroalimentación
6. Contenedores y estructura

La navegación se hace con un menú lateral que incluye la pantalla de inicio y las seis secciones. Desde el menú o con el botón atrás se regresa al inicio. La barra superior tiene una acción "Acerca de" con los datos del alumno.

### Conexión entre secciones

Las tres versiones comparten datos entre secciones:

- Lo que se escribe en la Sección 1 se agrega con un botón a la lista de la Sección 4, donde aparece al inicio con el origen "Sección 1".
- El interruptor "Modo detallado" de la Sección 3 hace que la Sección 5 muestre información adicional en la imagen y en la tarjeta.
- El deslizador simple de la Sección 3 controla la barra de progreso lineal determinada de la Sección 5.

## Tecnologías utilizadas

| Versión | Nombre de la app | Tecnología | Carpeta |
|---|---|---|---|
| 1 | UIViewcraft | Android nativo con Views y XML (Kotlin) | `android-views/` |
| 2 | UIvibe Compose | Android nativo con Jetpack Compose (Kotlin) | `android-compose/` |
| 3 | UIvision Flutter | Flutter (Dart) | `flutter/` |

| Versión | Herramientas y versiones |
|---|---|
| Views y XML | Android Gradle Plugin 9.3.3, compileSdk 37, minSdk 24, Material Components 1.14.0, Navigation 2.6.0, ViewBinding, RecyclerView 1.4.0, ViewPager2 1.1.0, SwipeRefreshLayout 1.1.0 |
| Jetpack Compose | Android Gradle Plugin 9.3.3, Kotlin 2.2.10, Compose BOM 2026.02.01, Material 3, Material Icons Extended, ConstraintLayout Compose 1.1.1, compileSdk 37, minSdk 24 |
| Flutter | Flutter 3.47.4, Dart SDK ^3.13.3, Material 3, flutter_localizations |

## Estructura del repositorio

```
android-views/     Versión con Views y XML
android-compose/   Versión con Jetpack Compose
flutter/           Versión con Flutter
docs/              Capturas de pantalla
apk/               APK de las tres versiones
README.md          Documento principal
```

## Instrucciones de compilación y ejecución

### Requisitos generales

- Android Studio con Android SDK 37 y un dispositivo o emulador con Android 7.0 (API 24) o superior.
- Permiso de internet en las tres versiones para cargar la imagen desde una URL en la Sección 5.

### Android Views y XML

1. Abrir la carpeta `android-views/` en Android Studio.
2. Esperar a que termine la sincronización de Gradle.
3. Elegir un dispositivo y presionar Run.

Para generar el APK desde la carpeta `android-views/`:

```
./gradlew assembleDebug
```

El archivo queda en `android-views/app/build/outputs/apk/debug/app-debug.apk`.

### Jetpack Compose

1. Abrir la carpeta `android-compose/` en Android Studio.
2. Esperar a que termine la sincronización de Gradle.
3. Elegir un dispositivo y presionar Run.

Para generar el APK desde la carpeta `android-compose/`:

```
./gradlew assembleDebug
```

El archivo queda en `android-compose/app/build/outputs/apk/debug/app-debug.apk`.

### Flutter

Desde la carpeta `flutter/`:

```
flutter pub get
flutter run
```

Para generar el APK:

```
flutter build apk
```

El archivo queda en `flutter/build/app/outputs/flutter-apk/app-release.apk`.

## APK

| Versión | Archivo |
|---|---|
| Views y XML | `apk/uiviewcraft.apk` |
| Jetpack Compose | `apk/uivibe-compose.apk` |
| Flutter | `apk/uivision-flutter.apk` |

## Tabla de equivalencias

| Elemento | Views y XML | Jetpack Compose | Flutter |
|---|---|---|---|
| Campo de texto simple | `TextInputLayout` + `TextInputEditText` | `OutlinedTextField` | `TextField` con `InputDecoration` |
| Campo con validación | `TextInputLayout.error` | `OutlinedTextField(isError, supportingText)` | `TextField(errorText)` |
| Contraseña mostrar u ocultar | `endIconMode="password_toggle"` | `PasswordVisualTransformation` + `trailingIcon` con `IconButton` | `obscureText` + `suffixIcon` con `IconButton` |
| Teclados numérico, correo y teléfono | `android:inputType` | `KeyboardOptions(keyboardType)` | `keyboardType: TextInputType` |
| Campo multilínea | `textMultiLine` con `minLines` y `maxLines` | `OutlinedTextField(minLines, maxLines)` | `TextField(minLines, maxLines, maxLength)` |
| Sugerencias automáticas | `MaterialAutoCompleteTextView` | `ExposedDropdownMenuBox` editable | `Autocomplete<String>` |
| Barra de búsqueda | `TextInputLayout` con íconos e `imeOptions` de búsqueda | `OutlinedTextField` con íconos y `KeyboardActions` | `TextField` con íconos y `textInputAction` de búsqueda |
| Botón relleno, con contorno y de solo texto | `MaterialButton` (estilos por defecto, `OutlinedButton` y `TextButton`) | `Button`, `OutlinedButton`, `TextButton` | `FilledButton`, `OutlinedButton`, `TextButton` |
| Botón con ícono | `MaterialButton` con `app:icon` (`IconButton.Filled` y `IconButton.Outlined`) | `FilledIconButton`, `OutlinedIconButton`, `Button` con `Icon` | `IconButton.filled`, `IconButton.outlined`, `FilledButton.icon` |
| Botón de acción flotante normal | `FloatingActionButton` | `FloatingActionButton` | `FloatingActionButton` |
| Botón de acción flotante extendido | `ExtendedFloatingActionButton` con `shrink()` y `extend()` | `ExtendedFloatingActionButton(expanded)` | `FloatingActionButton.extended` (sin equivalente directo de contracción animada: se alterna con un `FloatingActionButton` de solo ícono) |
| Selector segmentado | `MaterialButtonToggleGroup` | `SingleChoiceSegmentedButtonRow` con `SegmentedButton` | `SegmentedButton<T>` |
| Botón de alternancia | `MaterialButton` con `android:checkable` | `OutlinedButton` y `FilledTonalButton` alternados por estado | `OutlinedButton.icon` y `FilledButton.tonalIcon` alternados por estado |
| Botón deshabilitado | `android:enabled="false"` | `enabled = false` | `onPressed: null` |
| Botón en estado de carga | `MaterialButton` + `CircularProgressIndicator` | `Button` + `CircularProgressIndicator` | `FilledButton` + `CircularProgressIndicator` |
| Casilla de verificación | `MaterialCheckBox` | `Checkbox` | `Checkbox` y `CheckboxListTile` |
| Casilla con estado indeterminado | `MaterialCheckBox` con `checkedState` | `TriStateCheckbox` | `CheckboxListTile(tristate: true)` |
| Botones de opción | `RadioGroup` + `MaterialRadioButton` | `RadioButton` dentro de `Row` con `selectable` | `RadioGroup<T>` + `RadioListTile<T>` |
| Interruptor | `MaterialSwitch` | `Switch` | `Switch` y `SwitchListTile` |
| Deslizador de valor único | `Slider` | `Slider` | `Slider` |
| Deslizador de rango | `RangeSlider` | `RangeSlider` | `RangeSlider` |
| Lista desplegable | `MaterialAutoCompleteTextView` con estilo de menú desplegable | `ExposedDropdownMenuBox` de solo lectura | `DropdownMenu<T>` |
| Selector de fecha | `MaterialDatePicker` | `DatePickerDialog` + `DatePicker` | `showDatePicker` |
| Selector de hora | `MaterialTimePicker` | `AlertDialog` + `TimePicker` | `showTimePicker` |
| Chips de filtro | `Chip` con estilo de filtro + `ChipGroup` | `FilterChip` + `FlowRow` | `FilterChip` + `Wrap` |
| Lista vertical | `RecyclerView` + `ListAdapter` | `LazyColumn` | `ListView.builder` |
| Cuadrícula | `RecyclerView` + `GridLayoutManager` | `LazyVerticalGrid` | `GridView.builder` |
| Lista con encabezados de sección | `RecyclerView` con dos `viewType` | `LazyColumn` con `stickyHeader` e `item` | `ListView.builder` con dos tipos de elemento |
| Detalle al tocar un elemento | `setOnClickListener` + `MaterialAlertDialogBuilder` | `OutlinedCard(onClick)` + `AlertDialog` | `ListTile(onTap)` + `showDialog` |
| Deslizar para eliminar | `ItemTouchHelper` | `SwipeToDismissBox` | `Dismissible` |
| Actualizar arrastrando hacia abajo | `SwipeRefreshLayout` | `PullToRefreshBox` | `RefreshIndicator` |
| Estado vacío con ilustración | Layout con `ImageView` (`VectorDrawable`) y texto | `Column` con `Canvas` e `Icon` | `Column` con `Container` circulares e `Icon` |
| Pestañas deslizables | `TabLayout` + `ViewPager2` | `PrimaryTabRow` + `HorizontalPager` | `TabBar` + `TabBarView` |
| Textos con estilos | `TextView` con `textAppearance` y `SpannableString` | `Text` con `TextStyle` y `AnnotatedString` | `Text` con `TextStyle` y `Text.rich` |
| Imagen local | `ImageView` con `scaleType` | `Image` con `contentScale` y `painterResource` | `Image.asset` con `fit` |
| Imagen desde URL | `ImageView` + descarga con `HttpURLConnection` | `Image` + descarga con `HttpURLConnection` | `Image.network` con `fit` |
| Progreso lineal | `LinearProgressIndicator` | `LinearProgressIndicator` | `LinearProgressIndicator` |
| Progreso circular | `CircularProgressIndicator` | `CircularProgressIndicator` | `CircularProgressIndicator` |
| Toast | `Toast` | `Toast` de Android (Compose no tiene un componente propio) | Sin equivalente directo: se imita con un `SnackBar` flotante y breve |
| Snackbar | `Snackbar` | `SnackbarHost` con `SnackbarHostState` | `ScaffoldMessenger` con `SnackBar` |
| Diálogo de confirmación | `MaterialAlertDialogBuilder` | `AlertDialog` | `showDialog` con `AlertDialog` |
| Hoja inferior | `BottomSheetDialog` | `ModalBottomSheet` | `showModalBottomSheet` |
| Tarjeta | `MaterialCardView` | `Card` y `OutlinedCard` | `Card` |
| Separador | `MaterialDivider` | `HorizontalDivider` | `Divider` |
| Distintivo numérico | `TextView` circular en un `FrameLayout` y `BadgeDrawable` en `BottomNavigationView` | `BadgedBox` con `Badge` | `Badge` |
| Distribución en fila | `LinearLayout` horizontal | `Row` | `Row` |
| Distribución en columna | `LinearLayout` vertical | `Column` | `Column` |
| Distribución superpuesta | `FrameLayout` | `Box` | `Stack` |
| Contenedor con desplazamiento vertical | `NestedScrollView` | `Modifier.verticalScroll` | `SingleChildScrollView` |
| Barra superior | `MaterialToolbar` | `TopAppBar` | `AppBar` |
| Barra de navegación inferior | `BottomNavigationView` | `NavigationBar` | `NavigationBar` |
| Menú lateral | `DrawerLayout` + `NavigationView` | `ModalNavigationDrawer` | `Drawer` |
| Navegación entre secciones | Navigation Component con `nav_graph.xml` | Estado con `enum` y `when` | Estado con `enum` y `switch` |
| Distribución con pesos | `layout_weight` | `Modifier.weight` | `Expanded(flex)` |
| Distribución con restricciones | `ConstraintLayout` con `Guideline` | `ConstraintLayout` de Compose con `createGuidelineFromStart` | Sin equivalente directo: se resuelve con `LayoutBuilder` y `Positioned` |
| Estado compartido entre secciones | `ViewModel` con `LiveData` | `ViewModel` con `mutableStateOf` | `ChangeNotifier` con `InheritedNotifier` |

## Capturas de pantalla

Las capturas de las seis secciones se tomaron con el tema oscuro del sistema. La pantalla de inicio de cada versión se muestra además con el tema claro.

### Android Views y XML

**Captura 1. Views y XML, Sección 1: Entrada de texto**

![Captura 1. Views y XML, Sección 1: Entrada de texto](docs/views-01-entrada-texto.png)

**Captura 2. Views y XML, Sección 2: Botones y acciones**

![Captura 2. Views y XML, Sección 2: Botones y acciones](docs/views-02-botones.png)

**Captura 3. Views y XML, Sección 3: Elementos de selección (parte 1)**

![Captura 3. Views y XML, Sección 3: Elementos de selección (parte 1)](docs/views-03-seleccion-a.png)

**Captura 4. Views y XML, Sección 3: Elementos de selección (parte 2)**

![Captura 4. Views y XML, Sección 3: Elementos de selección (parte 2)](docs/views-03-seleccion-b.png)

**Captura 5. Views y XML, Sección 4: Listas y colecciones**

![Captura 5. Views y XML, Sección 4: Listas y colecciones](docs/views-04-listas.png)

**Captura 6. Views y XML, Sección 5: Información y retroalimentación (parte 1)**

![Captura 6. Views y XML, Sección 5: Información y retroalimentación (parte 1)](docs/views-05-informacion-a.png)

**Captura 7. Views y XML, Sección 5: Información y retroalimentación (parte 2)**

![Captura 7. Views y XML, Sección 5: Información y retroalimentación (parte 2)](docs/views-05-informacion-b.png)

**Captura 8. Views y XML, Sección 6: Contenedores y estructura (parte 1)**

![Captura 8. Views y XML, Sección 6: Contenedores y estructura (parte 1)](docs/views-06-contenedores-a.png)

**Captura 9. Views y XML, Sección 6: Contenedores y estructura (parte 2)**

![Captura 9. Views y XML, Sección 6: Contenedores y estructura (parte 2)](docs/views-06-contenedores-b.png)

### Jetpack Compose

**Captura 10. Compose, Sección 1: Entrada de texto**

![Captura 10. Compose, Sección 1: Entrada de texto](docs/compose-01-entrada-texto.png)

**Captura 11. Compose, Sección 2: Botones y acciones**

![Captura 11. Compose, Sección 2: Botones y acciones](docs/compose-02-botones.png)

**Captura 12. Compose, Sección 3: Elementos de selección (parte 1)**

![Captura 12. Compose, Sección 3: Elementos de selección (parte 1)](docs/compose-03-seleccion-a.png)

**Captura 13. Compose, Sección 3: Elementos de selección (parte 2)**

![Captura 13. Compose, Sección 3: Elementos de selección (parte 2)](docs/compose-03-seleccion-b.png)

**Captura 14. Compose, Sección 4: Listas y colecciones**

![Captura 14. Compose, Sección 4: Listas y colecciones](docs/compose-04-listas.png)

**Captura 15. Compose, Sección 5: Información y retroalimentación (parte 1)**

![Captura 15. Compose, Sección 5: Información y retroalimentación (parte 1)](docs/compose-05-informacion-a.png)

**Captura 16. Compose, Sección 5: Información y retroalimentación (parte 2)**

![Captura 16. Compose, Sección 5: Información y retroalimentación (parte 2)](docs/compose-05-informacion-b.png)

**Captura 17. Compose, Sección 6: Contenedores y estructura (parte 1)**

![Captura 17. Compose, Sección 6: Contenedores y estructura (parte 1)](docs/compose-06-contenedores-a.png)

**Captura 18. Compose, Sección 6: Contenedores y estructura (parte 2)**

![Captura 18. Compose, Sección 6: Contenedores y estructura (parte 2)](docs/compose-06-contenedores-b.png)

### Flutter

**Captura 19. Flutter, Sección 1: Entrada de texto**

![Captura 19. Flutter, Sección 1: Entrada de texto](docs/flutter-01-entrada-texto.png)

**Captura 20. Flutter, Sección 2: Botones y acciones**

![Captura 20. Flutter, Sección 2: Botones y acciones](docs/flutter-02-botones.png)

**Captura 21. Flutter, Sección 3: Elementos de selección (parte 1)**

![Captura 21. Flutter, Sección 3: Elementos de selección (parte 1)](docs/flutter-03-seleccion-a.png)

**Captura 22. Flutter, Sección 3: Elementos de selección (parte 2)**

![Captura 22. Flutter, Sección 3: Elementos de selección (parte 2)](docs/flutter-03-seleccion-b.png)

**Captura 23. Flutter, Sección 4: Listas y colecciones**

![Captura 23. Flutter, Sección 4: Listas y colecciones](docs/flutter-04-listas.png)

**Captura 24. Flutter, Sección 5: Información y retroalimentación (parte 1)**

![Captura 24. Flutter, Sección 5: Información y retroalimentación (parte 1)](docs/flutter-05-informacion-a.png)

**Captura 25. Flutter, Sección 5: Información y retroalimentación (parte 2)**

![Captura 25. Flutter, Sección 5: Información y retroalimentación (parte 2)](docs/flutter-05-informacion-b.png)

**Captura 26. Flutter, Sección 6: Contenedores y estructura (parte 1)**

![Captura 26. Flutter, Sección 6: Contenedores y estructura (parte 1)](docs/flutter-06-contenedores-a.png)

**Captura 27. Flutter, Sección 6: Contenedores y estructura (parte 2)**

![Captura 27. Flutter, Sección 6: Contenedores y estructura (parte 2)](docs/flutter-06-contenedores-b.png)

### Tema claro

**Captura 28. Views y XML, pantalla de inicio en tema claro**

![Captura 28. Views y XML, pantalla de inicio en tema claro](docs/views-claro-inicio.png)

**Captura 29. Compose, pantalla de inicio en tema claro**

![Captura 29. Compose, pantalla de inicio en tema claro](docs/compose-claro-inicio.png)

**Captura 30. Flutter, pantalla de inicio en tema claro**

![Captura 30. Flutter, pantalla de inicio en tema claro](docs/flutter-claro-inicio.png)

## Reflexión final

**Tecnología más rápida para construir la interfaz.** Jetpack Compose fue la más rápida. Una pantalla completa se resuelve con funciones que describen la interfaz y su estado en el mismo lugar, sin archivos de diseño aparte. Flutter quedó muy cerca porque comparte esa misma forma de pensar. Views y XML fue la más lenta, ya que cada elemento pide un archivo de diseño, un identificador, un binding y código que lo conecte, y las listas necesitan además un adaptador.

**Código más legible.** Compose y Flutter resultaron más legibles porque cada sección se lee de arriba hacia abajo como una descripción de lo que se ve. En Compose el estado de cada demostración vive junto al elemento que lo usa. En Flutter el árbol de widgets se anida mucho y hubo que dividir el código en archivos por sección para mantenerlo claro. En Views la interfaz queda separada de su lógica, lo que ordena el proyecto, pero obliga a saltar entre el XML y el Kotlin para entender un solo elemento.

**Dificultades encontradas.**

- Views y XML: la mayor cantidad de código repetitivo, errores de referencias por atributos de color que pertenecen a otra librería, botones de un grupo que necesitaron declararse como `MaterialButton` para poder leer su estado, y una vista propia para las tarjetas de documentación que no conserva `layout_weight` en sus hijos directos.
- Jetpack Compose: varias APIs de Material 3 cambian entre versiones, como los menús desplegables y los selectores, y hubo que decidir qué cargar con librerías y qué resolver a mano, como la imagen desde URL.
- Flutter: la configuración inicial del entorno fue lo más complicado. La compilación pidió instalar el NDK de Android y el instalador automático falló, así que hubo que instalarlo desde Android Studio. Además, Flutter no trae toast ni ConstraintLayout, y esos dos elementos tuvieron que resolverse con alternativas.

**Tecnología preferida para trabajar.** Jetpack Compose. Permite construir interfaces con menos archivos y menos código, el estado se conecta directamente con lo que se ve y la vista previa acelera los ajustes. Flutter sería la segunda opción, sobre todo si se necesitara una app para más de una plataforma con un solo código.

## Referencias

Android Developers. (s. f.). *Jetpack Compose*. Google. https://developer.android.com/compose

Android Developers. (s. f.). *Diseños y vistas*. Google. https://developer.android.com/develop/ui/views/layout/declaring-layout

Flutter. (s. f.). *Flutter documentation*. Google. https://docs.flutter.dev

Google. (s. f.). *Material Design 3*. https://m3.material.io

JetBrains. (s. f.). *Kotlin documentation*. https://kotlinlang.org/docs/home.html

Material Components for Android. (s. f.). *Material Components for Android* [Repositorio]. GitHub. https://github.com/material-components/material-components-android
