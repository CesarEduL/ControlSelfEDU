---
name: prp-manager
description: >-
  Crea, actualiza e indexa Product Requirement Prompts (PRPs) en español:
  carpeta prps/, README índice, archivos PRP-NN-slug.md con plantilla estándar.
  Usar cuando el usuario pida PRPs, prp manager, Product Requirement Prompts,
  documentar requisitos por módulos, o planificar implementación por PRPs.
---

# PRP Manager

Gestiona **Product Requirement Prompts** (no Pull Requests de GitHub).

## Cuándo aplicar

- Usuario dice: “haz los PRPs”, “prp manager”, “documenta requisitos”, “índice de PRPs”.
- Hay brief/PDF/specs y hay que partir el producto en dominios implementables.
- Hay que actualizar, renumerar o sincronizar un `prps/README.md`.

## Ubicación

```
prps/
├── README.md
├── PRP-00-vision-y-alcance.md
└── PRP-NN-….md
```

Español. Markdown. Sin implementar código salvo petición explícita.

## Workflow

1. Leer brief + `prps/` existente + scaffold.
2. Partir por dominio (un PRP = una responsabilidad).
3. Numerar desde `PRP-00` (visión) y respetar dependencias.
4. Escribir con la plantilla (ver abajo).
5. Actualizar `prps/README.md` y enlace en README del repo.
6. Indicar el siguiente PRP a implementar.

## Plantilla

```markdown
# PRP-NN — Título corto

## Objetivo
## Alcance
## Fuera de alcance (por ahora)
## Dependencias con otros PRPs
## Criterios de aceptación
## Notas técnicas
```

## Reglas

- PRP ≠ Pull Request de GitHub.
- No duplicar números; actualizar si ya existe.
- Mock-first si no hay backend.
- Checks de aceptación verificables.

Detalle: [template.md](template.md)
