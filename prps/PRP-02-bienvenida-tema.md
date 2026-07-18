# PRP-02 — Bienvenida, tema y navegación base

## Objetivo

Entregar la primera experiencia visual de la app: splash/bienvenida con marca ControlSelf EDU, tema azul/blanco/verde, y navegación mínima hacia el login (placeholder hasta PRP-03).

## Alcance

### Pantalla de bienvenida

- Fondo con gradiente azul (tecnología).
- Logo / marca centrada: **ControlSelf EDU**.
- Tagline corto (ej. “Aprende. Controla. Crece.”).
- Animación de entrada (fade + scale).
- Tras ~2 segundos → navega a login y saca welcome del back stack.

### Tema Material 3

| Token | Uso |
|-------|-----|
| Azul primario (`#1565C0`) | Acciones, marca, splash |
| Verde secundario (`#2E7D32`) | Educación, éxito, barra “tiempo OK” |
| Blanco / surface clara | Fondos de pantallas |
| Amarillo / rojo | Advertencia y peligro (barra de tiempo; PRP-04/05) |

### Navegación base

- `NavHost` con rutas `welcome` y `login`.
- Splash nativo (`core-splashscreen`) alineado al tema.

### Estado de implementación actual

Ya existe en el repo:

- `WelcomeScreen`, `ControlSelfEDUTheme`, `ControlSelfNavHost`
- Placeholder `LoginPlaceholderScreen`

## Fuera de alcance (por ahora)

- Formulario de login funcional (PRP-03).
- Logo definitivo de marca (placeholder vectorial OK).

## Dependencias con otros PRPs

| PRP | Relación |
|-----|----------|
| 00 | Colores y copy del brief |
| 01 | Arquitectura UI |
| 03 | Sustituye el placeholder de login |

## Criterios de aceptación

- [x] App muestra splash/bienvenida con marca.
- [x] Colores azul / blanco / verde en tema.
- [x] Tras unos segundos llega a pantalla de login (aunque sea placeholder).
- [ ] Logo final reemplaza placeholder (opcional MVP).

## Notas técnicas

- Archivos: `ui/theme/*`, `ui/screens/welcome/WelcomeScreen.kt`, `navigation/*`
- Probar: Run en emulador → splash → “Inicio de sesión”
