# Usa un'immagine base con Maven e OpenJDK preinstallati
FROM maven:3.8.6-openjdk-17-slim AS build

# Imposta la cartella di lavoro
WORKDIR /app

# Copia il file pom.xml e scarica le dipendenze
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia tutto il resto del codice sorgente
COPY src /app/src

# Esegui il build del progetto (compilando il .jar)
RUN mvn clean package -DskipTests

# Usa un'immagine base con solo OpenJDK per eseguire l'app
FROM openjdk:17-jdk-slim

# Copia il .jar dalla fase di build
COPY --from=build /app/target/speseMensiliBE.jar /speseMensiliBE.jar

# Imposta il comando di avvio dell'app
CMD ["java", "-jar", "/speseMensiliBE.jar"]
