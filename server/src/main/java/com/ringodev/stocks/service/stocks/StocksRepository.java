package com.ringodev.stocks.service.stocks;

import com.ringodev.stocks.data.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Todo write Python Script to setup table and insert StockData
@Repository
public interface StocksRepository extends JpaRepository<Stock, Long> {
    Stock findByName(String username);
}
