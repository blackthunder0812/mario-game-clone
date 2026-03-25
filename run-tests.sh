#!/usr/bin/env bash
# Compile and run JUnit 5 tests for the Super Mario Clone
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

LIB="lib/*"
SRC_OUT="out-test/classes"
TEST_OUT="out-test/test-classes"

echo "=== Cleaning ==="
rm -rf out-test
mkdir -p "$SRC_OUT" "$TEST_OUT"

echo "=== Compiling source ==="
javac -d "$SRC_OUT" \
    src/mario/util/*.java \
    src/mario/entities/*.java \
    src/mario/world/*.java \
    src/mario/*.java

echo "=== Compiling tests ==="
javac -d "$TEST_OUT" \
    -cp "$SRC_OUT:$LIB" \
    test/mario/util/*.java \
    test/mario/entities/*.java \
    test/mario/world/*.java \
    test/mario/*.java

echo "=== Running tests ==="
java -jar lib/junit-platform-console-standalone-1.10.2.jar \
    --class-path "$SRC_OUT:$TEST_OUT" \
    --scan-class-path "$TEST_OUT" \
    --details verbose
