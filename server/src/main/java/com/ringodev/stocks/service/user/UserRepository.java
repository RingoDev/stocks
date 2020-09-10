package com.ringodev.stocks.service.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserImpl, Long> {

    UserImpl findByUsername(String username);
}
