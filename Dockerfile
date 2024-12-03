FROM openjdk:11-jdk-slim
COPY module1 /app
WORKDIR /app
EXPOSE 8080
CMD ["/app/gradlew", "bootRun"]
