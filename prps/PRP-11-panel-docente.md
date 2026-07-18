# PRP-11 — Panel del docente

## Objetivo

Dar a los profesores un panel exclusivo para crear y editar evaluaciones, administrar el banco de preguntas, revisar resultados, ver estadísticas e identificar temas difíciles.

## Alcance

### Home docente

| Widget / acción | Spec |
|-----------------|------|
| Número de estudiantes | Conteo del salón (mock o lista local) |
| Promedio del salón | Media de calificaciones |
| Crear evaluación | CTA → editor |
| Administrar banco de preguntas | Lista CRUD |
| Estadísticas | Resumen + temas con más error |
| Descargar reportes | Export simple (CSV/JSON MVP) |

### Lista de estudiantes

- Nombre, últimas calificaciones, estado (activo / en bloqueo si aplica).
- Drill-down a historial de intentos.

### Crear / editar evaluación

- Curso, título, dificultad.
- Agregar hasta N preguntas (lección estándar = 20).
- Tipos: opción múltiple, verdadero/falso.
- Publicar / guardar borrador.

### Banco de preguntas

- CRUD, filtro por curso/tema.
- Reutilizar preguntas en varias evaluaciones.

### Temas con mayor dificultad

- Ranking de preguntas o tags con menor % de acierto.

## Fuera de alcance (por ahora)

- LMS completo, rúbricas, calificaciones parciales.
- Multisalón institucional con SSO.

## Dependencias con otros PRPs

| PRP | Relación |
|-----|----------|
| 03 | Rol docente |
| 07–08 | Consumo de evaluaciones por estudiantes |
| 10 | Gráficos del salón |
| 13 | Puede configurar contraseña admin institucional |

## Criterios de aceptación

- [ ] Home con métricas y CTAs del brief.
- [ ] Crear evaluación con 20 preguntas y publicarla.
- [ ] Editar / eliminar preguntas del banco.
- [ ] Ver lista de estudiantes con resultados.
- [ ] Identificar al menos un “tema difícil” a partir de errores.
- [ ] Export de reporte básico.

## Notas técnicas

- Rutas: `teacher/home`, `teacher/quizzes`, `teacher/questions`, `teacher/students`, `teacher/reports`
- Misma fuente de verdad Room que el estudiante lee en modo local
