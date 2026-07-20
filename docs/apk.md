# Generar APK

Guía para obtener un `.apk` instalable de **ControlSelf EDU** (`com.controlself.edu`).

## Requisitos

- Proyecto abierto al menos una vez en Android Studio (Gradle sync correcto)
- **JDK 17** (el mismo que usa Android Studio)
- Terminal en la **raíz del repositorio** (donde están `gradlew` y `gradlew.bat`)

En Windows puedes usar Git Bash (`./gradlew`) o CMD/PowerShell (`gradlew.bat`).

## APK de debug (pruebas en dispositivo)

Recomendado para instalar en tu teléfono o emulador sin configurar firma.

### Línea de comandos

```bash
./gradlew assembleDebug
```

En Windows (CMD/PowerShell):

```bat
gradlew.bat assembleDebug
```

**Salida:**

`app/build/outputs/apk/debug/app-debug.apk`

### Android Studio

1. Menú **Build → Build Bundle(s) / APK(s) → Build APK(s)**
2. Esperar la notificación **APK(s) generated successfully**
3. Enlace **locate** → carpeta `debug` con `app-debug.apk`

## APK de release

Para builds más cercanos a producción (sin depuración, optimización según `buildTypes.release` en `app/build.gradle.kts`).

```bash
./gradlew assembleRelease
```

**Salida habitual:**

`app/build/outputs/apk/release/app-release-unsigned.apk`

El proyecto **no** define aún un keystore de release en Gradle. Para publicar en Play Store o firmar con tu clave:

1. Crear o usar un keystore (Android Studio: **Build → Generate Signed App Bundle / APK**).
2. Añadir `signingConfigs` y referenciarlo en `buildTypes.release`, o firmar el APK unsigned con `apksigner`.

Para pruebas locales suele bastar el APK **debug**.

## Instalar el APK

Con [ADB](https://developer.android.com/tools/adb) y depuración USB activada:

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

`-r` reinstala conservando datos si la app ya estaba instalada.

También puedes copiar el archivo al dispositivo y abrirlo desde el gestor de archivos (permite **Orígenes desconocidos** si el sistema lo pide).

## Problemas frecuentes

| Síntoma | Qué revisar |
|---------|-------------|
| Gradle no descarga dependencias | Internet, proxy corporativo, sync en Android Studio |
| `assembleRelease` falla por firma | Usar `assembleDebug` o configurar signing (arriba) |
| El dispositivo no instala | Misma arquitectura, `minSdk` 26, desinstalar versión con otro certificado |

Más contexto de entorno y cuentas demo: [desarrollo.md](desarrollo.md).
