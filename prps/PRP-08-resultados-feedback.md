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

**Política de desbloqueo (decidida):** al aprobar, `unlockForRestOfDay()` — desbloquea y concede un **pase el resto del día**. UsageStats no vuelve a bloquear hasta el día siguiente (aunque el contador siga ≥ 30 min). Simular bloqueo (QA) o un nuevo día invalidan el pase.

### Fallo (< 15)

| Elemento | Spec |
|----------|------|
| Título | ¡Sigue intentándolo! |
| Score | Obtuviste **X/20** |
| Feedback | Se muestran respuestas correctas y en cuáles erró |
| CTA | **Reintentar** (misma evaluación) |

El estudiante **debe** resolver nuevamente la misma evaluación; no se desbloquea. No hay atajo al home tras un fallo (solo reintento o volver al bloqueo).

### Ver resultados (detalle)

- Ruta `quiz/review/{attemptId}`: lista de 20 ítems (enunciado, respuesta del usuario, correcta, marca ok/error).
- Accesible tras éxito o tras fallo.

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

- [x] Pantalla de éxito con copy del brief y desbloqueo efectivo.
- [x] Pantalla de fallo con feedback y reintento de la misma lección.
- [x] “Ver resultados” lista aciertos y errores.
- [x] No hay atajo para saltarse el reintento tras un fallo.
- [x] Pase el resto del día documentado e implementado.

## Implementación

| Área | Ubicación |
|------|-----------|
| Resultado | `ui/screens/quiz/QuizResultScreen.kt` |
| Detalle | `ui/screens/quiz/QuizReviewScreen.kt` |
| Pase diario | `LockRepository.unlockForRestOfDay()` / `PersistentLockRepository` |
| Anti re-lock | `UsageScreenTimeRepository.refresh` respeta el pase |
| Rutas | `quiz/result/{attemptId}`, `quiz/review/{attemptId}` |

## Notas técnicas

- Reintento: mismo `courseId` / banco, nuevo `attemptId`.
- Siguiente: [PRP-09](PRP-09-motivacion.md).
