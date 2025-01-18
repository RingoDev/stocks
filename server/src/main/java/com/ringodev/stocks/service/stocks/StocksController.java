package com.ringodev.stocks.service.stocks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
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

    Logger logger = LoggerFactory.getLogger(StocksController.class);


    private final StocksService stocksService;
    private final TaskExecutor taskExecutor;

    @Autowired
    StocksController(StocksService stocksService, TaskExecutor taskExecutor) {

        this.stocksService = stocksService;
        this.taskExecutor = taskExecutor;

    }

    // tries to insert a csv file
    @PostMapping("/insert/nyse")
    public ResponseEntity<Object> insertCSV(HttpServletRequest request) {
        if (!request.isUserInRole("ADMIN")) {
            System.out.println("Access to insert was denied");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        logger.info("File inserting access was granted");
        // should start in new thread
        taskExecutor.execute(() -> stocksService.insertStocks("/home/data/stock_data_nyse/"));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<Object> stockList() {
        return new ResponseEntity<>(stocksService.getStockList(), HttpStatus.OK);
    }


}
