# PRP-13 — Protección contra desinstalación

## Objetivo

Impedir que el estudiante desinstale ControlSelf EDU sin una contraseña de administrador conocida solo por el docente o el padre/madre.

## Alcance

### Flujo deseado (brief)

1. Estudiante intenta desinstalar.
2. Sistema solicita contraseña de administrador.
3. Correcta → desinstalación continúa.
4. Incorrecta → se cancela y la app permanece.

### Realidad de Android (obligatorio documentar)

En Android de consumo **no existe** una API pública simple que intercepte el diálogo de desinstalación del launcher para pedir password, salvo perfiles privilegiados:

| Enfoque | Viabilidad | Uso |
|---------|------------|-----|
| **Device Admin** (legado) | Limitado; muchas APIs deprecadas | Disuasión parcial |
| **Device Owner / Profile Owner** (MDM) | Alto control en dispositivos escolares provisionados | Instituciones |
| **App pin / lock task** | Evita salir de la app, no la desinstalación desde Ajustes | Complemento |
| **Accessibility / overlay** | Frágil y rechazable en Play | Evitar como única defensa |

### Spec de producto viable (MVP + fases)

**MVP (sin Device Owner):**

- Contraseña admin configurable por padre/docente (hash en DataStore).
- Pantalla de “protección activa” que educa y pide activar Device Admin si está disponible.
- Si el estudiante abre Ajustes para desinstalar, la app puede detectar pérdida de admin / intento vía receiver limitado y notificar al padre (best-effort).

**Fase institucional:**

- Provisionamiento Device Owner (Android Enterprise).
- Política: desinstalación de la app de control solo con admin.

### Quién conoce la contraseña

- Docente (implementación institucional) y/o padre/madre.
- El estudiante **no** puede verla ni resetearla sin el rol admin.

## Fuera de alcance (por ahora)

- Garantizar 100% anti-desinstalación en dispositivos no gestionados (imposible de forma confiable y compatible con Play).
- Root kits / modificación del sistema.

## Dependencias con otros PRPs

| PRP | Relación |
|-----|----------|
| 03 | Roles padre/docente |
| 11, 12 | UI para setear contraseña admin |

## Criterios de aceptación

- [ ] Padre/docente puede definir y cambiar contraseña admin.
- [ ] Documentación in-app de límites de Android.
- [ ] En build de prueba con Device Owner (si se provisiona), la desinstalación requiere admin.
- [ ] En build consumer, al menos Device Admin opcional + aviso claro de limitaciones.

## Notas técnicas

- Nunca guardar contraseña en texto plano (hash + salt).
- Separar `sessionPassword` (login) de `adminUninstallPassword`.
- Probar políticas con `adb shell dpm set-device-owner` solo en dispositivos de desarrollo
