# Super Mario Clone — Java

A simple Super Mario Bros-style 2D platformer built in pure Java (Swing/AWT).
No external libraries or sprite assets required — everything is rendered with
coloured shapes.

![Java](https://img.shields.io/badge/Java-21-blue)

## Features

| Feature | Description |
|---------|-------------|
| **Side-scrolling** | Camera follows Mario through a tile-based level |
| **Physics** | Gravity, jumping, ground detection |
| **Enemies** | Goombas patrol platforms; stomp them to score points |
| **Coins** | Collect spinning coins for bonus score |
| **Blocks** | Ground, brick, and animated "?" blocks |
| **Level design** | Text-map level definition — easy to edit |
| **HUD** | Live score, coin count, and remaining lives |
| **Game states** | Title screen → Playing → Game Over / Win |

## Quick Start

```bash
# Compile
mkdir -p out
javac -d out src/mario/util/*.java src/mario/entities/*.java src/mario/world/*.java src/mario/*.java

# Run
java -cp out mario.Main
```

Or use the helper script:

```bash
./run.sh
```

## Controls

| Key | Action |
|-----|--------|
| ← / A | Move left |
| → / D | Move right |
| ↑ / W / Space | Jump |
| Enter | Start game / Return to title |

## Documentation

| Document | Description |
|----------|-------------|
| [Architecture](docs/ARCHITECTURE.md) | Package structure, class diagram, data flow |
| [How to Run](docs/HOW_TO_RUN.md) | Build & run instructions, requirements |
| [Coding Conventions](docs/CODING_CONVENTIONS.md) | Style guide and project conventions |

## License

This is an educational / demo project. Not affiliated with Nintendo.
