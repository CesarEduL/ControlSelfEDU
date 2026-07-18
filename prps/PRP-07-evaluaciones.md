# PRP-07 — Evaluaciones

## Objetivo

Permitir al estudiante elegir un curso y completar una lección de **20 preguntas** (opción múltiple y verdadero/falso) para intentar desbloquear el entretenimiento.

## Alcance

### Selección de curso

Tarjetas:

| Curso | Muestra |
|-------|---------|
| Matemática | Ícono + nivel de dificultad |
| Comunicación | Ícono + nivel de dificultad |
| Ciencia y Tecnología | Ícono + nivel de dificultad |

Entrada desde panel estudiante o desde “Comenzar lección” (PRP-06).

### Estructura de una lección / evaluación

| Campo | Spec |
|-------|------|
| Preguntas | Exactamente 20 |
| Tipos | Opción múltiple (3–4 opciones) y Verdadero/Falso |
| Orden | Secuencial; una pregunta visible a la vez (recomendado MVP) |
| Progreso | Indicador “Pregunta k de 20” |

### Banco de preguntas (MVP)

- JSON/assets embebidos por curso **o** Room seed.
- Docente edita/crea en PRP-11; hasta entonces contenido fijo de calidad razonable.

### Calificación (lógica)

- Contar correctas al finalizar.
- Umbral de desbloqueo: **≥ 15 / 20**.
- Persistencia del intento: curso, score, fecha, detalle de respuestas (para PRP-08).

### UI durante la evaluación

- Enunciado claro, opciones tappable.
- Botón Siguiente / Finalizar en la última.
- Evitar salir accidentalmente (diálogo de confirmación).

## Fuera de alcance (por ahora)

- Preguntas abiertas, multimedia pesada, temporizador por pregunta.
- Generación con IA.
- Adaptive learning avanzado.

## Dependencias con otros PRPs

| PRP | Relación |
|-----|----------|
| 04, 06 | Entradas al flujo |
| 08 | Pantallas de resultado |
| 11 | CRUD de evaluaciones (docente) |

## Criterios de aceptación

- [ ] Tres cursos seleccionables con ícono y dificultad.
- [ ] Flujo de 20 preguntas mixtas MC / V-F.
- [ ] Score calculado correctamente.
- [ ] Intento guardado para feedback y estadísticas.

## Notas técnicas

- Modelos: `Question`, `Choice`, `Quiz`, `QuizAttempt`
- Rutas: `quiz/select`, `quiz/{courseId}/play`
