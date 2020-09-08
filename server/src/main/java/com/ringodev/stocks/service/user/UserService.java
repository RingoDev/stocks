package com.ringodev.stocks.service.user;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    UserRepository userRepository;

    /**
     * @param user the user to insert into the DB
     * @return true if the user was inserted successfully false if a user with that username exists already in the DB
     */
    public boolean addUser(User user) {
        if (userRepository.findById(user.getUsername()).isPresent()) return false;
        userRepository.insert(user);
        return userRepository.findById(user.getUsername()).isPresent();

    }

    /**
     * @param user the user to delete from the DB
     * @return true if the user was deleted successfully false if a user with that username doesn't exists in the DB
     */
    public boolean removeUser(User user) {
        if (userRepository.findById(user.getUsername()).isEmpty()) return false;
        userRepository.deleteById(user.getUsername());
        return userRepository.findById(user.getUsername()).isPresent();
    }


}
