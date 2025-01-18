# Stocks-WebApp

Stocks Application written with Java Spring Boot and React frontend

![Preview_1](https://d33wubrfki0l68.cloudfront.net/5fa8226bd0aad70008c457e9/screenshot.png)




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
