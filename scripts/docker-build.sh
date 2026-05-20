#!/usr/bin/env sh
set -eu

echo "Building Docker image spring-dicebear-proxy-cache..."
docker build -t spring-dicebear-proxy-cache .
