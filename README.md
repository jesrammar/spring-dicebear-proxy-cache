# spring-dicebear-proxy-cache

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-111827?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21" />
  <img src="https://img.shields.io/badge/Spring%20Boot-3.3.2-111827?style=for-the-badge&logo=springboot&logoColor=6DB33F" alt="Spring Boot 3.3.2" />
  <img src="https://img.shields.io/badge/Caffeine-Cache-111827?style=for-the-badge" alt="Caffeine Cache" />
  <img src="https://img.shields.io/badge/Resilience4j-Retry-111827?style=for-the-badge" alt="Resilience4j Retry" />
  <img src="https://img.shields.io/badge/OpenAPI-Swagger-111827?style=for-the-badge&logo=swagger&logoColor=85EA2D" alt="OpenAPI Swagger" />
</p>

<p align="center">
  <a href="https://github.com/jesrammar/spring-dicebear-proxy-cache/actions/workflows/maven.yml">
    <img src="https://github.com/jesrammar/spring-dicebear-proxy-cache/actions/workflows/maven.yml/badge.svg" alt="Maven CI" />
  </a>
</p>

API backend en Spring Boot que actua como proxy hacia [DiceBear](https://www.dicebear.com/), incorporando cache con Caffeine, retry con Resilience4j, documentacion OpenAPI y endpoints de observabilidad con Actuator.

## Que resuelve

- Evita acoplar el cliente directamente a un proveedor externo.
- Reduce llamadas repetidas mediante cache.
- Introduce control backend sobre timeouts, errores y configuracion del upstream.
- Sirve como proyecto de portfolio backend con foco en integraciones HTTP, testing y calidad.

## Stack tecnico

- Java 21
- Spring Boot 3.3
- Spring MVC + WebClient
- Caffeine Cache
- Resilience4j Retry
- Spring Actuator
- springdoc-openapi
- JUnit 5 + WireMock
- Maven + GitHub Actions + JaCoCo

## Arquitectura

El proyecto esta separado en capas sencillas:

- `web`: expone el endpoint HTTP y valida entrada.
- `application`: contiene el caso de uso principal (`AvatarService`).
- `infrastructure.client`: encapsula la llamada a DiceBear con `WebClient`.
- `infrastructure.config`: propiedades tipadas y configuracion tecnica.
- `infrastructure.exception`: manejo consistente de errores para el consumidor.

No busca ser una arquitectura compleja, sino una base pequena y mantenible para un backend realista.

## Endpoint principal

`GET /avatar/{seed}`

Parametros soportados:

- `style`: estilo de DiceBear. Si no se envia, usa `adventurer`.
- cualquier query param adicional compatible con DiceBear, por ejemplo `size=64`.

Ejemplos:

```bash
curl "http://localhost:8080/avatar/demo-user?style=bottts"
curl "http://localhost:8080/avatar/demo-user?style=adventurer&size=64"
```

## Endpoints utiles

- Swagger UI: `http://localhost:8080/swagger-ui`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- Actuator health: `http://localhost:8080/actuator/health`
- Actuator info: `http://localhost:8080/actuator/info`
- Actuator metrics: `http://localhost:8080/actuator/metrics`

Ejemplos de metricas utiles:

```bash
curl "http://localhost:8080/actuator/metrics/cache.gets"
curl "http://localhost:8080/actuator/metrics/http.server.requests"
curl "http://localhost:8080/actuator/metrics/resilience4j.retry.calls"
```

## Configuracion relevante

```properties
dicebear.base-url=https://api.dicebear.com
dicebear.version=7.x
dicebear.connect-timeout-millis=2000
dicebear.response-timeout-millis=3000

spring.cache.caffeine.spec=maximumSize=5000,expireAfterWrite=7d
resilience4j.retry.instances.dicebear.max-attempts=3
resilience4j.retry.instances.dicebear.wait-duration=300ms
```

## Ejecutar en local

```bash
mvn spring-boot:run
```

La aplicacion arranca por defecto en `http://localhost:8080`.

Atajos incluidos:

```bash
./scripts/run-local.sh
./scripts/verify.sh
./scripts/docker-build.sh
./scripts/docker-run.sh
```

En Windows PowerShell:

```powershell
./scripts/run-local.ps1
./scripts/verify.ps1
./scripts/docker-build.ps1
./scripts/docker-run.ps1
```

## Ejecutar con Docker

Construccion de imagen:

```bash
docker build -t spring-dicebear-proxy-cache .
```

Ejecucion:

```bash
docker run --rm -p 8080:8080 spring-dicebear-proxy-cache
```

La imagen genera el artefacto dentro de un stage de build y ejecuta la aplicacion sobre `eclipse-temurin:21-jre`.

## Testing y calidad

- Tests de integracion con WireMock, sin depender de la red externa.
- Verificacion de respuesta correcta, default style, gestion de error upstream y comportamiento de cache.
- Validacion de entrada comprobada con tests de contrato HTTP.
- Cobertura con JaCoCo.
- CI ejecutando `mvn -B -ntp verify` y publicando artefactos de test/cobertura.

Para lanzar la suite:

```bash
mvn -B -ntp verify
```

## Decisiones tecnicas

- `WebClient` se usa como cliente HTTP del upstream, pero la API expuesta sigue siendo MVC porque el caso de uso del proyecto no necesita exponer una API reactiva completa.
- La cache se aplica a nivel de servicio para mantener el controller fino.
- La llamada al proveedor externo esta encapsulada para facilitar testing y evolucion futura.
- Los errores del upstream se traducen a `502 Bad Gateway`, que es una semantica razonable para un proxy backend.
- Actuator expone `health`, `info` y `metrics`, y `info` incluye metadata de build generada por Maven.

## Objetivo del repositorio

Este proyecto forma parte de mi portfolio como perfil backend Java/Spring Boot. La idea no es simular una plataforma compleja, sino demostrar buen criterio tecnico en un servicio pequeno: integracion externa, cache, resiliencia, testing fiable, observabilidad y documentacion limpia.

## Detalles de mantenimiento

- `.editorconfig` para mantener formato consistente entre editores.
- scripts simples para los comandos mas usados.
- `.gitignore` ampliado para evitar ruido de IDE y artefactos locales.
