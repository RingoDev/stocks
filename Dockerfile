FROM openjdk:14-jdk-slim

# building fat jar
RUN .mvnw package -DskipTests

# extracting fat jar
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# adding user spring to group spring
RUN addgroup --system spring && adduser --system spring --ingroup spring

# setting app user to spring
USER spring

# creating different layers
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app

# setting entrypoint
ENTRYPOINT ["java","-cp","app:app/lib/*","com.ringodev.stocks.StocksApplication"]
