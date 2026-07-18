# PRP-06 — Bloqueo automático

## Objetivo

Cuando el estudiante agota los 30 minutos de entretenimiento, mostrar una pantalla a pantalla completa que impida seguir y ofrezca comenzar una lección.

## Alcance

### Trigger

- `ScreenTimeRepository` alcanza ≥ 30 minutos (PRP-05).
- Estado global `isEntertainmentLocked = true` hasta desbloqueo por evaluación aprobada (PRP-07/08).

### Pantalla de bloqueo

| Elemento | Copy / UI |
|----------|-----------|
| Título | ¡Tiempo cumplido! |
| Cuerpo | Has utilizado tus 30 minutos de entretenimiento. Para seguir usando tus aplicaciones necesitas completar una lección. |
| CTA | **Comenzar lección** (botón grande) |

Diseño atractivo, marca azul/verde, sin escape fácil (back no cierra el bloqueo).

### Comportamiento del sistema (MVP realista)

Android no permite bloquear otras apps sin privilegios fuertes. Enfoque por capas:

1. **MVP:** Overlay / Activity de bloqueo de *ControlSelf EDU* + recordatorio; al abrir la app siempre cae en bloqueo si `isEntertainmentLocked`.
2. **Mejora:** Accessibility / overlay sobre otras apps (con permiso; políticas de Play a revisar).
3. **Avanzado:** Device Owner / MDM (PRP-13, entornos escolares).

Documentar en la UI qué puede y no puede hacer el MVP.

### Flujo

```
Tiempo ≥ 30 → lock = true → pantalla bloqueo
  → Comenzar lección → selección de curso (PRP-07)
  → Aprobar (≥15) → lock = false (PRP-08)
```

## Fuera de alcance (por ahora)

- Bloqueo total tipo MDM sin Device Owner.
- Kill automático de procesos de otras apps.

## Dependencias con otros PRPs

| PRP | Relación |
|-----|----------|
| 05 | Contador |
| 07–08 | Desbloqueo vía lección |
| 13 | Relacionado con control parental fuerte |

## Criterios de aceptación

- [ ] Al simular 30 min aparece la pantalla de bloqueo con el copy del brief.
- [ ] CTA navega a selección de curso.
- [ ] Back no desbloquea.
- [ ] Tras aprobar evaluación, el bloqueo se levanta.

## Notas técnicas

- `LockActivity` o destination Compose `lock/overlay` con `launchSingleTop`
- Flag en DataStore: `entertainment_locked`, `unlocked_until` (opcional grace period)
