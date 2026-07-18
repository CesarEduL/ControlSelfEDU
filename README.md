# ControlSelf EDU

App Android de control parental educativo: monitorea tiempo en redes/videojuegos, bloquea tras 30 minutos y desbloquea al aprobar una lección (mín. 15/20).

## Stack

- Kotlin 1.9 + Jetpack Compose + Material 3
- Navigation Compose · SplashScreen API
- AGP 8.6.1 · Gradle 8.7 · minSdk 26 · targetSdk 35
- package `com.controlself.edu`

## Abrir en Android Studio

1. File → Open → esta carpeta
2. Espera el sync de Gradle
3. Run en emulador o dispositivo

## Plan de PRs (paso a paso)

| PR | Rama sugerida | Contenido |
|----|---------------|-----------|
| **#1** | `main` / `feat/proyecto-base` | Proyecto base, tema azul/blanco/verde, splash, nav mínima |
| **#2** | `feat/auth-login` | Login, registro, recordar sesión, roles (Estudiante / Docente / Padre) |
| **#3** | `feat/panel-estudiante` | Menú principal: tiempo, cursos, racha, progreso |
| **#4** | `feat/monitoreo-uso` | Registro de tiempo en redes sociales y videojuegos |
| **#5** | `feat/bloqueo-automatico` | Overlay de bloqueo a los 30 min + CTA “Comenzar lección” |
| **#6** | `feat/evaluaciones` | Selección de curso, 20 preguntas (opción múltiple / V-F), calificación ≥15 |
| **#7** | `feat/resultados-feedback` | Pantallas de éxito/reintento + explicación de errores |
| **#8** | `feat/motivacion` | Rachas, logros, medallas |
| **#9** | `feat/estadisticas` | Gráficos (tiempo, notas, cursos, rachas) |
| **#10** | `feat/panel-docente` | Crear/editar evaluaciones, banco de preguntas, reportes |
| **#11** | `feat/panel-padre` | Tiempo, cursos, promedio, logros, evolución semanal |
| **#12** | `feat/anti-desinstalacion` | Device Admin / contraseña de administrador |

Detalle de cada PR: [`docs/PR_PLAN.md`](docs/PR_PLAN.md)

## Estado actual

**PR #1 — Proyecto base** listo para commit/merge:

- Estructura Gradle (AGP 8.9, Kotlin 2.1, Compose)
- Tema ControlSelf (azul / blanco / verde)
- Pantalla de bienvenida con animación → navega a placeholder de login
- Navegación preparada para ampliaciones
