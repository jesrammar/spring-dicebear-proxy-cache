$ErrorActionPreference = "Stop"

Write-Host "Running Docker container on http://localhost:8080 ..."
docker run --rm -p 8080:8080 spring-dicebear-proxy-cache
