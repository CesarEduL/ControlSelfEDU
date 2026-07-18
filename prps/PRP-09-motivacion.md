# PRP-09 — Sistema de motivación

## Objetivo

Motivar el estudio constante con rachas diarias, logros, reconocimientos por uso responsable y medallas por rendimiento.

## Alcance

### Rachas diarias

- Contador de días consecutivos con al menos una actividad de estudio (evaluación iniciada o aprobada — decidir: **recomendación:** día cuenta si hubo intento o sesión de estudio ≥ N minutos).
- Mostrar en panel estudiante (PRP-04): “N días seguidos aprendiendo”.
- Se rompe si pasa un día calendario sin actividad.

### Logros (ejemplos del brief + extras MVP)

| Logro | Condición sugerida |
|-------|--------------------|
| Principiante | Primera evaluación completada |
| Constante | Racha ≥ 3 días |
| Estudiante destacado | Racha ≥ 7 o promedio ≥ 16/20 |
| Maestro del aprendizaje | Racha ≥ 30 o 20 evaluaciones aprobadas |
| Uso responsable | Varios días bajo el límite sin bloqueo forzado |

### Medallas / reconocimientos

- Insignias visuales en “Mi racha”.
- Historial simple de logros desbloqueados (fecha).

### Eventos que otorgan progreso

- Evaluación aprobada (PRP-08).
- Mantenerse bajo 30 min (PRP-05) — reconocimiento semanal opcional.

## Fuera de alcance (por ahora)

- Tienda de recompensas, puntos canjeables, leaderboards sociales.
- Notificaciones de “¡no pierdas tu racha!” (fase posterior).

## Dependencias con otros PRPs

| PRP | Relación |
|-----|----------|
| 04 | UI de racha e insignias |
| 08 | Eventos de aprobación |
| 10 | Métricas de logros |
| 12 | Padre ve logros del hijo |

## Criterios de aceptación

- [ ] Racha se incrementa y se rompe según reglas documentadas.
- [ ] Al menos 4 insignias del brief implementables.
- [ ] Logros se persisten y se muestran en el panel.
- [ ] No se puede “farmear” el mismo logro dos veces.

## Notas técnicas

- `Achievement`, `StreakState`, `AchievementRepository`
- Evaluar logros en un use case tras cada intento/aprobación
