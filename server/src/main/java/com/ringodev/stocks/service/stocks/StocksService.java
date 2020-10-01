package com.ringodev.stocks.service.stocks;

import com.ringodev.stocks.data.AlreadyExistsException;
import com.ringodev.stocks.data.DataPoint;
import com.ringodev.stocks.data.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${DOCKER_ENV}")
    private String docker;
    // check if run in docker container
    //

    @Autowired
    StocksService(StocksRepository repository) {
        this.repository = repository;
    }

    List<String> getStockList() {
        return repository.findAll().stream().map(Stock::getName).collect(Collectors.toList());
    }


    public void insertStocks(String folder) {

        logger.info("Inserting Stocks from " + folder);

        if(docker.equals("true"))folder = "/home/" + folder;

        File f = new File(folder);
        String[] pathNames = f.list();
        if(pathNames == null){
            logger.error("Pathname to data wasn't correct: "+ folder);
            return;
        }
        for(String pathname : pathNames){
            String stockSign = pathname.substring(0,pathname.indexOf('.'));
            try{
                logger.info("Inserting "+stockSign+" from path: "+folder + pathname);
                insertStock(stockSign,folder+"/"+pathname);
            }catch (AlreadyExistsException e){
                logger.info("Stock "+stockSign+ " already existed in the DB and wasn't inserted");
            }
        }
    }


    public ResponseEntity<Object> insertStock(String stockSign, String path) throws AlreadyExistsException {
        if (repository.findByName(stockSign) != null)
            throw new AlreadyExistsException("stock is inserted in the DB already");

        long time = System.currentTimeMillis();
        Stock test = new Stock();

        boolean firstDone = false;
        // read in csv file
        try (
                Scanner scanner = new Scanner(new File(path))) {

            while (scanner.hasNextLine()) {
                List<String> l = getStringsFromLine(scanner.nextLine());
                if (!firstDone && l.get(0).equals("Date")) {
                    firstDone = true;
                    test.setName(stockSign);
                } else {
                    test.addDataPoint(new DataPoint(l));
                }
            }
        } catch (ParseException | FileNotFoundException e) {
            e.printStackTrace();
            logger.info("Couldn't insert " + stockSign);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        repository.save(test);
        if (repository.findByName(stockSign) != null) logger.info("Succesfully inserted " + stockSign + " in " + (System.currentTimeMillis() - time)/1000 + " seconds" );
        return new ResponseEntity<>(HttpStatus.OK);
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
