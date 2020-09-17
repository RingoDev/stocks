package com.ringodev.stocks.service.stocks;

import com.ringodev.stocks.data.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StocksService {

    private final StocksRepository repository;

    @Autowired
    StocksService(StocksRepository repository) {
        this.repository = repository;
    }

    List<String> getStockList(){
        return repository.findAll().stream().map(Stock::getName).collect(Collectors.toList());
    }
}
