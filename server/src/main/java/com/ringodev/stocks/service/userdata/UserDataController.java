package com.ringodev.stocks.service.userdata;

import com.ringodev.stocks.data.Position;
import com.ringodev.stocks.data.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/user")
public class UserDataController {

    private final UserDataService userDataService;

    @Autowired
    UserDataController(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    // gets the userdata of the user
    @GetMapping("/data")
    public ResponseEntity<Object> getData(Principal principal) {
        UserData data = userDataService.getUserData(principal);
        if (data == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(data, HttpStatus.OK);
    }

    // gets the userdata of the user
    @PostMapping("/addPosition")
    public ResponseEntity<Object> addPosition(@RequestBody Position position, Principal principal) {
        userDataService.addPosition(position, principal.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
