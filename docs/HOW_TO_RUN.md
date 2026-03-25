# How to Run

## Prerequisites

| Requirement | Minimum Version | Check command |
|-------------|-----------------|---------------|
| **Java JDK** | 17+ (tested on 21) | `java -version` |

No build tools (Maven, Gradle) are required — the project compiles with
`javac` directly. No external libraries are needed.

## Build & Run

### Option 1 — Shell script (recommended)

```bash
chmod +x run.sh
./run.sh
```

The script compiles and launches the game in one step.

### Option 2 — Manual

```bash
# 1. Compile all sources into the out/ directory
mkdir -p out
javac -d out \
    src/mario/util/*.java \
    src/mario/entities/*.java \
    src/mario/world/*.java \
    src/mario/*.java

# 2. Run
java -cp out mario.Main
```

### Option 3 — Create a runnable JAR

```bash
mkdir -p out
javac -d out src/mario/util/*.java src/mario/entities/*.java src/mario/world/*.java src/mario/*.java

# Create manifest
echo "Main-Class: mario.Main" > out/MANIFEST.MF

# Package
jar cfm SuperMario.jar out/MANIFEST.MF -C out .

# Run
java -jar SuperMario.jar
```

## Controls

| Key | Action |
|-----|--------|
| ← / A | Move left |
| → / D | Move right |
| ↑ / W / Space | Jump |
| Enter | Start game / navigate menus |

## Troubleshooting

| Problem | Solution |
|---------|----------|
| `javac: command not found` | Install a JDK 17+ and ensure `JAVA_HOME/bin` is on `PATH` |
| Window does not appear | Make sure you are running on a system with a display (not a headless server) |
| Low frame rate | The game targets 60 FPS — close other heavy applications |

## Project Structure

```
super-mario-clone/
├── README.md
├── run.sh              # One-step build & run
├── docs/
│   ├── ARCHITECTURE.md
│   ├── HOW_TO_RUN.md
│   └── CODING_CONVENTIONS.md
└── src/mario/          # All source code
    ├── Main.java
    ├── GamePanel.java
    ├── Camera.java
    ├── entities/
    ├── world/
    └── util/
```
