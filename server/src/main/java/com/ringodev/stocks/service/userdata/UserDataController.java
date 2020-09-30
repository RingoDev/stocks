package com.ringodev.stocks.service.userdata;

import com.ringodev.stocks.data.Position;
import com.ringodev.stocks.data.UserData;
import com.ringodev.stocks.data.UserStockData;
import com.ringodev.stocks.service.stocks.StocksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.security.Principal;

@RestController
@RequestMapping("api/user")
public class UserDataController {

    private final UserDataService userDataService;
    private final StocksService stocksService;

    @Autowired
    UserDataController(StocksService stocksService,UserDataService userDataService) {
        this.userDataService = userDataService;
        this.stocksService = stocksService;
    }

    // gets the userdata of the user
    @GetMapping("/data")
    public ResponseEntity<Object> getData(Principal principal) {
        UserData data = userDataService.getUserData(principal);
        if (data == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        try{
            data = userDataService.enrichUserData(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(data);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/stock")
    public ResponseEntity<Object> getStockData(Principal principal) {
        UserData data = userDataService.getUserData(principal);
        if (data == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        UserStockData stockData = null;
        try{
            stockData = userDataService.getUserStockData(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(stockData, HttpStatus.OK);
    }

    // gets the userdata of the user
    @PostMapping("/addPosition")
    public ResponseEntity<Object> addPosition(@RequestBody Position position, Principal principal) {
        userDataService.addPosition(position, principal.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
