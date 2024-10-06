# Source: https://docs.spring.io/spring-boot/reference/packaging/container-images/dockerfiles.html (06.10.2024)
# Builder
FROM bellsoft/liberica-openjre-debian:23-cds AS builder
WORKDIR /builder
ARG JAR_FILE=target/*.jar
# rename the *.jar file to application.jar
COPY ${JAR_FILE} application.jar
# Extract the jar file using an efficient layout
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted

# Runtime
FROM bellsoft/liberica-openjre-debian:23-cds
WORKDIR /application
COPY target/classes/application.properties ./
COPY target/classes/application-prod.properties ./
COPY target/classes/data.sql ./
COPY --from=builder /builder/extracted/dependencies/ ./
COPY --from=builder /builder/extracted/spring-boot-loader/ ./
COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
COPY --from=builder /builder/extracted/application/ ./
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "application.jar"]