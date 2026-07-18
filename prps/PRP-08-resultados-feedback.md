# PRP-08 — Resultados y feedback

## Objetivo

Mostrar el resultado de la evaluación: felicitar y desbloquear si ≥15/20, o explicar errores y forzar reintento si no alcanza el mínimo.

## Alcance

### Éxito (≥ 15 correctas)

| Elemento | Spec |
|----------|------|
| Título | ¡Felicitaciones! |
| Score | Has obtenido **X/20** respuestas correctas |
| Estado | Aplicaciones desbloqueadas |
| Botones | Ver resultados · Volver al inicio |

Acciones: `entertainment_locked = false`; opcional grace o reset parcial del contador (decidir: desbloqueo hasta nuevo tope del día vs. +30 min — documentar en implementación; **recomendación:** desbloquea el resto del día tras aprobar una vez).

### Fallo (< 15)

| Elemento | Spec |
|----------|------|
| Título | ¡Sigue intentándolo! |
| Score | Obtuviste **X/20** |
| Feedback | Se muestran respuestas correctas y en cuáles erró |
| CTA | **Reintentar** (misma evaluación) |

El estudiante **debe** resolver nuevamente la misma evaluación; no se desbloquea.

### Ver resultados (detalle)

- Lista de 20 ítems: enunciado corto, respuesta del usuario, correcta, marca ok/error.
- Accesible tras éxito o tras fallo (en fallo, antes o junto al reintento).

## Fuera de alcance (por ahora)

- Explicaciones pedagógicas largas por IA.
- Cambio automático a otra lección más fácil.

## Dependencias con otros PRPs

| PRP | Relación |
|-----|----------|
| 07 | Intento y score |
| 06 | Quita el bloqueo si aprueba |
| 09 | Puede disparar logros |
| 10 | Alimenta promedio y conteos |

## Criterios de aceptación

- [ ] Pantalla de éxito con copy del brief y desbloqueo efectivo.
- [ ] Pantalla de fallo con feedback y reintento de la misma lección.
- [ ] “Ver resultados” lista aciertos y errores.
- [ ] No hay atajo para saltarse el reintento tras un fallo.

## Notas técnicas

- Rutas: `quiz/result/{attemptId}`, `quiz/review/{attemptId}`
- Reintento: mismo `quizId`, nuevo `attemptId`
