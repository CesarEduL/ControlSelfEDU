# Estado funcional

Resumen orientado a producto (no sustituye el código ni los tests). Actualizar cuando cambien capacidades visibles.

## Implementado

| Área | Qué incluye |
|------|-------------|
| Base Android | Estructura por capas, navegación Compose, contenedor DI manual |
| Bienvenida y tema | Pantalla de entrada y tema «Core Educational Minimalist» |
| Autenticación | Login y recuperación; registro público solo Padre/Docente; `createChildAccount` / vínculos padre→hijo; seed `padre`→`estudiante` |
| Panel estudiante | Home, cursos, estadísticas y perfil (nav inferior) |
| Tiempo de pantalla | Monitoreo vía Usage Stats (gateway Android) |
| Bloqueo | Overlay a los 30 minutos de uso en apps objetivo |
| Evaluaciones | Selección de curso, quiz de 20 preguntas, revisión |
| Resultados | Pantalla de éxito/reintento y feedback tras el quiz |
| Motivación | Rachas, logros y medallas (dominio en app) |
| Estadísticas | Gráficos y métricas de uso/aprendizaje |
| Panel docente | Home, banco de preguntas y flujos de gestión |
| Panel padre/madre | Home, detalle de intentos, protección admin; API de hijos lista (UI multi-hijo → PRP-12) |
| Anti-desinstalación | Device Admin y flujos asociados |

## Mejoras conocidas (no bloqueantes)

- Fuente **Nunito Sans** embebida: hoy se usa SansSerif del sistema con pesos equivalentes.
- Panel padre: alta/listado UI de N hijos (PRP-12) aún no expuesta; la API auth ya existe.

## Ampliaciones futuras

Nuevas capacidades deberían reflejarse aquí y, si aplica, en [arquitectura.md](arquitectura.md). La planificación con PRPs vive fuera de `docs/` (ver [README.md](README.md)).
