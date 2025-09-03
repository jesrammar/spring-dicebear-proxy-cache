# DiceBear Proxy + Cache (Spring Boot)

![Java](https://img.shields.io/badge/Java-21-red?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3-brightgreen?style=for-the-badge&logo=springboot)
[![Maven CI](https://github.com/jesusctqx5w2/spring-dicebear-proxy-cache/actions/workflows/maven.yml/badge.svg)](https://github.com/jesusctqx5w2/spring-dicebear-proxy-cache/actions/workflows/maven.yml)







Servicio **proxy** para [DiceBear](https://www.dicebear.com/) con **caché Caffeine** y **reintentos Resilience4j**.
Reduce latencia y evita rate-limit del servicio externo.


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
