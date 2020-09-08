package com.ringodev.stocks.service.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.core.userdetails.User;

@RepositoryRestResource
public interface UserRepository extends MongoRepository<User, String> {

}
