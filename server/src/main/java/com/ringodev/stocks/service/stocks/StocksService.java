package com.ringodev.stocks.service.stocks;

import com.ringodev.stocks.data.DataPoint;
import com.ringodev.stocks.data.Stock;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final StocksRepository repository;

    @Autowired
    StocksService(StocksRepository repository) {
        this.repository = repository;
    }

    List<String> getStockList() {
        return repository.findAll().stream().map(Stock::getName).collect(Collectors.toList());
    }

    public ResponseEntity<Object> insertStock(String name) {
        Stock test = new Stock();

        boolean firstDone = false;
        // read in csv file
        try (
                Scanner scanner = new Scanner(new File(name + ".csv"))) {

            while (scanner.hasNextLine()) {
                List<String> l = getStringsFromLine(scanner.nextLine());
                if (!firstDone && l.get(0).equals("Date")) {
                    firstDone = true;
                    test.setName(name);
                } else {
                    test.addDataPoint(new DataPoint(l));
                }
            }
        } catch (ParseException | FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Couldn't insert " + name);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        repository.save(test);
        if (repository.findByName(name) != null) System.out.println("Succesfully inserted " + name);
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
