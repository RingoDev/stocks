package com.ringodev.stocks.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsManager implements UserDetailsManager {
    UserRepository userRepository;

    @Autowired
    MyUserDetailsManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(UserDetails userDetails) {
        if(!userExists(userDetails.getUsername())){
            userRepository.save(new UserEntity(userDetails.getUsername(),new BCryptPasswordEncoder().encode(userDetails.getPassword())));
        }
    }

    @Override
    public void updateUser(UserDetails userDetails) {

    }

    @Override
    public void deleteUser(String s) {

    }

    @Override
    public void changePassword(String s, String s1) {

    }

    @Override
    public boolean userExists(String s) {
        return userRepository.findByUsername(s) != null;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(s);
        if (user == null) throw new UsernameNotFoundException("Couldn't find Username");
        else return user.toUserDetails();

    }
}
