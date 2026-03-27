#!/bin/bash
# Kompilacja i uruchomienie projektu Julia Set

SRC_DIR="src"
OUT_DIR="out"

echo "=== Kompilacja ==="
mkdir -p "$OUT_DIR"
javac -d "$OUT_DIR" "$SRC_DIR"/juliaset/*.java

if [ $? -eq 0 ]; then
    echo "=== Kompilacja OK ==="
    echo "=== Uruchamianie ==="
    java -cp "$OUT_DIR" juliaset.Main
else
    echo "=== Błąd kompilacji! ==="
fi
