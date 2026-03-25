# Architecture

## Overview

The game is a single-threaded Swing application with a fixed-timestep game
loop. All rendering is done with Java2D (Graphics2D) — no external image
assets are needed.

## Package Structure

```
src/mario/
├── Main.java              # Entry point — creates JFrame
├── GamePanel.java         # Game loop, input, collision, rendering
├── Camera.java            # Viewport that tracks the player
├── entities/
│   ├── Player.java        # Mario — movement, physics, state
│   ├── Enemy.java         # Goomba — patrol AI, stomp detection
│   └── Coin.java          # Collectible coin
├── world/
│   ├── Block.java         # Single tile (ground, brick, question)
│   └── Level.java         # Tile-map loader & entity spawner
└── util/
    ├── Constants.java     # All tunable numeric values
    └── SpriteRenderer.java # Shape-based drawing helpers
```

## Class Responsibilities

### `Main`
Creates the `JFrame`, adds the `GamePanel`, and starts the game thread.

### `GamePanel`
The central orchestrator:
- **Game loop** — fixed 60 FPS via `Thread.sleep` + `System.nanoTime`.
- **State machine** — `TITLE → PLAYING → GAME_OVER | WIN → TITLE`.
- **Input** — `KeyListener` sets boolean flags consumed each frame.
- **Update** — advances player, enemies, coins; resolves tile collisions.
- **Render** — paints background, tiles, entities, and HUD every frame.

### `Camera`
Horizontal-only camera that centres on the player, clamped to level bounds.

### `Player`
Owns position, velocity, lives, score, and coins. Physics (gravity/jump)
are applied in `update()`. Collision response is handled externally by
`GamePanel.resolvePlayerTileCollisions()`.

### `Enemy`
Walks left until it hits a wall or ledge edge, then reverses. Has a
"squish" animation on stomp.

### `Coin`
Static position with a spinning animation counter. Collected on overlap
with the player.

### `Block`
Enum-typed tile: `EMPTY`, `GROUND`, `BRICK`, `QUESTION`. Question blocks
track a `hit` flag.

### `Level`
Parses a `String[]` character map into a 2-D grid of `Block` objects and
spawns enemies, coins, and the flag. The map legend:

| Char | Meaning |
|------|---------|
| `G`  | Ground  |
| `B`  | Brick   |
| `?`  | Question block |
| `E`  | Enemy spawn |
| `C`  | Coin spawn |
| `F`  | Flag (level end) |
| `.`  | Empty / air |

### `Constants`
All magic numbers live here — tile size, physics parameters, scoring
values, window dimensions. Changing one value tunes the whole game.

### `SpriteRenderer`
Static methods that draw each visual element (player, goomba, coin, block,
flag, cloud, hill, background gradient) using `Graphics2D` primitives.

## Data Flow (per frame)

```
KeyListener
    │
    ▼
GamePanel.update()
    ├── Player.update()          ← apply input + physics
    ├── resolvePlayerTileCollisions()
    ├── Enemy.update()           ← patrol AI
    ├── resolveEnemyTileCollisions()
    ├── checkPlayerEnemyCollision()
    ├── Coin.update()            ← animation
    ├── coin collection check
    ├── flag check
    └── Camera.update()
    │
    ▼
GamePanel.paintComponent()
    ├── drawBackground()
    ├── drawBlocks()
    ├── drawEntities()
    └── drawHUD()
```

## Collision Detection

Axis-aligned bounding box (AABB) collision. For player-tile collisions the
overlap is computed on both axes; the axis with the smaller overlap is
resolved first (minimum translation vector). Enemy collisions use a
separate "feet" hitbox to detect stomps.
