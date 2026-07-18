# PRP-06 — Bloqueo automático

## Objetivo

Cuando el estudiante agota los 30 min de entretenimiento, mostrar una pantalla a pantalla completa en ControlSelf EDU que ofrezca comenzar una lección. El desbloqueo por aprobar (≥15/20) es [PRP-08](PRP-08-resultados-feedback.md).

## Alcance

### Trigger

- `ScreenTimeRepository` ≥ 30 → `LockRepository.setLocked(true)` (ya en PRP-05).
- Flag persistido en **DataStore** (`entertainment_locked`).
- Si el estudiante abre la app con lock activo → destino `lock` (no el home).

### Pantalla de bloqueo (brief)

| Elemento | Spec |
|----------|------|
| Título | ¡Tiempo cumplido! |
| Cuerpo | Has utilizado tus 30 minutos de entretenimiento… |
| CTA | **Comenzar lección** → selección de curso |
| Back | No desbloquea (BackHandler / no pop al home) |
| Aviso MVP | El bloqueo aplica **dentro de esta app**; no cierra otras apps sin Device Owner (PRP-13) |

### Selección de curso (puente a PRP-07)

- Tarjetas Matemática / Comunicación / Ciencia y Tecnología.
- Tap → `student/course/{id}` (placeholder hasta evaluación real).

### Simulación (dev / QA)

- Botón debug en panel estudiante: “Simular tiempo agotado” → `setLocked(true)` para probar sin UsageStats reales.

### Flujo

```
≥ 30 min (o simular) → lock = true → pantalla bloqueo
  → Comenzar lección → course/select → curso
  → Aprobar (PRP-07/08) → lock = false
```

## Fuera de alcance (por ahora)

- Overlay sobre otras apps / Accessibility / MDM.
- Evaluación y desbloqueo por nota (PRP-07/08).
- Kill de procesos de terceros.

## Dependencias con otros PRPs

| PRP | Relación |
|-----|----------|
| 05 | Contador y `setLocked(true)` |
| 07 | Quiz real desde selección de curso |
| 08 | `setLocked(false)` al aprobar |
| 13 | Bloqueo sistema fuerte |

## Criterios de aceptación

- [x] Con lock activo aparece la pantalla con el copy del brief.
- [x] CTA lleva a selección de curso.
- [x] Back no desbloquea ni vuelve al home libremente.
- [x] Lock persiste al reiniciar la app.
- [ ] Desbloqueo al aprobar → PRP-08 (`LockRepository.setLocked(false)` listo).

## Implementación

| Área | Ubicación |
|------|-----------|
| Persistencia | `data/lock/PersistentLockRepository.kt` |
| UI bloqueo | `ui/screens/lock/LockScreen.kt` |
| Cursos | `ui/screens/lock/CourseSelectScreen.kt` |
| Nav | rutas `lock`, `course/select` |

## Notas técnicas

- Siguiente: [PRP-07](PRP-07-evaluaciones.md).
