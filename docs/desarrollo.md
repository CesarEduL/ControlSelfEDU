# Desarrollo local

## Requisitos

- Android Studio reciente (compatible con AGP 8.6)
- JDK configurado por el IDE
- Emulador o dispositivo con API ≥ 26

## Abrir el proyecto

1. **File → Open** → raíz del repositorio (`ControlSelfEDU`)
2. Dejar terminar **Gradle Sync**
3. Variante **debug** → Run

Build por línea de comandos (desde la raíz):

```bash
./gradlew assembleDebug
```

## Prueba manual rápida

Cuentas demo embebidas (rol elegido en **Iniciar sesión** debe coincidir con el usuario):

| Rol | Usuario | Contraseña | Nombre en app |
|-----|---------|------------|---------------|
| Estudiante | `estudiante` | `123456` | Estudiante Demo |
| Docente | `docente` | `123456` | Docente Demo |
| Padre | `padre` | `123456` | Padre Demo |

### Estudiante

1. Iniciar sesión con `estudiante` / `123456` (rol **Estudiante**).
2. En Inicio → **Simular bloqueo** (herramienta demo).
3. En la pantalla de bloqueo → **Comenzar lección** o **Quitar bloqueo (demo)** para volver al inicio sin hacer el quiz.
4. Completar el quiz con **≥ 15 aciertos** para desbloquear de forma “real” en el flujo de lección.

### Docente

1. Iniciar sesión con `docente` / `123456` (rol **Docente**).
2. Explorar banco de preguntas, lista de estudiantes, reportes y estadísticas desde el panel inferior.

### Padre

1. Iniciar sesión con `padre` / `123456` (rol **Padre**).
2. Revisar progreso del hijo vinculado (MVP: datos del Estudiante Demo en el mismo dispositivo) e intentos recientes.

Para permisos reales (Usage Access, overlay, administrador de dispositivo), concederlos cuando la app lo solicite; el mapa está en [arquitectura.md](arquitectura.md).
