package com.ringodev.stocks.service.stocks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/stocks")
public class StocksController {

    private final StocksRepository repository;
    private final StocksService stocksService;

    @Autowired
    StocksController(StocksRepository repository, StocksService stocksService) {
        this.repository = repository;
        this.stocksService = stocksService;
    }

    // tries to insert a csv file
    @PostMapping("/insert")
    public ResponseEntity<Object> insertCSV(HttpServletRequest request) {
        if (!request.isUserInRole("ROLE_ADMIN")) {
            System.out.println("Access to insert was denied");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        System.out.println("File inserting access was granted");
        String name = request.getParameter("name");
        return stocksService.insertStock(name);
    }

    @GetMapping("/list")
    public ResponseEntity<Object> stockList() {
        return new ResponseEntity<>(stocksService.getStockList(), HttpStatus.OK);
    }


}
