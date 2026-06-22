# Stage 1: Builder, compilación
# Imagen base con el kit jdk 21 para compilar el código de Pagos
FROM eclipse-temurin:21-jdk AS builder

# Establece directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia primero el pom al directorio actual (/app) para aprovechar la caché de capas.
COPY pom.xml .

# Copia el Maven Wrapper (mvnw) para ejecutar Maven sin instalarlo en el sistema.
COPY mvnw .

# Configuraciones de maven wrapper
COPY .mvn .mvn

# Da permiso de ejecución al script mvnw y pre-descarga las dependencias en segundo plano
RUN chmod +x mvnw && ./mvnw dependency:go-offline -q

# Copia el código fuente
COPY src ./src

# Compila y empaqueta la aplicación a un archivo JAR saltando los Tests
RUN ./mvnw clean package -DskipTests -q

# Stage 2: Runtime
# Imagen base limpia solo con el JRE para ejecutar la aplicación de forma liviana
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copia desde el stage anterior ("builder") el JAR generado en la carpeta target
COPY --from=builder /app/target/*.jar app.jar

# Exponemos el puerto de escucha (internamente usará el 8080 que le forzamos en el docker-compose)
EXPOSE 8080

# Comando de arranque del microservicio de Pagos
ENTRYPOINT ["java", "-jar", "app.jar"]