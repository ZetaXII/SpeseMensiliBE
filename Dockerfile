# Fase di build: usa un'immagine con OpenJDK 17
FROM openjdk:17-jdk-slim AS build

# Installa Maven
RUN apt-get update && apt-get install -y maven

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

# Imposta la cartella di lavoro
WORKDIR /app

# Copia il file .jar dalla fase di build
# Sostituire "speseMensiliBE-0.0.1-SNAPSHOT.jar" con il nome effettivo del file .jar generato
COPY --from=build /app/target/speseMensiliBE-0.0.1-SNAPSHOT.jar /speseMensiliBE.jar

# Imposta il comando di avvio dell'app
CMD ["java", "-jar", "/speseMensiliBE.jar"]
