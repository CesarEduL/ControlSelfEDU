# PRP-10 — Estadísticas

## Objetivo

Registrar y presentar con gráficos fáciles de interpretar: tiempo de uso, tiempo de estudio, evaluaciones, promedios, cursos, logros y rachas.

## Alcance

### Métricas automáticas

| Métrica | Fuente |
|---------|--------|
| Tiempo de uso del teléfono (entretenimiento) | PRP-05 |
| Tiempo dedicado al estudio | Duración de sesiones de quiz / timer |
| Nº de evaluaciones realizadas | Intentos (PRP-07/08) |
| Promedio de calificaciones | Media de scores |
| Cursos completados | Lecciones aprobadas por curso |
| Logros obtenidos | PRP-09 |
| Rachas alcanzadas | Máxima y actual (PRP-09) |

### Visualización (estudiante)

Sección “Mi progreso” (PRP-04) y/o pantalla `student/stats`:

- Barras o líneas simples: notas en el tiempo, minutos estudio vs redes, distribución por curso.
- Evitar dashboards saturados: **una idea por gráfico**.

### Visualización (otros roles)

- Docente: agregados del salón (PRP-11).
- Padre: evolución semanal del hijo (PRP-12).

## Fuera de alcance (por ahora)

- Export CSV/PDF (salvo “Descargar reportes” docente en PRP-11).
- Analytics de terceros (Firebase Analytics opcional más adelante).

## Dependencias con otros PRPs

| PRP | Relación |
|-----|----------|
| 05, 07–09 | Datos |
| 04, 11, 12 | Superficies UI |

## Criterios de aceptación

- [ ] Al menos 3 gráficos legibles en el panel estudiante.
- [ ] Datos coherentes con intentos y tiempo reales (o mock etiquetado).
- [ ] Vacío con estado empty (“Aún no hay datos”).

## Notas técnicas

- Librería sugerida: Vico, o charts Compose ligeros.
- Agregar `StatsAggregator` que lea repositorios existentes (no duplicar verdad)
