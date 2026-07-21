# PRP-03 — Autenticación y roles

## Objetivo

Permitir acceso con usuario/correo y contraseña, recordar sesión, registrar solo roles autónomos (Padre, Docente) y dirigir a cada rol a su panel. Las cuentas de estudiante las crea el padre (PRP-12), no el registro público.

## Alcance

### Login (brief)

| Elemento | Spec |
|----------|------|
| Usuario o correo | Obligatorio |
| Contraseña | Oculta + toggle mostrar |
| Recordar sesión | Switch → DataStore; al reabrir salta welcome/login |
| Iniciar sesión | CTA primario |
| Crear cuenta | → `register` (solo Padre / Docente) |
| ¿Olvidaste tu contraseña? | → `forgot-password` (mock: mensaje de éxito, sin email real) |
| Roles abajo | **Estudiante / Docente / Padre** (selección obligatoria al entrar) |

El login sí admite rol **Estudiante**: el hijo entra con las credenciales que le asignó el padre.

### Registro público

- Nombre, usuario/correo, contraseña (≥ 6), rol.
- Roles permitidos en registro: **solo Padre y Docente**.
- **No** se puede elegir Estudiante al crear cuenta.
- Tras éxito → sesión iniciada y navegación al home del rol.

### Alta de estudiante (jerarquía)

- El estudiante **no** se auto-registra.
- API: `AuthRepository.createChildAccount` + `listChildren` (UI de alta multi-hijo en [PRP-12](PRP-12-panel-padre.md)).
- Relación: un padre → **N** estudiantes (vínculos en DataStore).

### Auth local (MVP)

- Usuarios + sesión + vínculos padre→hijo en **DataStore** (sin backend).
- Contraseñas en claro solo en mock local (documentado; no producción).
- Cuentas demo seed al primer arranque (ver notas); el seed respeta la jerarquía.
- Migración: installs ya seeded sin vínculos reciben `padre` → `estudiante`.

### Guard de navegación

- Con sesión recordada → `Routes.homeFor(role)` (sin welcome).
- Sin sesión → `welcome` → `login`.
- Logout en panel limpia sesión → `login`.

## Fuera de alcance (por ahora)

- OAuth, backend, verificación email, 2FA.
- Vinculación docente↔salón institucional (IDs mock en panel docente).
- Contraseña admin anti-desinstalación → PRP-13 (distinta de la de login).
- UI completa de crear/listar/seleccionar hijos en panel padre → PRP-12.

## Dependencias con otros PRPs

| PRP | Relación |
|-----|----------|
| 02 | Sustituye placeholder de login |
| 01 | Rutas y `AppContainer` |
| 00 | Jerarquía padre → hijo; docente autónomo |
| 04, 11, 12 | Homes reales; 12 UI alta de estudiantes |
| 13 | Password admin ≠ sesión |

## Criterios de aceptación

- [x] UI login con campos y botones del brief.
- [x] Selector de rol en login (incluye Estudiante para entrar).
- [x] Registro público **sin** rol Estudiante (solo Padre / Docente).
- [x] Crear cuenta Padre o Docente y luego entrar (o auto-login post-registro).
- [x] No existe flujo de auto-registro de estudiante (`register` rechaza `STUDENT`).
- [x] Recordar sesión restaura al reabrir.
- [x] Cada rol navega a su panel.
- [x] Forgot-password muestra flujo mock sin servidor.
- [x] Seed demo: `estudiante` creado bajo `padre` (vínculo local + migración).
- [x] API `createChildAccount` / `listChildren` disponible para PRP-12.

## Implementación

| Área | Ubicación |
|------|-----------|
| Contrato | `domain/repository/AuthRepository.kt` |
| Modelo hijo | `domain/model/ChildAccount.kt` |
| DataStore | `data/auth/LocalAuthRepository.kt` |
| UI | `ui/screens/login/*`, `register/*`, `forgot/*` |
| Roles UI | `ui/screens/auth/RoleSelector.kt` (`RegisterAllowedRoles` / `LoginAllowedRoles`) |
| Nav | `ControlSelfNavHost` + restore de sesión |

## Notas técnicas

**Cuentas demo (seed — misma lógica de producto):**

| Usuario | Contraseña | Rol | Relación |
|---------|------------|-----|----------|
| padre | 123456 | Padre | Dueño de los hijos demo |
| estudiante | 123456 | Estudiante | Creado por `padre` (vínculo local) |
| docente | 123456 | Docente | Independiente (aporte comunitario) |

- Rutas: `login`, `register`, `forgot-password`
- Alta de hijos UI: [PRP-12](PRP-12-panel-padre.md)
