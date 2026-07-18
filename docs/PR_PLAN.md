# Plan de Pull Requests — ControlSelf EDU

Cada PR debe ser revisable por sí solo: un módulo, UI + lógica mínima, y un camino de prueba claro.

---

## PR #1 — Proyecto base ✅ (este paso)

**Objetivo:** App que compile y muestre splash → placeholder de login.

**Incluye:**
- Gradle, `applicationId`, tema Material 3
- Colores del brief (azul, blanco, verde)
- `WelcomeScreen` + navegación a login placeholder

**Cómo probar:** Abrir en Android Studio → Run → ver splash ~2s → pantalla “Inicio de sesión”.

---

## PR #2 — Autenticación y roles

**Objetivo:** Flujo de acceso por tipo de usuario.

**Incluye:**
- Campos: usuario/correo, contraseña, “Recordar sesión”
- Botones: Iniciar sesión, Crear cuenta, ¿Olvidaste tu contraseña?
- Selector: Estudiante / Docente / Padre → ruta distinta

**Fuera de alcance:** Backend real (puede ser auth local/mock hasta definir API).

---

## PR #3 — Panel del estudiante

**Objetivo:** Dashboard post-login del estudiante.

**Incluye:**
- Tarjeta “Tiempo de hoy” (barra verde/amarillo/rojo)
- Mis cursos (Matemática, Comunicación, Ciencia y Tecnología)
- Mi racha + insignias placeholder
- Mi progreso (placeholders de gráficos)

---

## PR #4 — Monitoreo de uso

**Objetivo:** Contar minutos en apps de entretenimiento.

**Incluye:**
- Permiso `PACKAGE_USAGE_STATS` / UsageStatsManager
- Lista configurable de apps (redes + juegos)
- Persistencia del contador diario (límite 30 min)

---

## PR #5 — Bloqueo automático

**Objetivo:** Pantalla a pantalla completa al agotar 30 min.

**Incluye:**
- Overlay / Activity de bloqueo
- Copy: “¡Tiempo cumplido!” + botón “Comenzar lección”
- Hook al flujo de evaluación (PR #6)

---

## PR #6 — Evaluaciones

**Objetivo:** Completar lección para desbloquear.

**Incluye:**
- Selección de curso (tarjetas con ícono y dificultad)
- 20 preguntas (opción múltiple + verdadero/falso)
- Umbral: 15/20 correctas → desbloqueo

---

## PR #7 — Resultados y feedback

**Objetivo:** Refuerzo de aprendizaje si no aprueba.

**Incluye:**
- Éxito (≥15): “Aplicaciones desbloqueadas” + Ver resultados / Inicio
- Fallo (<15): respuestas correctas, errores, botón Reintentar

---

## PR #8 — Motivación

**Incluye:** Rachas diarias, logros, medallas por rendimiento / uso responsable.

---

## PR #9 — Estadísticas

**Incluye:** Tiempo de uso/estudio, evaluaciones, promedio, cursos, logros, rachas + gráficos.

---

## PR #10 — Panel docente

**Incluye:** Métricas del salón, crear/editar evaluaciones, banco de preguntas, lista de estudiantes, reportes.

---

## PR #11 — Panel padre

**Incluye:** Tiempo en pantalla, cursos, promedio, logros, racha, evolución semanal.

---

## PR #12 — Anti-desinstalación

**Incluye:** Device Admin (o equivalente) + contraseña solo conocida por docente/padre.

> Nota: en Android moderno el bloqueo total de desinstalación tiene límites del sistema; se documentará el enfoque viable por versión de API.

---

## Convención de ramas

```
feat/<nombre-corto>   → PR hacia main
fix/<nombre-corto>
```

Mensajes de commit: imperativo corto en español o inglés, un tema por commit.
