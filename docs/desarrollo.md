# Desarrollo local

## Requisitos

- Android Studio reciente (compatible con AGP 8.6)
- JDK configurado por el IDE
- Emulador o dispositivo con API ≥ 26

## Abrir el proyecto

1. **File → Open** → raíz del repositorio (`ControlSelfEDU`)
2. Dejar terminar **Gradle Sync**
3. Variante **debug** → Run

Para compilar un APK e instalarlo fuera de Run, ver [apk.md](apk.md).

## Prueba manual rápida

### Modelo de cuentas

- **Padre** y **Docente** se registran solos (“Crear cuenta”).
- **Estudiante** no se auto-registra: lo crea el padre (credenciales que el padre define). Un padre puede tener varios hijos.
- El docente es independiente y aporta ejercicios al banco comunitario.

Cuentas demo embebidas (seed con la misma lógica; el rol en **Iniciar sesión** debe coincidir con el usuario):

| Rol | Usuario | Contraseña | Nombre en app | Relación |
|-----|---------|------------|---------------|----------|
| Padre | `padre` | `123456` | Padre Demo | Dueño del hijo demo |
| Estudiante | `estudiante` | `123456` | Estudiante Demo | Creado por `padre` |
| Docente | `docente` | `123456` | Docente Demo | Independiente |

### Estudiante

1. Iniciar sesión con `estudiante` / `123456` (rol **Estudiante**). No usar “Crear cuenta” para este rol.
2. En Inicio → **Simular bloqueo** (herramienta demo).
3. En la pantalla de bloqueo → **Comenzar lección** o **Quitar bloqueo (demo)** para volver al inicio sin hacer el quiz.
4. Completar el quiz con **≥ 15 aciertos** para desbloquear de forma “real” en el flujo de lección.

### Docente

1. Iniciar sesión con `docente` / `123456` (rol **Docente**), o registrarse como docente.
2. Explorar banco de preguntas, lista de estudiantes, reportes y estadísticas desde el panel inferior.

### Padre

1. Iniciar sesión con `padre` / `123456` (rol **Padre**), o registrarse como padre.
2. Revisar progreso del hijo vinculado (demo: `estudiante`) e intentos recientes.
3. Cuando esté la UI de PRP-12: crear más estudiantes desde el panel y seleccionar el hijo activo (la API `createChildAccount` ya está en auth).

Para permisos reales (Usage Access, overlay, administrador de dispositivo), concederlos cuando la app lo solicite; el mapa está en [arquitectura.md](arquitectura.md).
