---
name: Core Educational Minimalist
colors:
  surface: '#f7f9fb'
  surface-dim: '#d8dadc'
  surface-bright: '#f7f9fb'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f2f4f6'
  surface-container: '#eceef0'
  surface-container-high: '#e6e8ea'
  surface-container-highest: '#e0e3e5'
  on-surface: '#191c1e'
  on-surface-variant: '#44474d'
  inverse-surface: '#2d3133'
  inverse-on-surface: '#eff1f3'
  outline: '#74777e'
  outline-variant: '#c4c6ce'
  surface-tint: '#4d5f7b'
  primary: '#001126'
  on-primary: '#ffffff'
  primary-container: '#12263f'
  on-primary-container: '#7b8eac'
  inverse-primary: '#b4c8e8'
  secondary: '#006c52'
  on-secondary: '#ffffff'
  secondary-container: '#7bf9ce'
  on-secondary-container: '#007257'
  tertiary: '#1a0e00'
  on-tertiary: '#ffffff'
  tertiary-container: '#362100'
  on-tertiary-container: '#a8875a'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#d4e3ff'
  primary-fixed-dim: '#b4c8e8'
  on-primary-fixed: '#061c35'
  on-primary-fixed-variant: '#354862'
  secondary-fixed: '#7bf9ce'
  secondary-fixed-dim: '#5ddcb3'
  on-secondary-fixed: '#002117'
  on-secondary-fixed-variant: '#00513d'
  tertiary-fixed: '#ffddb3'
  tertiary-fixed-dim: '#e6c08f'
  on-tertiary-fixed: '#291800'
  on-tertiary-fixed-variant: '#5c421b'
  background: '#f7f9fb'
  on-background: '#191c1e'
  surface-variant: '#e0e3e5'
typography:
  headline-xl:
    fontFamily: Nunito Sans
    fontSize: 40px
    fontWeight: '800'
    lineHeight: 48px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Nunito Sans
    fontSize: 32px
    fontWeight: '800'
    lineHeight: 40px
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Nunito Sans
    fontSize: 24px
    fontWeight: '700'
    lineHeight: 32px
  body-lg:
    fontFamily: Nunito Sans
    fontSize: 18px
    fontWeight: '400'
    lineHeight: 28px
  body-md:
    fontFamily: Nunito Sans
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  label-bold:
    fontFamily: Nunito Sans
    fontSize: 14px
    fontWeight: '700'
    lineHeight: 20px
  label-sm:
    fontFamily: Nunito Sans
    fontSize: 12px
    fontWeight: '600'
    lineHeight: 16px
  headline-lg-mobile:
    fontFamily: Nunito Sans
    fontSize: 28px
    fontWeight: '800'
    lineHeight: 36px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  unit: 8px
  container-max: 1200px
  gutter: 24px
  margin-mobile: 16px
  margin-desktop: 40px
  stack-sm: 8px
  stack-md: 16px
  stack-lg: 32px
---

## Brand & Style

This design system shifts away from legacy educational aesthetics toward a **Minimalist Corporate** style that is both approachable and highly functional. The brand personality is clear, structured, and intentional, focusing on reducing cognitive load for students, parents, and teachers.

The visual narrative is built on high-contrast surfaces and flat, purposeful color blocks. It avoids decorative gradients or faux-3D effects in favor of raw clarity. By using a "color-as-function" approach, the system creates instant mental models for user roles, making the interface feel intuitive and stable. The emotional response is one of reliability and calm focus, moving the educational experience from "toy-like" to "tool-like."

## Colors

The palette utilizes a high-contrast foundation with functional color coding for primary user roles. 

- **Foundation:** The interface relies on a clean `neutral` off-white (#F8FAFC) for backgrounds to reduce glare and a deep navy `primary` (#12263F) for text and core structural elements.
- **Functional Roles:** 
  - **Student (Green):** Represents growth and action.
  - **Parent (Blue):** Evokes trust and stability.
  - **Teacher (Teal):** Suggests calm authority and organization.
- **Application:** Use solid color blocks for categories and primary actions. Avoid gradients; use 100% opacity for primary role indicators and 10% opacity of the same hue for secondary "tint" backgrounds behind role-specific content.

## Typography

The typography system exclusively uses **Nunito Sans** to maintain a friendly yet professional tone. The hierarchy is "top-heavy," meaning headlines are significantly bolder and larger to provide clear landmarks within the app.

- **Weight Strategy:** Use Extra Bold (800) for primary headers to create a distinct brand voice. Use Bold (700) for subheaders and UI labels.
- **Readability:** Body text is kept at a comfortable 16px-18px range with generous line heights to accommodate long-form educational content. 
- **Functional Labels:** All-caps labels with slight tracking should be used for category headers or metadata to differentiate them from interactive body text.

## Layout & Spacing

This design system follows a **Fluid Grid** model with strict vertical rhythm based on an 8px base unit. 

- **Desktop:** A 12-column grid with a 1200px max-width. Content is centered with 40px outer margins.
- **Mobile:** A single-column flow with 16px side margins. 
- **Whitespace:** Emphasize "Ample Whitespace." Sections should be separated by `stack-lg` (32px) to ensure the user can focus on one educational task at a time. Elements within a card or component should use `stack-sm` or `stack-md`.
- **Reflow:** On tablets, the 12-column grid collapses to 6 columns, with margins adjusting to 24px.

## Elevation & Depth

To maintain the minimalist and flat aesthetic, this system avoids traditional drop shadows.

- **Flat Tiers:** Hierarchy is created through **Tonal Layers**. The base background is light gray/off-white, while primary content containers (cards) are pure white.
- **Outlines:** Use subtle, low-contrast borders (1px solid #E2E8F0) to define containers instead of shadows.
- **Active States:** Instead of "lifting" an element with a shadow when hovered or pressed, use a subtle color shift (e.g., a 5% darken or a role-specific border-weight increase).
- **Depth:** Reserved only for critical overlays like modals, which use a semi-transparent dark backdrop (60% opacity) to dim the background, keeping the focus purely on the foreground task.

## Shapes

The shape language is consistently **Rounded**, reflecting the approachable nature of education.

- **Cards & Containers:** Use `rounded-lg` (1rem / 16px) for main content cards. This softens the high-contrast layout.
- **Buttons & Inputs:** Use the standard `rounded` (0.5rem / 8px) for interactive elements to provide a sturdy, clickable feel.
- **Role Icons:** Circular containers (pill-shaped) should be used for profile avatars or role badges to distinguish them from rectangular content blocks.

## Components

- **Buttons:** Entirely flat. Primary buttons use role-specific colors (Student Green, etc.) with white text. Secondary buttons use a transparent background with a 2px solid border in the primary navy.
- **Cards:** White background, 1px light border, 16px border-radius. No shadow. Cards for specific roles can include a 4px "accent strip" of the role color along the top edge.
- **Input Fields:** Thick 2px borders in a neutral gray that transition to the role-specific color on focus. Labels sit clearly above the field in `label-bold` typography.
- **Chips/Badges:** Small, rounded-pill containers with a 10% tint background and 100% saturation text of the same hue. Used for status (e.g., "In Progress," "Completed").
- **Lists:** Clean rows separated by subtle dividers. Each row should have a minimum height of 64px to ensure touch-friendliness.
- **Iconography:** Simple, monoline or flat-fill icons. Icons should be expressive but not detailed, using the primary navy color for maximum legibility against white surfaces.