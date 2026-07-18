# PRP-10 — Estadísticas

## Objetivo

Registrar y presentar con gráficos fáciles de interpretar: tiempo de uso, tiempo de estudio, evaluaciones, promedios, cursos, logros y rachas.

## Alcance

### Métricas automáticas

| Métrica | Fuente |
|---------|--------|
| Tiempo de uso (entretenimiento hoy) | PRP-05 `ScreenTimeRepository` |
| Tiempo dedicado al estudio | Duración de sesiones de quiz (`QuizAttempt.durationMillis`) |
| Nº de evaluaciones realizadas | Intentos (PRP-09 agregados + historial stats) |
| Promedio de calificaciones | Media de scores (PRP-09) |
| Cursos con al menos una aprobación | Historial de intentos |
| Logros obtenidos | PRP-09 |
| Rachas actual / máxima | PRP-09 (`maxStreakDays`) |

### Visualización (estudiante)

Sección **Mi progreso** en el home:

1. **Notas recientes** — barras de los últimos intentos (sobre 20).
2. **Estudio vs redes** — minutos de estudio acumulados vs redes hoy.
3. **Aprobadas por curso** — barras Mate / Comms / CyT.

Estado vacío: “Aún no hay datos” si no hay intentos.

### Visualización (otros roles)

- Docente: agregados del salón (PRP-11).
- Padre: evolución semanal del hijo (PRP-12).

## Fuera de alcance (por ahora)

- Export CSV/PDF (salvo “Descargar reportes” docente en PRP-11).
- Analytics de terceros (Firebase Analytics opcional más adelante).
- Pantalla dedicada `student/stats` (métricas viven en “Mi progreso”).
- Librería Vico (charts Compose ligeros propios).

## Dependencias con otros PRPs

| PRP | Relación |
|-----|----------|
| 05, 07–09 | Datos |
| 04, 11, 12 | Superficies UI |

## Criterios de aceptación

- [x] Al menos 3 gráficos legibles en el panel estudiante.
- [x] Datos coherentes con intentos y tiempo reales.
- [x] Vacío con estado empty (“Aún no hay datos”).

## Implementación

| Área | Ubicación |
|------|-----------|
| Modelos | `domain/model/stats/StudentStats.kt` |
| Historial | `data/stats/PersistentStatsRepository.kt` |
| Agregador | `domain/stats/StatsAggregator.kt` |
| UI | `ProgressSection` (métricas + 3 charts) |
| Duración quiz | `QuizAttempt.durationMillis` en `QuizPlayScreen` |

## Notas técnicas

- `StatsAggregator` combina `StatsRepository` + screen time + achievements (no duplica verdad).
- Siguiente: [PRP-11](PRP-11-panel-docente.md) o [PRP-12](PRP-12-panel-padre.md).
