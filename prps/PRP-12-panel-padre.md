# PRP-12 — Panel del padre

## Objetivo

Permitir al padre/madre visualizar el progreso del hijo: tiempo en pantalla, estudio, calificaciones, cursos, rachas, logros y evolución semanal.

## Alcance

### Home padre

| Bloque | Spec |
|--------|------|
| Hijo vinculado | MVP: Estudiante Demo del dispositivo (`DEMO-001`) |
| Tiempo en pantalla | Minutos de entretenimiento hoy + suma 7 días |
| Cursos desarrollados | Chips de cursos con intentos / aprobaciones |
| Promedio de calificaciones | Media de scores (PRP-09/10) |
| Logros obtenidos | Insignias desbloqueadas (PRP-09) |
| Racha de estudio | Actual y máxima |
| Evolución semanal | Gráfico 7 días: estudio vs redes |
| Intentos recientes | Lista solo lectura → detalle |

### Vinculación hijo (MVP)

- Un padre → un hijo local (Estudiante Demo).
- Código de vínculo mock: `DEMO-001` (sin backend).

### Acciones

- Ver detalle de un intento (resumen; respuestas si el intento sigue en memoria).
- Aviso de protección admin (PRP-13) — **solo en panel padre**, nunca en pantallas del estudiante.

## Fuera de alcance (por ahora)

- Chat padre–docente.
- Controles remotos en tiempo real.
- Varios hijos / códigos dinámicos.

## Dependencias con otros PRPs

| PRP | Relación |
|-----|----------|
| 03 | Rol padre (`padre` / `123456`) |
| 05, 08–10 | Datos mostrados |
| 13 | Contraseña anti-desinstalación |

## Criterios de aceptación

- [x] Panel muestra todos los bloques del brief.
- [x] Evolución semanal visible (estudio desde intentos; redes desde historial diario).
- [x] Solo lectura sobre evaluaciones (sin editar banco).
- [x] No expone la contraseña admin en pantallas del estudiante.

## Implementación

| Área | Ubicación |
|------|-----------|
| Agregador | `domain/parent/ParentDashboardAggregator.kt` |
| Historial 7 días | `UsageScreenTimeRepository` + `observeWeeklyEntertainmentMinutes` |
| UI | `ui/screens/parent/ParentHomeScreen.kt` |
| Detalle intento | `ParentAttemptDetailScreen` |
| Rutas | `parent/home`, `parent/attempt/{attemptId}` |

## Notas técnicas

- Reutiliza `StatsAggregator.build` + motivación + stats de intentos.
- Demo: login `padre` / `123456`.
- Protección anti-desinstalación: [PRP-13](PRP-13-anti-desinstalacion.md).
