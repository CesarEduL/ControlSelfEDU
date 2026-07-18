# PRP-03 — Autenticación y roles

## Objetivo

Permitir acceso a la app con usuario/correo y contraseña, recordar sesión, y dirigir a cada tipo de usuario (Estudiante, Docente, Padre) a su panel.

## Alcance

### Pantalla de inicio de sesión

| Elemento | Spec |
|----------|------|
| Campo usuario o correo | Texto, validación básica no vacío |
| Campo contraseña | Oculto, toggle mostrar/ocultar |
| Recordar sesión | Switch / checkbox → DataStore |
| Iniciar sesión | CTA primario |
| Crear cuenta | Navega a registro |
| ¿Olvidaste tu contraseña? | Flujo mínimo (mensaje o reset local/mock) |

### Selector de tipo de usuario

En login y/o registro (según UX):

- Estudiante
- Docente
- Padre de familia

Cada rol abre un grafo de navegación distinto (PRP-04, 11, 12).

### Registro

- Datos mínimos: nombre, correo/usuario, contraseña, rol.
- Validación de contraseña (mín. longitud).
- Tras registro exitoso → login automático o pantalla de login.

### Auth mock (MVP)

- Repositorio local (DataStore / Room) sin servidor.
- Usuarios de prueba documentados en notas.
- Sesión: token/local flag + rol + `userId`.

## Fuera de alcance (por ahora)

- OAuth Google/Apple.
- Backend real, verificación de email, 2FA.
- Vinculación padre↔hijo y docente↔salón (puede ser mock IDs hasta fase 4).

## Dependencias con otros PRPs

| PRP | Relación |
|-----|----------|
| 02 | Reemplaza placeholder de login |
| 04, 11, 12 | Destinos post-login |
| 13 | Contraseña admin distinta de la de sesión (padre/docente) |

## Criterios de aceptación

- [ ] UI de login con todos los campos y botones del brief.
- [ ] Selector de rol visible y funcional.
- [ ] “Recordar sesión” persiste y restaura al reabrir la app.
- [ ] Crear cuenta crea usuario mock y permite login.
- [ ] Cada rol navega a su panel (aunque el panel sea stub).

## Notas técnicas

- Rutas: `login`, `register`, `forgot-password`
- `AuthRepository` + `SessionStore`
- Guard de navegación: si hay sesión → saltar welcome/login
