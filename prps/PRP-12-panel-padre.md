# PRP-12 — Panel del padre

## Objetivo

Permitir al padre/madre visualizar el progreso del hijo: tiempo en pantalla, estudio, calificaciones, cursos, rachas, logros y evolución semanal.

## Alcance

### Home padre

| Bloque | Spec |
|--------|------|
| Tiempo en pantalla | Minutos de entretenimiento hoy / semana (PRP-05) |
| Cursos desarrollados | Lista o chips de cursos con lecciones hechas |
| Promedio de calificaciones | Media de scores |
| Logros obtenidos | Insignias (PRP-09) |
| Racha de estudio | Valor actual y máxima |
| Evolución semanal | Gráfico 7 días: estudio vs redes / notas |

### Vinculación hijo (MVP)

- Selección de estudiante vinculado por código/ID mock.
- Un padre → uno o varios hijos (MVP: uno).

### Acciones

- Ver detalle de un intento (solo lectura).
- Configurar / conocer contraseña de administrador (PRP-13) — no visible al estudiante.

## Fuera de alcance (por ahora)

- Chat padre–docente.
- Controles remotos en tiempo real tipo “pausar internet” del ISP.

## Dependencias con otros PRPs

| PRP | Relación |
|-----|----------|
| 03 | Rol padre |
| 05, 08–10 | Datos mostrados |
| 13 | Contraseña anti-desinstalación |

## Criterios de aceptación

- [ ] Panel muestra todos los bloques del brief.
- [ ] Evolución semanal visible con datos mock o reales.
- [ ] Solo lectura sobre evaluaciones (sin editar banco).
- [ ] No expone la contraseña admin en pantallas del estudiante.

## Notas técnicas

- Ruta: `parent/home`, `parent/child/{id}`
- Reutilizar `StatsAggregator` (PRP-10) filtrado por `studentId`
