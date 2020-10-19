package com.ringodev.stocks.service.userdata;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, String> {
    UserData findByUsername(String username);

    @Query("SELECT u FROM UserData u WHERE u.email = :email")
    UserData findByEmail(@Param("email")String email);
}
