# PRP-00 — Visión y alcance

## Objetivo

Definir la visión de **ControlSelf EDU** como app Android de control parental educativo: limitar el tiempo en redes sociales y videojuegos, y desbloquear ese tiempo solo tras aprobar una lección académica.

## Alcance

### Visión del producto

La app registra el uso de aplicaciones de entretenimiento. Tras **30 minutos** continuos (o acumulados del día, según decisión en PRP-05), bloquea esas apps. Para continuar, el estudiante debe:

1. Elegir un curso: Matemática, Comunicación, o Ciencia y Tecnología.
2. Completar una evaluación de **20 preguntas** (opción múltiple y verdadero/falso).
3. Obtener al menos **15/20** correctas.

Si no alcanza el umbral, ve feedback con respuestas correctas y debe reintentar la misma evaluación.

### Usuarios y paneles

| Rol | Acceso principal | Alta de cuenta |
|-----|------------------|----------------|
| **Padre/madre** | Tiempo en pantalla, estudio, calificaciones, logros, evolución; **crea y gestiona cuentas de sus hijos** | Auto-registro |
| **Docente** | Banco de preguntas / ejercicios (aporte comunitario), resultados, estadísticas | Auto-registro (ente separado; no crea estudiantes) |
| **Estudiante** | Tiempo, cursos, lecciones, racha, progreso, desbloqueo | **Solo el padre** define usuario/contraseña; el hijo no se auto-registra |

#### Jerarquía de cuentas

```
Padre (1) ──crea──► Estudiante (N)
Docente     ──registro propio──► aporta ejercicios a la comunidad
```

- Un padre puede crear **varios** estudiantes; cada uno con credenciales propias definidas por el padre.
- El estudiante solo inicia sesión; no aparece como opción de “Crear cuenta” en el registro público.
- El docente no forma parte de la cadena padre↔hijo.

### Identidad visual (brief)

- Colores: **azul, blanco y verde** (tecnología + educación).
- Pantalla de bienvenida con logo → login.
- UI sencilla e intuitiva.

### Sistema de motivación

Rachas diarias, logros por evaluaciones, reconocimientos por uso responsable, medallas por rendimiento.

### Estadísticas

Tiempo de uso, tiempo de estudio, evaluaciones, promedio, cursos, logros, rachas — presentados con gráficos legibles.

### Protección

Impedir desinstalación sin contraseña de administrador (docente o padre). Ver límites de Android en PRP-13.

### Fases

| Fase | Contenido |
|------|-----------|
| **0** | Scaffold + tema + splash (PRP-01, 02) |
| **1** | Auth (Padre/Docente se registran; padre crea hijos) + panel estudiante + mock de tiempo/cursos |
| **2** | Monitoreo real + bloqueo + evaluaciones + resultados |
| **3** | Motivación + estadísticas |
| **4** | Paneles docente (aporte comunitario) y padre (multi-hijo) |
| **5** | Anti-desinstalación + endurecimiento |

## Fuera de alcance (por ahora)

- Backend en la nube obligatorio (MVP puede ser local/mock).
- iOS / web.
- Bloqueo a nivel de sistema de *todas* las apps sin permisos especiales (Usage Access + overlay; Device Owner es fase avanzada).
- Contenido curricular oficial certificado por ministerio (banco de preguntas propio o editable por docente).

## Dependencias con otros PRPs

Todos los PRPs de este índice dependen de esta visión. Orden: ver [README.md](README.md).

## Criterios de aceptación

- [x] Visión, roles y umbrales (30 min, 15/20) documentados.
- [x] Jerarquía de cuentas: padre crea estudiantes; docente y padre se auto-registran.
- [x] Fases de producto claras.
- [x] Fuera de alcance explícito.

## Notas técnicas

- Package: `com.controlself.edu`
- Plataforma: Android (Kotlin + Jetpack Compose)
- Spec de producto origen: PDF *ControlSelf EDU*
