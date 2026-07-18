# PRP-11 — Panel del docente

## Objetivo

Dar a los profesores un panel exclusivo para administrar el banco de preguntas por curso, revisar resultados del salón, ver estadísticas, identificar temas difíciles y exportar un reporte básico.

## Alcance

### Home docente

| Widget / acción | Spec |
|-----------------|------|
| Número de estudiantes | Conteo del salón (lista local / mock + demo) |
| Promedio del salón | Media de calificaciones disponibles |
| Administrar banco | CTA → banco por curso |
| Estadísticas / temas difíciles | Ranking por % de error |
| Ver estudiantes | Lista con últimas notas |
| Descargar reportes | Export CSV compartible (MVP) |

### Lista de estudiantes

- Nombre, últimas calificaciones, estado (activo / en bloqueo si aplica).
- Drill-down a resumen de intentos (solo lectura).

### Banco de preguntas (evaluación = banco del curso)

En este MVP la “evaluación” publicada es el banco de **20 preguntas** por curso que ya consume el estudiante (PRP-07).

- Editar / agregar / eliminar preguntas (MC y V/F).
- Al publicar / guardar, el curso debe quedar con exactamente 20 preguntas.
- Semilla inicial: `QuizBank` embebido; overrides en DataStore.

### Temas con mayor dificultad

- Ranking de preguntas con menor % de acierto (agregado desde intentos reales).

## Fuera de alcance (por ahora)

- LMS completo, rúbricas, calificaciones parciales.
- Multisalón institucional con SSO.
- Editor de “nueva evaluación” independiente del banco del curso (fase posterior).
- Room / backend remoto.

## Dependencias con otros PRPs

| PRP | Relación |
|-----|----------|
| 03 | Rol docente (`docente` / `123456`) |
| 07–08 | Estudiante lee el mismo banco |
| 10 | Historial de intentos / promedios |
| 13 | Contraseña admin institucional |

## Criterios de aceptación

- [x] Home con métricas y CTAs del brief.
- [x] Editar banco de un curso y que el estudiante reciba las preguntas actualizadas.
- [x] Agregar / eliminar preguntas (publicar con 20).
- [x] Ver lista de estudiantes con resultados.
- [x] Identificar al menos un “tema difícil” a partir de errores.
- [x] Export de reporte básico (CSV vía share).

## Implementación

| Área | Ubicación |
|------|-----------|
| Banco editable | `EditableQuizRepository` |
| Analytics errores | `PersistentQuestionAnalyticsRepository` |
| Salón | `LocalClassroomRepository` |
| UI | `ui/screens/teacher/*` |
| Rutas | `teacher/home`, `teacher/bank`, `teacher/students`, `teacher/stats`, `teacher/reports` |

## Notas técnicas

- Persistencia: DataStore (mismo patrón que PRP-03/09/10), no Room.
- Demo: login `docente` / `123456`.
- Siguiente: [PRP-12](PRP-12-panel-padre.md).
