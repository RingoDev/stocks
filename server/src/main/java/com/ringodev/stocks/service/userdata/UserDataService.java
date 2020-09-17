package com.ringodev.stocks.service.userdata;

import com.ringodev.stocks.data.AlreadyExistsException;
import com.ringodev.stocks.data.Position;
import com.ringodev.stocks.data.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;

@Service
public class UserDataService {

    private final UserDataRepository repository;

    @Autowired
    UserDataService(UserDataRepository repository) {
        this.repository = repository;
    }

    UserData getUserData(Principal principal) {
        return repository.findByUsername(principal.getName());
    }

    public UserData getUserData(String username) {
        return repository.findByUsername(username);
    }

    void addPosition(Position position, Principal principal) {
        UserData data = repository.findByUsername(principal.getName());
        if (data == null) {
            data = new UserData();
        }
        data.addPosition(position);
        repository.save(data);
        System.out.println("added Userdata");
        System.out.println(repository.findByUsername(principal.getName()));
    }

    /**
     * is called when a new user signs up and creates the userDataObject for this user
     * @param user the user to create the Data object for
     */
    public void createUserData(User user) throws AlreadyExistsException {
        UserData data = repository.findByUsername(user.getUsername());
        if(data != null) throw new AlreadyExistsException("UserData Object existed already");
        data = new UserData();
        data.setUsername(user.getUsername());
        data.setPositions(new ArrayList<>());
        repository.save(data);
    }
}
