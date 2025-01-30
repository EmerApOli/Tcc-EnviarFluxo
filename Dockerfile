FROM openjdk:17-alpine
COPY --chown=185 target/quarkus-app/lib/ /deployments/lib/
COPY --chown=185 target/quarkus-app/*.jar /deployments/
COPY --chown=185 target/quarkus-app/app/ /deployments/app/
COPY --chown=185 target/quarkus-app/quarkus/ /deployments/quarkus/
EXPOSE 8079
#ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"
ENTRYPOINT ["java", "-jar", "/deployments/quarkus-run.jar"]