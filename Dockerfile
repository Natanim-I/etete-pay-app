FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:25-jre

COPY --from=build /app/target/etete-pay.jar etete-pay.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "etete-pay.jar"]