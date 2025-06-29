# Usar la imagen base de OpenJDK 21
FROM openjdk:21-jdk-slim

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo JAR del microservicio "users" (Asegúrate de que el JAR esté en build/libs)
COPY build/libs/co.edu.unicauca.monitoring.tool.backend.users.ms-0.0.1-SNAPSHOT.jar /app/monitoring-tool-users-ms.jar

# Exponer el puerto en el que el microservicio escuchará
EXPOSE 8090

# Comando para ejecutar el microservicio
ENTRYPOINT ["java", "-jar", "monitoring-tool-users-ms.jar"]
