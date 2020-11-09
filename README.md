# Stocks-WebApp


## Notes
Setting up mysql container
```
docker run --name=mysql1 -d mysql/mysql-server
docker logs mysql1 2>&1 | grep GENERATED
docker exec -it mysql1 mysql -uroot -p
```
In Mysql instance:
create spring user
```
mysql> create database db_stocks; -- Creates the new database
mysql> create user 'springuser'@'%' identified by 'ThePassword'; -- Creates the user
mysql> grant all on db_stocks.* to 'springuser'@'%'; -- Gives all privileges to the new user on the newly created database
```

creating spring boot docker image

```
docker build --rm -t ringodev/spring-docker-stocks .
```


running the mysql container

running the spring container 

linking mysql1 to mysql1
setting MYSQL_HOST variable to mysql1
porting mapping from 8085 to 8085

```
docker run --name=spring2 --link mysql1 -e MYSQL_HOST=mysql1 -p 8085:8085 -t springio/gs-spring-boot-docker
```

building spring boot image with Docker file

creating a fat jar with cmd and skipping tests:

```
./mwnw package - DskipTests

```
then unpacking fat jar with 
```
mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)
```

creating Dockerfile with split dependencies

```
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

```

building docker image:

to mount the data so java-spring can access it and insert into db add

`
-v /path-to-data-on-host/:/home/data/
`
```
docker run --name spring1 \
--link mysql1 \
-d \
-v /home/data/:/home/data/ \
-e MYSQL_HOST=mysql1 \
-e SPRING_PROFILES_ACTIVE=prod \
-d \
```


* [x] Write CommandLineRunner to insert Data into DB

* [x] Insert some stockData into DB

### Deploy

#### setup Linode Server Instance

* [x] Decide on the Specs for the instance
  * Spring Boot + Security + Docker = 200 - 300 RAM according to [stackoverflow](https://bit.ly/3cJy7ai)
  * MySql 500MB
  * [x] 2 GB RAM Instance will suffice for my needs right now, could also separate MySQL and Spring Instance
  * [ ] Separate DB and App Instances

* setup ssl/tls to enable https

* [x] Install Docker
* [x] Install MySQL DockerImage
* [x] Install Spring-Boot DockerImage
  * [ ] Set Memory constraints
* [x] give Spring Boot access to Stock .csv files -> [stackoverflow](https://bit.ly/3jnzXAw)
* [x] Populate MySQL Table
* [ ] Install Jenkins
* [x] set up Watchtower to pull Docker Images
* [x] Configure CI pipeline: local Development -> Github -> Linode Instance
