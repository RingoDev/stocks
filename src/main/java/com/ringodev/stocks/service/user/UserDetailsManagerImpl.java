package com.ringodev.stocks.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsManagerImpl implements UserDetailsManager {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(UserDetailsManagerImpl.class);
    private AuthenticationManager authenticationManager;

    // Workaround for circular dependency injection
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void verifyMail(String username){
        if(userExists(username)){
            userRepository.findByUsername(username).setEnabled(true);
        }
        else throw new EntityNotFoundException("Username didn't belong to a user");
    }


    @Autowired
    UserDetailsManagerImpl( UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates a Guest-account with a random username
     * TODO
     *
     * @return the created User object
     */
    public UserImpl createGuest() {
        // todo
        return new UserImpl("", "", new ArrayList<>());
    }

    /**
     * saves a new user to the database
     *
     * @param userDetails the userDetails object to save
     */
    @Override
    public void createUser(UserDetails userDetails) {
        if (!userExists(userDetails.getUsername())) {
            logger.info("Saving a User to the DB with username: "+userDetails.getUsername());
            userRepository.save(UserImpl.from(userDetails));
            logger.info("Saved User: "+ userDetails.getUsername());
        }
    }

    /**
     * @return all users in the db
     */
    public List<UserImpl> getAll() {
        return userRepository.findAll();
    }

    /**
     * Checks if a user with a certain username exists in the db
     *
     * @param username the username of a user
     * @return true if a user with this username exists already
     */
    @Override
    public boolean userExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    /**
     * Loads a User from the DB
     *
     * @param username the name of the user
     * @return the found user
     * @throws UsernameNotFoundException if no user with the specific username is found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserImpl user = userRepository.findByUsername(username);
        Assert.notNull(user, "Couldn't find User by Username");
        return user;
    }

    /**
     * Updates a user
     *
     * @param userDetails the new User Information
     */
    @Override
    public void updateUser(UserDetails userDetails) {
        userRepository.findByUsername(userDetails.getUsername()).setFromUserDetails(userDetails);
        userRepository.flush();
    }

    /**
     * Deletes a User from the DB
     * @param username the username of the user
     */

    @Override
    public void deleteUser(String username) {
        userRepository.deleteById(userRepository.findByUsername(username).getId());
        userRepository.flush();
    }

    /**
     * Changes the Password of the current User in Security Context
     * authenticates the User again with the old Password and sets the new Password afterwards
     *
     * @param oldPassword the old Password
     * @param newPassword the new Password
     */
    @Override
    public void changePassword(String oldPassword, String newPassword) {
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userImpl = this.loadUserByUsername(user.getName());
        Assert.notNull(user, "Can't change password as no Authentication object found in context for current user.");
        Assert.notNull(userImpl, "Can't change password of non existent user.");

        this.logger.debug("Reauthenticating user '" + userImpl.getUsername() + "' for password change request.");

        if (this.authenticationManager != null) {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userImpl.getUsername(), oldPassword));
        } else {
            this.logger.debug("No authentication manager set. Password won't be re-checked.");
        }
        this.logger.debug("Changing password for user '" + userImpl.getUsername() + "'");
        SecurityContextHolder.getContext().setAuthentication(this.createNewAuthentication(user, newPassword));
    }

    protected Authentication createNewAuthentication(Authentication currentAuth, String newPassword) {
        UserDetails user = this.loadUserByUsername(currentAuth.getName());
        UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        newAuthentication.setDetails(currentAuth.getDetails());
        return newAuthentication;
    }

    /**
     * Deletes all the Users from the DB
     */
    public void clearALL(){
        userRepository.deleteAll();
        userRepository.flush();
    }


}
