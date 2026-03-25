# Coding Conventions

This document describes the code style and conventions used throughout the
Super Mario Clone project.

## Language & Version

- **Java 17+** (switch expressions, text blocks, enhanced pattern matching)
- No external dependencies — pure JDK only

## Naming

| Element | Style | Example |
|---------|-------|---------|
| Packages | lowercase, dot-separated | `mario.entities` |
| Classes | PascalCase, nouns | `GamePanel`, `SpriteRenderer` |
| Interfaces / Enums | PascalCase | `Block.Type` |
| Methods | camelCase, verbs | `resolvePlayerTileCollisions()` |
| Constants | UPPER_SNAKE_CASE | `TILE_SIZE`, `GRAVITY` |
| Local variables | camelCase | `leftTile`, `animFrame` |
| Boolean flags | `is`/`has`/`on` prefix where clear | `isAlive()`, `onGround` |

## Formatting

- **Indentation**: 4 spaces (no tabs)
- **Braces**: K&R style (opening brace on same line)
- **Line length**: aim for ≤ 100 characters; hard-wrap at 120
- **Blank lines**: one blank line between methods; two between major
  sections inside large classes (marked with `// ── Section ──`)
- **Imports**: no wildcard imports; grouped by package

## Class Layout

Each class follows this ordering:

1. Static fields / constants
2. Instance fields
3. Constructors
4. Public methods
5. Package-private / protected methods
6. Private methods
7. Getters and setters (grouped at the bottom)

## Comments & Documentation

- **Javadoc** (`/** */`) on every public class and non-trivial public
  method. One-liner Javadoc is fine for simple getters.
- **Inline comments** only when the *why* is not obvious from the code.
- Section banners (`// ── Rendering ──`) divide large files like
  `GamePanel` into scannable regions.

## Constants

All numeric tuning values live in `mario.util.Constants`:

```java
public static final double GRAVITY    = 0.6;
public static final double JUMP_FORCE = -11.0;
```

**Never hard-code magic numbers** in game logic. If a value could
conceivably be tweaked, add it to `Constants`.

## Immutability

- Fields are `final` whenever possible.
- Utility classes (`Constants`, `SpriteRenderer`) are `final` with a
  private constructor to prevent instantiation.

## Error Handling

- Bounds checks return `null` (e.g., `Level.getBlock()`) rather than
  throwing exceptions, because out-of-bounds queries are expected during
  collision sweeps.
- No checked exceptions are used in game code.

## Rendering

- All drawing is done through `SpriteRenderer` static methods so visual
  changes are localised to one file.
- `Graphics2D` is always cast from `Graphics` in `paintComponent`.
- Anti-aliasing is enabled for smoother shapes.

## Level Design

Levels are defined as `String[]` character grids in `Level.java`. The map
legend is documented in `Level`'s class Javadoc. To create a new level,
copy the `MAP` array and adjust characters.

## Git

- Commit messages follow [Conventional Commits](https://www.conventionalcommits.org/):
  `feat:`, `fix:`, `docs:`, `refactor:`, `chore:`
- One logical change per commit
