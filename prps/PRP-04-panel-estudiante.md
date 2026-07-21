# PRP-04 — Panel del estudiante

## Objetivo

Menú principal del estudiante tras el login: tiempo en pantalla, cursos, racha e progreso (datos mock hasta PRP-05/09/10; lecciones reales en PRP-07). La cuenta del estudiante la crea el padre ([PRP-03](PRP-03-autenticacion-roles.md) / [PRP-12](PRP-12-panel-padre.md)); aquí solo se usa tras iniciar sesión.

## Alcance

### Identidad

- El estudiante **no** crea su cuenta desde este panel ni desde el registro público.
- Entra con usuario/contraseña definidos por su padre; el saludo usa el `displayName` de esa cuenta.

### Encabezado

- **Bienvenido a ControlSelf EDU** + saludo con `{nombre}`.
- Acción: Cerrar sesión.

### Tiempo en pantalla

| Elemento | Spec |
|----------|------|
| Título | Tiempo de hoy |
| Valor | `{min} minutos de 30 disponibles` |
| Barra | 0–30 |
| Color | Verde &lt; 20 · Amarillo 20–29 · Rojo ≥ 30 |

Fuente: `ScreenTimeRepository` (mock PRP-05 con valor demo **18**).

### Mis cursos

Tarjetas grandes navegables:

| Curso | Color | id |
|-------|-------|----|
| Matemática | Azul | `math` |
| Comunicación | Verde | `comms` |
| Ciencia y Tecnología | Teal | `science` |

Tap → pantalla puente `student/course/{id}` (“Lección en PRP-07”) hasta existir el quiz.

### Mi racha

- Mock: **12 días** seguidos (copy del brief).
- Insignias placeholder (PRP-09): Principiante, Constante, Estudiante destacado, Maestro del aprendizaje (estado visual locked/unlocked demo).

### Mi progreso

Filas/métricas simples (no charts Vico — PRP-10):

- Promedio de notas, lecciones completadas, tiempo estudiado, tiempo en redes, curso favorito (valores mock).

## Fuera de alcance (por ahora)

- UsageStats real (PRP-05), evaluación (PRP-07), logros reales (PRP-09), gráficos (PRP-10).
- Perfil editable, push.
- Auto-registro del estudiante (prohibido por producto; ver PRP-03 / PRP-12).

## Dependencias con otros PRPs

| PRP | Relación |
|-----|----------|
| 03 | Login estudiante (cuenta creada por padre) |
| 12 | Alta y vínculo padre → este estudiante |
| 05 | Minutos reales |
| 07 | Destino real de cursos |
| 09–10 | Racha/logros/gráficos reales |

## Criterios de aceptación

- [x] Cuatro secciones del brief visibles.
- [x] Barra de tiempo cambia de color según umbrales.
- [x] Tres cursos navegan a un destino (puente hasta PRP-07).
- [x] Scroll vertical usable en móvil + logout.

## Implementación

| Área | Ubicación |
|------|-----------|
| Home | `ui/screens/student/StudentHomeScreen.kt` |
| Cards | `ui/screens/student/components/*` |
| Curso puente | `CoursePlaceholderScreen` |
| Ruta | `student/home`, `student/course/{courseId}` |

## Notas técnicas

- Demo: login `estudiante` / `123456` (seed: hijo de `padre`, no auto-registrado).
- Siguiente: [PRP-05](PRP-05-monitoreo-uso.md) o [PRP-07](PRP-07-evaluaciones.md) según prioridad de bloqueo.
