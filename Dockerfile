
FROM gradle:8.3-jdk20 AS build
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

FROM openjdk:20-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/com.example.ktor-cart-all.jar /app/com.example.ktor-cart.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/com.example.ktor-cart.jar"]
