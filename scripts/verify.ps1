$ErrorActionPreference = "Stop"

Write-Host "Running full Maven verification..."
mvn -B -ntp verify
