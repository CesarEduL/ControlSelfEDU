# PRP-02 — Bienvenida y tema visual

## Objetivo

Entregar la primera experiencia visual: splash/bienvenida con marca **ControlSelf EDU**, tema azul/blanco/verde, y transición automática a la pantalla de login (placeholder hasta PRP-03).

## Alcance

### Pantalla de bienvenida (brief)

- Diseño moderno con colores **azul, blanco y verde**.
- Logo / marca centrada: **ControlSelf EDU**.
- Tagline corto opcional (ej. “Aprende. Controla. Crece.”) — no está en el PDF; refuerza marca.
- Animación de entrada (fade + scale).
- Tras ~2 segundos → navega a `login` y saca `welcome` del back stack.

### Tema Material 3

| Token | Hex | Uso en este PRP |
|-------|-----|-----------------|
| Azul primario | `#1565C0` | Marca, splash, acciones |
| Verde secundario | `#2E7D32` | Acento educación / éxito |
| Blanco / surface | `#FFFFFF` / `#F5F9FC` | Fondos |
| Amarillo / rojo | `#F9A825` / `#C62828` | **Solo definidos** en el tema; uso en barra de tiempo → PRP-04/05 |

### Splash nativo

- `core-splashscreen` + `Theme.ControlSelfEDU.Splash` alineado al azul de marca.
- Transición a `WelcomeScreen` (Compose) sin flash blanco brusco.

### Destino post-bienvenida

- Única ruta de salida de este PRP: `login` (placeholder).
- **No** define el grafo completo de navegación (eso es [PRP-01](PRP-01-arquitectura-android.md)).

## Fuera de alcance (por ahora)

- Formulario de login, registro, roles, recordar sesión → [PRP-03](PRP-03-autenticacion-roles.md).
- Rutas `register`, `forgot-password`, homes por rol → [PRP-01](PRP-01-arquitectura-android.md) / PRPs de panel.
- Logo definitivo de marketing (asset de marca externo). El placeholder vectorial del repo es suficiente para MVP.
- Barra de tiempo / paneles / auth real.

## Dependencias con otros PRPs

| PRP | Relación |
|-----|----------|
| 00 | Colores y copy del brief |
| 01 | Arquitectura UI y rutas (este PRP solo usa `welcome` → `login`) |
| 03 | Sustituye el placeholder de login |

## Criterios de aceptación

- [x] App muestra splash/bienvenida con marca ControlSelf EDU.
- [x] Colores azul / blanco / verde visibles en bienvenida y tema.
- [x] Tras unos segundos llega a pantalla de login (placeholder).
- [x] Tokens warning/danger existen en el tema (sin usarlos aún en UI de tiempo).
- [x] Logo de marketing final: fuera de alcance MVP (placeholder OK).

## Implementación

| Área | Ubicación | Estado |
|------|-----------|--------|
| Welcome Compose | `ui/screens/welcome/WelcomeScreen.kt` | ✅ |
| Tema | `ui/theme/*` | ✅ |
| Splash XML | `res/drawable/splash_background.xml`, `themes.xml` | ✅ |
| Nav salida | `ControlSelfNavHost` → `Routes.LOGIN` | ✅ |
| Login placeholder | `LoginPlaceholderScreen` | ✅ (UI real = PRP-03) |

## Notas técnicas

- Probar: Run → splash nativo → bienvenida animada → “Inicio de sesión”.
- Siguiente: [PRP-03](PRP-03-autenticacion-roles.md).
