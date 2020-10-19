package com.ringodev.stocks.service.userdata;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Todo write sql script to initialize table
@Repository
public interface UserDataRepository extends JpaRepository<UserData, String> {
    UserData findByUsername(String username);
    UserData findByEmail(String email);
}
