package com.ringodev.stocks.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsManagerImpl implements UserDetailsManager {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    UserDetailsManagerImpl(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void createUser(UserDetails userDetails) {
        if(!userExists(userDetails.getUsername())){
            userRepository.save(new UserImpl(userDetails.getUsername(),passwordEncoder.encode(userDetails.getPassword())));
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
        UserImpl user = userRepository.findByUsername(s);
        if (user == null) throw new UsernameNotFoundException("Couldn't find Username");
        else return user.toUserDetails();

    }
}
