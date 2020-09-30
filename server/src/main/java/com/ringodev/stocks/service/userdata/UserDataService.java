package com.ringodev.stocks.service.userdata;

import com.ringodev.stocks.data.*;
import com.ringodev.stocks.service.stocks.StocksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.security.Principal;
import java.util.*;

@Service
public class UserDataService {

    private final UserDataRepository userDataRepository;
    private final StocksRepository stocksRepository;

    @Autowired
    UserDataService(UserDataRepository userDataRepository, StocksRepository stocksRepository) {
        this.userDataRepository = userDataRepository;
        this.stocksRepository = stocksRepository;
    }

    UserData getUserData(Principal principal) {
        return userDataRepository.findByUsername(principal.getName());
    }

    public UserData getUserData(String username) {
        return userDataRepository.findByUsername(username);
    }

    public void addPosition(Position position, String username) {
        UserData data = userDataRepository.findByUsername(username);
        if (data == null) {
            data = new UserData();
        }
        data.addPosition(position);
        userDataRepository.save(data);
        System.out.println("added Userdata");
        System.out.println(userDataRepository.findByUsername(username));
    }

    /**
     * is called when a new user signs up and creates the userDataObject for this user
     *
     * @param user the user to create the Data object for
     */
    public void createUserData(User user) throws AlreadyExistsException {
        UserData data = userDataRepository.findByUsername(user.getUsername());
        if (data != null) throw new AlreadyExistsException("UserData Object existed already");
        data = new UserData();
        data.setUsername(user.getUsername());
        data.setPositions(new ArrayList<>());
        userDataRepository.save(data);
    }

    public double calculateBuyValue(Position position, Stock stock) throws FileNotFoundException {
        DataPoint dp = getClosestDataPoint(position.getDate(), stock.getHistory());
        if (dp == null) throw new RuntimeException("Couldn't get a DataPoint that closest matches the date");
        return dp.getClose() * position.getQuantity();
    }

    public double calculateCurrentValue(Position position, Stock stock) {
        DataPoint dp = getClosestDataPoint(new Date(), stock.getHistory());
        if (dp == null) throw new RuntimeException("Couldn't get a DataPoint that closest matches the date");
        return dp.getClose() * position.getQuantity();
    }

    static DataPoint getClosestDataPoint(Date date, Set<DataPoint> history) {
        DataPoint closest = null;
        long ms = date.getTime();

        for (DataPoint dp : history) {
            // +1 so it prefers earlier date
            long diff = Math.abs(dp.getDate().getTime() - date.getTime() + 1);
            if (diff < ms) {
                ms = diff;
                closest = dp;
            }
        }
        return closest;
    }

    public UserData enrichUserData(UserData userData) throws FileNotFoundException {
        List<Position> positions = userData.getPositions();


        for (Position position : positions) {
            Stock stock = stocksRepository.findByName(position.getStockRef());
            if (stock == null) throw new FileNotFoundException();
            position.setBuyValue(calculateBuyValue(position, stock));
            position.setCurrentValue(calculateCurrentValue(position, stock));
        }

        userData.setCombinedPositions(combinePositions(userData));

        return userData;
    }

    public UserStockData getUserStockData(UserData userData) throws FileNotFoundException {
        List<Date> dates = initDates();
        return new UserStockData(dates, enrichPositions(userData, dates));

    }

    // get all Positions as one DataObject

    private List<CombinedPosition> combinePositions(UserData userData) throws FileNotFoundException {
        List<CombinedPosition> list = new ArrayList<>();

        // initialize 28 long array with dates excluding saturdays and sundays
        List<Date> dates = initDates();
        for (Date date : dates) {
            CombinedPosition combined = new CombinedPosition(date);
            for (Position position : userData.getPositions()) {
                Stock stock = stocksRepository.findByName(position.getStockRef());
                if (stock == null) throw new FileNotFoundException();
                DataPoint dp = getClosestDataPoint(date, stock.getHistory());
                //System.out.println(date + " Closest DataPoint:" + dp);
                if (dp == null) throw new RuntimeException("Couldn't get a DataPoint that closest matches the date");
                combined.addValue(dp.getClose() * position.getQuantity());
            }
            list.add(combined);
        }
        return list;
    }

    public List<Position> enrichPositions(UserData userData, List<Date> dates) throws FileNotFoundException {
        List<Position> list = userData.getPositions();
        for (Position position : list) {

            Stock stock = stocksRepository.findByName(position.getStockRef());
            if (stock == null) throw new FileNotFoundException();

            MonthHistory hist = new MonthHistory(position.getStockRef());
            for (Date date : dates) {
                DataPoint dp = getClosestDataPoint(date, stock.getHistory());
                if (dp == null) throw new RuntimeException("Couldn't get a DataPoint that closest matches the date");
                hist.addDataPoint(dp,position.getQuantity());
            }

            position.setBuyValue(calculateBuyValue(position, stock));
            position.setCurrentValue(calculateCurrentValue(position, stock));
            position.setHistory(hist);
        }
        return list;
    }


    private static List<Date> initDates() {
        List<Date> list = new ArrayList<>();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        while (list.size() < 28) {

            if (!(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
                list.add(cal.getTime());
            }
            cal.add(Calendar.DATE, -1);
        }
        return list;
    }

}
