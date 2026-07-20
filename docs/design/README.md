# Diseño UI — pack Stitch “inicio”

Fuente: `Downloads/stich/inicio` (Core Educational Minimalist).

## Aplicado en código

| Diseño | Pantalla Compose |
|--------|------------------|
| `core_educational_minimalist` | `Color.kt`, `Type.kt`, `Theme.kt`, `ui/components/DesignSystem.kt` |
| `a.1_welcome_screen_minimalist` | `WelcomeScreen` (Empezar / Tengo una cuenta) |
| `a.2_login_screen_minimalist` | `LoginScreen` + `RoleSelector` |
| Auth extendido | `RegisterScreen`, `ForgotPasswordScreen` |
| `b.5_student_home_minimalist` | Home estudiante + **bottom nav** (Inicio/Cursos/Stats/Perfil) |
| Quiz | `CourseSelect`, `CourseIntro`, `QuizPlay`, `QuizReview`, `QuizResult` |
| `b.8_9_quiz_results_minimalist` | `QuizResultScreen` |
| `b.11_lock_screen_animated_v2` | `LockScreen` |
| Paneles | `ParentHome`, `ParentAttemptDetail`, `TeacherHome` + subpantallas, `AdminProtection` |
| Pack `padres` | Ver `docs/design/padres/README.md` |
| Pack `maestros` | Ver `docs/design/maestros/README.md` |

## Pendiente

- Fuente Nunito Sans embebida (ahora SansSerif del sistema con mismos pesos).

## Tokens clave

- Background `#F7F9FB`, Primary navy `#001126` / `#12263F`
- Secondary (estudiante) `#006C52` + container `#7BF9CE`
- Cards: blanco + borde `#C4C6CE`, sin sombra
