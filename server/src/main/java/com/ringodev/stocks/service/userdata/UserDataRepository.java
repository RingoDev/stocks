package com.ringodev.stocks.service.userdata;

import com.ringodev.stocks.data.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, String> {
    UserData findByUsername(String username);
}
