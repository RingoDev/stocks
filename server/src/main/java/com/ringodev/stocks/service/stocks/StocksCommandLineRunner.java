package com.ringodev.stocks.service.stocks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StocksCommandLineRunner implements CommandLineRunner {

    private final StocksRepository repository;

    @Autowired
    StocksCommandLineRunner(StocksRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {




    }


}
