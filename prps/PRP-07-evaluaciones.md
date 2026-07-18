# PRP-07 — Evaluaciones

## Objetivo

Completar una lección de **20 preguntas** (opción múltiple y V/F) por curso, calcular el score y guardar el intento. Umbral de desbloqueo: **≥ 15/20** (UI de resultado enriquecida en PRP-08).

## Alcance

### Entrada

- Selección de curso ya existe (PRP-04/06): `course/select` y cursos del home.
- Tap en curso → intro breve → **Comenzar evaluación** → `quiz/{courseId}/play`.

### Lección

| Campo | Spec |
|-------|------|
| Preguntas | Exactamente 20 |
| Tipos | Opción múltiple (3–4) + Verdadero/Falso |
| UI | Una pregunta a la vez · “Pregunta k de 20” |
| Salida | Diálogo de confirmación si intenta salir |

### Banco MVP

- Contenido embebido en código por curso (`QuizBank`).
- CRUD docente → PRP-11.

### Calificación y persistencia

- Score = correctas / 20.
- `QuizAttempt` en memoria (lista reciente) para feedback PRP-08.
- Si ≥ 15 → `LockRepository.setLocked(false)` al terminar.
- Si &lt; 15 → lock se mantiene; reintento de la misma lección.

### Resultado (mínimo en este PRP)

- Pantalla score + aprobar/reprobar + Reintentar / Ir al inicio.
- Detalle “Ver resultados” y copy exacto del brief → se pulen en [PRP-08](PRP-08-resultados-feedback.md).

## Fuera de alcance

- IA, multimedia, timer por pregunta, adaptive learning.

## Criterios de aceptación

- [x] Tres cursos con ícono/dificultad llevan al quiz.
- [x] 20 preguntas mixtas MC / V-F.
- [x] Score correcto.
- [x] Intento guardado y desbloqueo si ≥ 15.

## Implementación

| Área | Ubicación |
|------|-----------|
| Modelos | `domain/model/quiz/*` |
| Banco | `data/quiz/QuizBank.kt` |
| Repo | `QuizAttemptRepository` |
| UI | `ui/screens/quiz/*` |
| Rutas | `quiz/{courseId}/play`, `quiz/result/{attemptId}` |

## Notas técnicas

- Siguiente: [PRP-08](PRP-08-resultados-feedback.md) (feedback detallado / copy brief).
