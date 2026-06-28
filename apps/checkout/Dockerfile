# syntax=docker/dockerfile:1

# ---- build: compila e empacota o jar com JDK 21 ----
# Usa a imagem do Maven (em vez do mvnw) para evitar problemas de CRLF do wrapper no Windows.
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# baixa dependências primeiro (camada cacheável enquanto o pom não muda)
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

# compila e empacota (testes rodam no CI/local, não na imagem)
COPY src ./src
RUN mvn -B -q clean package -DskipTests

# ---- runtime: só o JRE 21 + o jar ----
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# usuário sem privilégios
RUN useradd --system --no-create-home spring
USER spring

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
