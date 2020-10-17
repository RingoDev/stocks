package com.ringodev.stocks.service.user;

import com.ringodev.stocks.service.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsManagerImpl implements UserDetailsManager {
    private final UserRepository userRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    UserDetailsManagerImpl(MailService mailService ,UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    @Override
    public void createUser(UserDetails userDetails) {
        if (!userExists(userDetails.getUsername())) {
            userRepository.save(new UserImpl((User) userDetails));
        }
    }

    public User createGuest(){
        // todo
        return new User("","",new ArrayList<>());
    }

    public List<UserImpl> getAll(){
        return userRepository.findAll();
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


    public void verifyMail(String mail){


        this.mailService.sendVerificationMessage(mail);
    }
}
