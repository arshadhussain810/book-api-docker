#Stage-1: build
FROM maven:3.9.9 AS build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

#Stage-2: run
FROM eclipse-temurin:21-jdk

WORKDIR /app

#Installing mysql client
RUN apt-get update && apt-get install -y mysql-client && rm -rf /var/lib/apt/lists/*

#Copying jar files
COPY --from=build /app/target/*.jar app.jar

#Copy wait script
COPY wait-for-mysql.sh wait-for-mysql.sh

RUN chmod +x wait-for-mysql.sh

EXPOSE 8080

ENTRYPOINT ["./wait-for-mysql.sh"]

