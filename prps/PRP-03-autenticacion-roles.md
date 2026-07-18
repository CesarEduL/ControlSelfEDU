# PRP-03 — Autenticación y roles

## Objetivo

Permitir acceso con usuario/correo y contraseña, recordar sesión, registrar cuenta y dirigir a cada rol (Estudiante, Docente, Padre) a su panel (stub hasta PRP-04/11/12).

## Alcance

### Login (brief)

| Elemento | Spec |
|----------|------|
| Usuario o correo | Obligatorio |
| Contraseña | Oculta + toggle mostrar |
| Recordar sesión | Switch → DataStore; al reabrir salta welcome/login |
| Iniciar sesión | CTA primario |
| Crear cuenta | → `register` |
| ¿Olvidaste tu contraseña? | → `forgot-password` (mock: mensaje de éxito, sin email real) |
| Roles abajo | Estudiante / Docente / Padre de familia (selección obligatoria) |

### Registro

- Nombre, usuario/correo, contraseña (≥ 6), rol.
- Tras éxito → sesión iniciada y navegación al home del rol.

### Auth local (MVP)

- Usuarios + sesión en **DataStore** (sin backend).
- Contraseñas en claro solo en mock local (documentado; no producción).
- Cuentas demo seed al primer arranque (ver notas).

### Guard de navegación

- Con sesión recordada → `Routes.homeFor(role)` (sin welcome).
- Sin sesión → `welcome` → `login`.
- Logout en stub de panel limpia sesión → `login`.

## Fuera de alcance (por ahora)

- OAuth, backend, verificación email, 2FA.
- Vinculación padre↔hijo / docente↔salón (IDs mock en fases posteriores).
- Contraseña admin anti-desinstalación → PRP-13 (distinta de la de login).

## Dependencias con otros PRPs

| PRP | Relación |
|-----|----------|
| 02 | Sustituye placeholder de login |
| 01 | Rutas y `AppContainer` |
| 04, 11, 12 | Homes reales |
| 13 | Password admin ≠ sesión |

## Criterios de aceptación

- [x] UI login con campos y botones del brief.
- [x] Selector de rol visible y funcional.
- [x] Recordar sesión restaura al reabrir.
- [x] Crear cuenta y luego entrar (o auto-login post-registro).
- [x] Cada rol navega a su stub de panel.
- [x] Forgot-password muestra flujo mock sin servidor.

## Implementación

| Área | Ubicación |
|------|-----------|
| Contrato | `domain/repository/AuthRepository.kt` |
| DataStore | `data/auth/LocalAuthRepository.kt` |
| UI | `ui/screens/login/*`, `register/*`, `forgot/*` |
| Nav | `ControlSelfNavHost` + restore de sesión |

## Notas técnicas

**Cuentas demo (seed):**

| Usuario | Contraseña | Rol |
|---------|------------|-----|
| estudiante | 123456 | Estudiante |
| docente | 123456 | Docente |
| padre | 123456 | Padre |

- Rutas: `login`, `register`, `forgot-password`
- Siguiente: [PRP-04](PRP-04-panel-estudiante.md)
