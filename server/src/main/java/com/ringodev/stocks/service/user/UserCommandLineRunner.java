package com.ringodev.stocks.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Used to insert some custom users for testing
 */
@Component
public class UserCommandLineRunner implements CommandLineRunner {

    private final UserRepository repository;
    PasswordEncoder passwordEncoder;

    @Autowired
    UserCommandLineRunner(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        repository.save(new UserImpl("admin", passwordEncoder.encode("password"),Role.ROLE_ADMIN));
        repository.save(new UserImpl("user", passwordEncoder.encode("password"),Role.ROLE_USER));
        for (UserImpl user : repository.findAll()) {
            System.out.println(user.toUserDetails());
        }
    }
}
