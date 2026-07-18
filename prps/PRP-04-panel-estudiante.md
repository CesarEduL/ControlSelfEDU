# PRP-04 — Panel del estudiante

## Objetivo

Definir el menú principal del estudiante tras el login: resumen de tiempo, cursos, racha e progreso.

## Alcance

### Encabezado

- Texto: **Bienvenido a ControlSelf EDU** (o “Bienvenido, {nombre}”).

### Tarjeta — Tiempo en pantalla

| Elemento | Spec |
|----------|------|
| Título | Tiempo de hoy |
| Valor | `{minutos} minutos de 30 disponibles` |
| Barra de progreso | 0–30 min |
| Color barra | Verde = disponible · Amarillo = cerca del límite · Rojo = agotado |

Datos reales vienen de PRP-05; hasta entonces usar mock.

### Mis cursos

Tres botones/tarjetas grandes:

| Curso | Color distintivo (sugerido) |
|-------|----------------------------|
| Matemática | Azul |
| Comunicación | Verde |
| Ciencia y Tecnología | Teal / acento |

Al tocar → flujo de lección (PRP-07), o desde bloqueo (PRP-06).

### Mi racha

- Racha actual: “N días seguidos aprendiendo”.
- Insignias (placeholder hasta PRP-09): Principiante, Constante, Estudiante destacado, Maestro del aprendizaje.

### Mi progreso

Placeholders o gráficos simples (completar en PRP-10):

- Promedio de notas
- Lecciones completadas
- Tiempo estudiado
- Tiempo en redes
- Cursos favoritos

## Fuera de alcance (por ahora)

- Edición de perfil avanzada.
- Notificaciones push.
- Gráficos definitivos (PRP-10).

## Dependencias con otros PRPs

| PRP | Relación |
|-----|----------|
| 03 | Login estudiante |
| 05 | Minutos reales |
| 06–08 | Flujo de bloqueo y lección |
| 09–10 | Rachas, logros y gráficos |

## Criterios de aceptación

- [ ] Dashboard muestra las cuatro secciones del brief.
- [ ] Barra de tiempo cambia de color según umbrales.
- [ ] Los tres cursos son navegables.
- [ ] Layout usable en móvil (scroll vertical, sin clutter excesivo).

## Notas técnicas

- Ruta: `student/home`
- Componentes sugeridos: `ScreenTimeCard`, `CoursesRow`, `StreakCard`, `ProgressSection`
