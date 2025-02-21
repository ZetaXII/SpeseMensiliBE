# Fase di build
FROM maven:3.8.6-openjdk-17 AS build

# Imposta la cartella di lavoro
WORKDIR /app

# Copia il pom.xml e scarica le dipendenze
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia il codice sorgente
COPY src /app/src

# Compila il progetto
RUN mvn clean package -DskipTests

# Fase finale
FROM openjdk:17-jdk-slim

# Imposta la cartella di lavoro
WORKDIR /app

# Copia il file .jar dalla fase di build
COPY --from=build /app/target/speseMensiliBE-0.0.1-SNAPSHOT.jar /speseMensiliBE.jar

# Imposta il comando di avvio dell'app
CMD ["java", "-jar", "/speseMensiliBE.jar"]
