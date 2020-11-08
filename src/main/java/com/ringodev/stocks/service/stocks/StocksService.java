package com.ringodev.stocks.service.stocks;

import com.ringodev.stocks.data.AlreadyExistsException;
import com.ringodev.stocks.data.DataPoint;
import com.ringodev.stocks.data.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class StocksService {

    Logger logger = LoggerFactory.getLogger(StocksService.class);
    private final StocksRepository repository;
    Environment env;


    @Autowired
    StocksService(StocksRepository repository, Environment environment) {
        this.repository = repository;
        this.env = environment;
    }

    List<String> getStockList() {
        return repository.findAll().stream().map(Stock::getName).collect(Collectors.toList());
    }


    public void insertStocks(String folder) {

        logger.info("Inserting Stocks from " + folder);

        File f = new File(folder);
        String[] pathNames = f.list();
        if (pathNames == null) {
            logger.error("Pathname to data wasn't correct: " + folder);
            return;
        }
        insertStocksFromFolder(folder, pathNames);
    }

    private void insertStocksFromFolder(String folder, String[] pathNames) {
        for (String pathname : pathNames) {
            String stockSign = pathname.substring(0, pathname.indexOf('.'));
            try {
                logger.info("Inserting " + stockSign + " from path: " + folder + pathname);
                insertStock(stockSign, folder + "/" + pathname);
            } catch (AlreadyExistsException e) {
                logger.info("Stock " + stockSign + " already existed in the DB and wasn't inserted");
            }
        }
    }


    public ResponseEntity<Object> insertStock(String stockSign, String path) throws AlreadyExistsException {

        if (stockExists(stockSign))
            throw new AlreadyExistsException("stock is inserted in the DB already");

        long time = System.currentTimeMillis();

        try  {
            Stock test = new Stock();
            readStockFromCSV(stockSign, path, test);
            repository.save(test);
            if (stockExists(stockSign))
                logger.info("Successfully inserted " + stockSign + " in " + (System.currentTimeMillis() - time) / 1000 + " seconds");
            else return new ResponseEntity<>(HttpStatus.CONFLICT);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ParseException | FileNotFoundException e) {
            e.printStackTrace();
            logger.info("Couldn't insert " + stockSign);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    private boolean stockExists(String stockSign) {
        return repository.findByName(stockSign) != null;
    }

    private void readStockFromCSV(String stockSign,String path, Stock test) throws ParseException, FileNotFoundException {
        boolean firstDone = false;
        Scanner scanner = new Scanner(new File(path));
        while (scanner.hasNextLine()) {
            List<String> l = getStringsFromLine(scanner.nextLine());
            if (!firstDone && l.get(0).equals("Date")) {
                firstDone = true;
                test.setName(stockSign);
            } else {
                test.addDataPoint(new DataPoint(l));
            }
        }
    }

    static List<String> getStringsFromLine(String line) {
        List<String> values = new ArrayList<>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }
}
