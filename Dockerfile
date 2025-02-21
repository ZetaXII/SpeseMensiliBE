# Fase di build
FROM maven:3.8.6-openjdk-17-slim AS build

# Imposta la cartella di lavoro
WORKDIR /app

# Copia il codice sorgente nel container
COPY . .

# Esegui il build con Maven (senza test)
RUN mvn clean package -DskipTests

# Fase finale: immagine per l'esecuzione
FROM openjdk:17-jdk-slim

# Imposta la cartella di lavoro per l'applicazione
WORKDIR /

# Copia il file .jar dal container di build
COPY --from=build /app/target/speseMensiliBE-0.0.1-SNAPSHOT.jar /speseMensiliBE.jar

# Espone la porta su cui l'applicazione è in ascolto (modifica se necessario)
EXPOSE 8080

# Imposta il comando di avvio
ENTRYPOINT ["java", "-jar", "/speseMensiliBE.jar"]
