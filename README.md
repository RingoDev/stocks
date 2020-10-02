# Stocks-WebApp

## Backend

### Development

* [x] Decide on the storage - Sequel or nonSequel
  * [x] MySQL  
  * [ ] MongoDB

#### set up MySQL DB

* [x] Install MySQL DB on local machine

```
docker run --name=mysql1 -p 6603:3306 -d mysql/mysql-server
docker logs mysql1 2>&1 | grep GENERATED
docker exec -it mysql1 mysql -uroot -p
```
In Mysql instance:
create spring user
```
mysql> create database db_example; -- Creates the new database
mysql> create user 'springuser'@'%' identified by 'ThePassword'; -- Creates the user
mysql> grant all on db_example.* to 'springuser'@'%'; -- Gives all privileges to the new user on the newly created database
```

creating spring boot docker image

```
$ ./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=springio/gs-spring-boot-docker
```

running the mysql container

running the spring container 

linking mysql3 to mysql3
setting MYSQL_HOST variable to mysql3
porting mapping from 8085 to 8085

```
docker run --name=spring2 --link mysql3 -e MYSQL_HOST=mysql3 -p 8085:8085 -t springio/gs-spring-boot-docker
```

building spring boot image with Docker file

creating a fat jar with cmd:

```
./mwnw package

```


then unpacking fat jar with 
```
mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)
```

creating Dockerfile with split dependencies

```
FROM openjdk:14-jdk-slim
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.ringodev.stocks.StocksApplication"]
```

building docker image:

`
docker build --rm -t ringodev/spring-docker-stocks .fde
`

to mount the data so java-spring can access it and insert into db add

`
-v /path-to-data-on-host/:/home/data/
`
```
docker run --name=springTest \
--link mysql3 \
-v /home/ringo/Documents/IntellijProjects/Stocks/server/data/:/home/data/ \
-e MYSQL_HOST=mysql3 \
-e SPRING_PROFILES_ACTIVE=prod \
-p 8085:8085 \
-t ringodev/spring-stocks
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


* [ ] Install Docker
* [ ] Install MySQL DockerImage
* [ ] Install Spring-Boot DockerImage
  * [ ] Set Memory constraints
* [ ] give Spring Boot access to Stock .csv files -> [stackoverflow](https://bit.ly/3jnzXAw)
* [ ] Populate MySQL Table
* [ ] Install Jenkins
* [ ] Configure CI pipeline: local Development -> Github -> Linode Instance
