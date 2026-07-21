# ControlSelf EDU

App Android de control parental educativo: monitorea tiempo en redes y videojuegos, bloquea tras 30 minutos y desbloquea al aprobar una lección (mínimo 3/4).

## Stack

- Kotlin 1.9 · Jetpack Compose · Material 3
- Navigation Compose · SplashScreen API
- AGP 8.6.1 · Gradle 8.7 · minSdk 26 · targetSdk 35
- Paquete: `com.controlself.edu`

## Empezar

1. Abrir en Android Studio: **File → Open** → esta carpeta
2. Esperar el sync de Gradle
3. Ejecutar en emulador o dispositivo

Credenciales de demo y pruebas manuales: [`docs/desarrollo.md`](docs/desarrollo.md).  
Generar e instalar un APK: [`docs/apk.md`](docs/apk.md).

## Documentación

Índice en [`docs/README.md`](docs/README.md):

- [Arquitectura](docs/arquitectura.md) — capas, paquetes, rutas y permisos
- [Estado funcional](docs/estado.md) — capacidades implementadas
- [Desarrollo local](docs/desarrollo.md) — entorno y pruebas manuales
- [Generar APK](docs/apk.md) — `assembleDebug` / Studio e instalación con ADB
