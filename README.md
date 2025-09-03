# DiceBear Proxy + Cache (Spring Boot)

![Build](https://github.com/jesusctqx5w2/spring-dicebear-proxy-cache/actions/workflows/maven.yml/badge.svg)


Servicio **proxy** para [DiceBear](https://www.dicebear.com/) con **caché Caffeine** y **reintentos Resilience4j**.
Reduce latencia y evita rate-limit del servicio externo.

## Stack
- Java 21 · Spring Boot 3
- WebClient (reactivo)
- Caffeine Cache · Resilience4j (retry)
- Actuator · springdoc-openapi (Swagger)
- GitHub Actions (Maven CI)

## Ejecutar
```bash
mvn spring-boot:run
# Swagger: http://localhost:8080/swagger-ui
