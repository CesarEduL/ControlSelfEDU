# PRP-13 — Protección contra desinstalación

## Objetivo

Impedir (o disuadir) que el estudiante desinstale ControlSelf EDU sin una contraseña de administrador conocida solo por el docente o el padre/madre.

## Alcance

### Flujo deseado (brief)

1. Estudiante intenta desinstalar.
2. Sistema solicita contraseña de administrador.
3. Correcta → desinstalación continúa.
4. Incorrecta → se cancela y la app permanece.

### Realidad de Android (documentada in-app)

En Android de consumo **no existe** una API pública que intercepte el diálogo de desinstalación del launcher con password, salvo perfiles privilegiados:

| Enfoque | Viabilidad | Uso en esta app |
|---------|------------|-----------------|
| **Device Admin** (legado) | Disuasión: hay que desactivar admin antes de desinstalar | MVP |
| **Device Owner** (MDM) | Alto control en dispositivos escolares | Fase institucional / QA con `adb` |
| **App pin / lock task** | No evita desinstalación desde Ajustes | Fuera de MVP |
| **Accessibility / overlay** | Frágil / rechazable en Play | Evitar |

### Spec MVP (implementado)

- Contraseña admin configurable por **padre** y **docente** (PBKDF2 + salt en DataStore).
- Pantalla `protection/admin`: estado, límites, activar/desactivar Device Admin.
- Desactivar Device Admin **desde la app** exige la contraseña admin.
- `onDisableRequested` muestra aviso si intentan desactivar desde Ajustes.
- El estudiante **no** ve ni resetea la contraseña.

### Fase institucional (documentada)

```text
adb shell dpm set-device-owner com.controlself.edu/.system.admin.ControlSelfDeviceAdminReceiver
```

## Fuera de alcance (por ahora)

- Garantizar 100% anti-desinstalación en dispositivos no gestionados.
- Root kits / modificación del sistema.

## Dependencias con otros PRPs

| PRP | Relación |
|-----|----------|
| 03 | Roles padre/docente |
| 11, 12 | CTAs hacia pantalla de protección |

## Criterios de aceptación

- [x] Padre/docente puede definir y cambiar contraseña admin.
- [x] Documentación in-app de límites de Android.
- [x] Device Admin opcional activable; desactivar desde la app pide password.
- [x] Estudiante no tiene UI para ver/cambiar la contraseña.
- [x] Notas de Device Owner para builds de prueba documentadas.

## Implementación

| Área | Ubicación |
|------|-----------|
| Hash password | `PersistentAdminPasswordRepository` |
| Gateway | `AndroidDeviceAdminGateway` |
| Receiver | `ControlSelfDeviceAdminReceiver` + `@xml/device_admin` |
| UI | `AdminProtectionScreen` |
| Ruta | `protection/admin` |

## Notas técnicas

- Separar login de `adminUninstallPassword`.
- Ciclo de PRPs completado (00–13).
