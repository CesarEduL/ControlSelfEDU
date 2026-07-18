# PRP-09 — Sistema de motivación

## Objetivo

Motivar el estudio constante con rachas diarias, logros, reconocimientos por uso responsable y medallas por rendimiento.

## Alcance

### Rachas diarias

- Contador de días consecutivos con al menos **un intento de evaluación completado** ese día calendario.
- Mostrar en panel estudiante (PRP-04): “N días seguidos aprendiendo”.
- Se rompe si pasa un día calendario sin actividad (al abrir el home o al restaurar, la racha visible pasa a 0).

### Logros (MVP)

| Logro | Condición |
|-------|-----------|
| Principiante | Primera evaluación completada |
| Constante | Racha ≥ 3 días |
| Estudiante destacado | Racha ≥ 7 **o** promedio ≥ 16/20 |
| Maestro del aprendizaje | Racha ≥ 30 **o** 20 evaluaciones aprobadas |
| Uso responsable | 3 días distintos con &lt; 30 min de entretenimiento **y** sin bloqueo forzado ese día |

### Medallas / reconocimientos

- Insignias visuales en “Mi racha” (locked / unlocked).
- Historial simple de logros desbloqueados con fecha.

### Eventos que otorgan progreso

- Evaluación completada (intento guardado en PRP-07/08) → racha + evaluación de logros.
- Sync al reanudar el home → uso responsable (PRP-05 minutos + estado de bloqueo PRP-06).

## Fuera de alcance (por ahora)

- Tienda de recompensas, puntos canjeables, leaderboards sociales.
- Notificaciones de “¡no pierdas tu racha!” (fase posterior).
- Contar sesión de estudio por minutos (solo intentos de evaluación).

## Dependencias con otros PRPs

| PRP | Relación |
|-----|----------|
| 04 | UI de racha e insignias |
| 05 | Minutos para uso responsable |
| 06 | Bloqueo forzado invalida el día responsable |
| 08 | Eventos de intento / aprobación |
| 10 | Métricas de logros |
| 12 | Padre ve logros del hijo |

## Criterios de aceptación

- [x] Racha se incrementa y se rompe según reglas documentadas.
- [x] Al menos 4 insignias del brief + Uso responsable (5 total).
- [x] Logros se persisten (DataStore) y se muestran en el panel.
- [x] No se puede “farmear” el mismo logro dos veces (`AchievementId` único).

## Implementación

| Área | Ubicación |
|------|-----------|
| Modelos | `domain/model/motivation/MotivationModels.kt` |
| Contrato | `domain/repository/AchievementRepository.kt` |
| Persistencia | `data/motivation/PersistentAchievementRepository.kt` |
| DI | `AppContainer.achievementRepository` |
| Evento quiz | `QuizPlayScreen` → `onQuizAttempt` |
| UI | `StreakCard` + `StudentHomeScreen` |

## Notas técnicas

- `Achievement`, `StreakState`, `AchievementRepository`
- Evaluar logros en el repositorio tras cada intento / sync de uso responsable
- Siguiente: [PRP-10](PRP-10-estadisticas.md)
