$ErrorActionPreference = "Stop"

Write-Host "Building Docker image spring-dicebear-proxy-cache..."
docker build -t spring-dicebear-proxy-cache .
