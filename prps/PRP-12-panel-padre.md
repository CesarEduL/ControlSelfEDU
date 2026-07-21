# PRP-12 — Panel del padre

## Objetivo

Permitir al padre/madre **crear y gestionar cuentas de sus hijos** y visualizar el progreso de cada uno: tiempo en pantalla, estudio, calificaciones, cursos, rachas, logros y evolución semanal.

## Alcance

### Jerarquía (producto)

- Solo el padre crea cuentas de estudiante (usuario + contraseña que él define).
- Un padre → **N** estudiantes.
- El registro público no ofrece rol Estudiante ([PRP-03](PRP-03-autenticacion-roles.md)).

### Alta / gestión de hijos

| Acción | Spec |
|--------|------|
| Crear hijo | Formulario: nombre, usuario/correo, contraseña (≥ 6) → cuenta `STUDENT` ligada al padre en sesión |
| Listar hijos | Lista de estudiantes creados por este padre |
| Seleccionar hijo activo | El dashboard (bloques abajo) muestra datos del hijo seleccionado |
| Credenciales | El padre puede recordar/mostrar al crear; el hijo usa esas credenciales en login |

### Home padre (hijo activo)

| Bloque | Spec |
|--------|------|
| Hijo activo | Nombre + identificador / código de vínculo local |
| Tiempo en pantalla | Minutos de entretenimiento hoy + suma 7 días |
| Cursos desarrollados | Chips de cursos con intentos / aprobaciones |
| Promedio de calificaciones | Media de scores (PRP-09/10) |
| Logros obtenidos | Insignias desbloqueadas (PRP-09) |
| Racha de estudio | Actual y máxima |
| Evolución semanal | Gráfico 7 días: estudio vs redes |
| Intentos recientes | Lista solo lectura → detalle |

### Vinculación (MVP local)

- Vínculo en DataStore: `parentUsername` → lista de usernames hijo (sin backend).
- Seed: `padre` ya tiene a `estudiante` como hijo ([PRP-03](PRP-03-autenticacion-roles.md)).
- Código de vínculo mock opcional por hijo (p. ej. derivado del username) para UI.

### Acciones

- Ver detalle de un intento (resumen; respuestas si el intento sigue en memoria).
- Aviso de protección admin (PRP-13) — **solo en panel padre**, nunca en pantallas del estudiante.

## Fuera de alcance (por ahora)

- Chat padre–docente.
- Controles remotos en tiempo real.
- Edición remota de contraseña del hijo tras el alta (fase posterior; el alta inicial sí define credenciales).
- Sincronización multi-dispositivo / nube.

## Dependencias con otros PRPs

| PRP | Relación |
|-----|----------|
| 00 | Jerarquía padre → N hijos |
| 03 | Auth, registro restringido, seed jerárquico, API alta de hijo |
| 05, 08–10 | Datos mostrados del hijo activo |
| 13 | Contraseña anti-desinstalación |

## Criterios de aceptación

- [ ] Padre autenticado puede crear **varios** estudiantes con credenciales propias.
- [ ] Las cuentas hijas aparecen en su lista y se pueden seleccionar como hijo activo.
- [ ] Un estudiante creado por el padre puede hacer login (rol Estudiante) con esas credenciales.
- [x] Panel muestra los bloques del brief para el hijo activo.
- [x] Evolución semanal visible (estudio desde intentos; redes desde historial diario).
- [x] Solo lectura sobre evaluaciones (sin editar banco).
- [x] No expone la contraseña admin en pantallas del estudiante.
- [x] Seed: `padre` → `estudiante` vinculados en auth (API); UI multi-hijo pendiente.

## Implementación

| Área | Ubicación |
|------|-----------|
| Agregador | `domain/parent/ParentDashboardAggregator.kt` |
| Historial 7 días | `UsageScreenTimeRepository` + `observeWeeklyEntertainmentMinutes` |
| UI | `ui/screens/parent/ParentHomeScreen.kt` (+ UI crear/listar hijos) |
| Detalle intento | `ParentAttemptDetailScreen` |
| Auth / vínculo | `AuthRepository` / `LocalAuthRepository` (`createChildAccount`, lista por padre) |
| Rutas | `parent/home`, `parent/attempt/{attemptId}` (y ruta/form de alta si aplica) |

## Notas técnicas

- Demo: login `padre` / `123456`; hijo demo `estudiante` / `123456`.
- Hasta implementar multi-hijo, el dashboard puede seguir leyendo datos locales del dispositivo; el modelo de cuentas debe anticipar N hijos.
- Protección anti-desinstalación: [PRP-13](PRP-13-anti-desinstalacion.md).
