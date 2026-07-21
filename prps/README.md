# Índice de PRPs — ControlSelf EDU

Product Requirement Prompts de la app Android **ControlSelf EDU**: control parental educativo con lecciones para desbloquear tiempo de entretenimiento.

Fuente de requisitos: documento *ControlSelf EDU* (brief del producto).

## Orden recomendado de implementación

```
PRP-00 → PRP-01 → PRP-02 → PRP-03 → PRP-04
                              ↓
              PRP-05 → PRP-06 → PRP-07 → PRP-08
                              ↓
                    PRP-09 → PRP-10
                              ↓
                         PRP-11 · PRP-12
                              ↓
                           PRP-13
```

| Orden | PRP | Dominio | Depende de | Estado código |
|-------|-----|---------|------------|---------------|
| 1 | [PRP-00](PRP-00-vision-y-alcance.md) | Visión y alcance | — | 📄 |
| 2 | [PRP-01](PRP-01-arquitectura-android.md) | Arquitectura Android | 00 | ✅ |
| 3 | [PRP-02](PRP-02-bienvenida-tema.md) | Bienvenida y tema visual | 00, 01 | ✅ |
| 4 | [PRP-03](PRP-03-autenticacion-roles.md) | Login, registro (Padre/Docente), roles | 00–02 | ✅ |
| 5 | [PRP-04](PRP-04-panel-estudiante.md) | Dashboard estudiante | 03 | ✅ |
| 6 | [PRP-05](PRP-05-monitoreo-uso.md) | Tiempo en redes/juegos | 01, 04 | ✅ |
| 7 | [PRP-06](PRP-06-bloqueo-automatico.md) | Overlay a los 30 min | 05, 07 | ✅ |
| 8 | [PRP-07](PRP-07-evaluaciones.md) | Cursos y 20 preguntas | 04, 06 | ✅ |
| 9 | [PRP-08](PRP-08-resultados-feedback.md) | Éxito / reintento | 07 | ✅ |
| 10 | [PRP-09](PRP-09-motivacion.md) | Rachas, logros, medallas | 04, 08 | ✅ |
| 11 | [PRP-10](PRP-10-estadisticas.md) | Gráficos y métricas | 05, 08, 09 | ✅ |
| 12 | [PRP-11](PRP-11-panel-docente.md) | Panel profesor (aporte comunitario) | 03, 07 | ✅ |
| 13 | [PRP-12](PRP-12-panel-padre.md) | Panel padre + alta de hijos | 03, 05, 09 | ⏳ |
| 14 | [PRP-13](PRP-13-anti-desinstalacion.md) | Protección desinstalación | 03, 11, 12 | ✅ |

Leyenda: ✅ hecho · ⏳ parcial · 📄 solo spec (visión)

## Estado del ciclo

El orden **PRP-00 → PRP-13** está cubierto en código base (01–13), con **excepción parcial**:

- **PRP-12**: UI de alta/listado/selección de N hijos sobre la API de [PRP-03](PRP-03-autenticacion-roles.md) pendiente de cerrar.
- **PRP-00** es visión/alcance (documento de producto).

**Siguiente a implementar:** [PRP-12](PRP-12-panel-padre.md) (alta multi-hijo en panel padre).

### Jerarquía de identidad (resumen)

| Rol | Registro público | Quién crea la cuenta |
|-----|------------------|----------------------|
| Padre | Sí | Él mismo |
| Docente | Sí | Él mismo (ente separado; aporta ejercicios) |
| Estudiante | No | Solo el padre (credenciales que él define; 1→N) |

## Convención de cada PRP

Cada archivo incluye: **Objetivo**, **Alcance**, **Fuera de alcance**, **Dependencias**, **Criterios de aceptación**, **Notas técnicas**.

Se implementan **uno a uno**, sin mezclar dominios en la misma sesión salvo dependencias mínimas.
