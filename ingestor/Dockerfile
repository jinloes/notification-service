FROM eclipse-temurin:17
RUN adduser --system --group ingestor
USER ingestor:ingestor
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} ingestor.jar
ENTRYPOINT ["java","-jar","/ingestor.jar"]