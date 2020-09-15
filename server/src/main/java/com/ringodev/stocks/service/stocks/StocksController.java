package com.ringodev.stocks.service.stocks;

import com.ringodev.stocks.data.DataPoint;
import com.ringodev.stocks.data.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RestController
@RequestMapping("stocks")
public class StocksController {

    private final StocksRepository repository;

    @Autowired
    StocksController(StocksRepository repository) {
        this.repository = repository;
    }

    // tries to signup a new user
    @PostMapping("/insert")
    public void insertCSV(HttpServletRequest request) throws FileNotFoundException {
        String name = request.getParameter("name");
        Stock test = new Stock();

        boolean firstDone = false;
        // read in csv file
        try (Scanner scanner = new Scanner(new File(name+".csv"));) {

            while (scanner.hasNextLine()) {
                List<String> l = getStringsFromLine(scanner.nextLine());
                if(!firstDone && l.get(0).equals("Date")){
                    firstDone = true;
                    test.setName(name);
                }


                else {
                    test.addDataPoint(new DataPoint(l));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        repository.save(test);
        System.out.println(repository.findById((long)1).orElseThrow());
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
