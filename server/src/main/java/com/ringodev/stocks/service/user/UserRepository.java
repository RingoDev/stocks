package com.ringodev.stocks.service.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Todo connect to MySQL instance instead of JPA repo
// Todo write sql script to initialize table
@Repository
public interface UserRepository extends JpaRepository<UserImpl, Long> {
    UserImpl findByUsername(String username);
}
