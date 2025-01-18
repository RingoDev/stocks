# Stage 1
FROM openjdk:14 as builder

# copying needed files and making .mvnw executable
COPY ./pom.xml ./pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY ./src ./src
RUN ["chmod", "+x", "mvnw"]

# building fat jar
RUN ./mvnw package -DskipTests

# extracting fat jar
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)


# Stage 2
FROM openjdk:14-jdk-slim

# adding user spring to group spring
RUN addgroup --system spring && adduser --system spring --ingroup spring

# setting app user to spring
USER spring

# creating different layers
ARG DEPENDENCY=target/dependency
COPY --from=builder ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=builder ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=builder ${DEPENDENCY}/BOOT-INF/classes /app

# setting entrypoint
ENTRYPOINT ["java","-cp","app:app/lib/*","com.ringodev.stocks.StocksApplication"]
