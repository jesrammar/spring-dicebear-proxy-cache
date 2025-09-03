# DiceBear Proxy + Cache (Spring Boot)

[![Maven CI](https://github.com/jesusctqx5w2/spring-dicebear-proxy-cache/actions/workflows/maven.yml/badge.svg)](https://github.com/jesusctqx5w2/spring-dicebear-proxy-cache/actions/workflows/maven.yml)



Servicio **proxy** para [DiceBear](https://www.dicebear.com/) con **caché Caffeine** y **reintentos Resilience4j**.  
Reduce latencia y evita el *rate-limit* del servicio externo.

---

## 🚀 Stack

- ☕ **Java 21** · Spring Boot 3  
- 🌐 **WebClient** (reactivo)  
- ⚡ **Caffeine Cache** · **Resilience4j** (retry)  
- 📊 **Actuator** · **springdoc-openapi** (Swagger)  
- 🤖 **GitHub Actions** (Maven CI)  

---

## ▶️ Ejecutar en local

```bash
mvn spring-boot:run
