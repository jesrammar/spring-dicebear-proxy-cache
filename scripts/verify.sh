#!/usr/bin/env sh
set -eu

echo "Running full Maven verification..."
mvn -B -ntp verify
