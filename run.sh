#!/usr/bin/env bash
# Build and run the Super Mario Clone
set -euo pipefail

echo "=== Compiling ==="
mkdir -p out
javac -d out \
    src/mario/util/*.java \
    src/mario/entities/*.java \
    src/mario/world/*.java \
    src/mario/*.java

echo "=== Launching ==="
java -cp out mario.Main
