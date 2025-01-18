package com.ringodev.stocks.service.userdata;

import com.ringodev.stocks.data.*;
import com.ringodev.stocks.service.stocks.StocksRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.EntityNotFoundException;
import java.io.FileNotFoundException;
import java.security.Principal;
import java.util.*;

@Service
public class UserDataService {

    private final Logger logger = LoggerFactory.getLogger(UserDataService.class);
    private final UserDataRepository userDataRepository;
    private final StocksRepository stocksRepository;

    @Autowired
    UserDataService(UserDataRepository userDataRepository, StocksRepository stocksRepository) {
        this.userDataRepository = userDataRepository;
        this.stocksRepository = stocksRepository;
    }

    public void clearALL() {
        userDataRepository.deleteAll();
        userDataRepository.flush();
    }

    public List<UserData> getAllUserData() {
        return userDataRepository.findAll();
    }

    UserData getUserData(Principal principal) {
        return userDataRepository.findByUsername(principal.getName());
    }

    public UserData getUserData(String username) {
        return userDataRepository.findByUsername(username);
    }

    public void addPosition(Position position, String username) {
        UserData data = userDataRepository.findByUsername(username);
        Assert.notNull(data, "UserData cannot be null");


        // check if stock exists in DB

        if (stocksRepository.findByName(position.getStockRef()) == null) {
            logger.warn("Positions for Stock: " + position.getStockRef() + " cant be added because the stock doesn't exist.");
            return;
        }
        data.addPosition(position);
        userDataRepository.save(data);
        logger.info("added Userdata: " + userDataRepository.findByUsername(username).toString());
    }

    /**
     * removes a Position from the specific User's Userdata
     *
     * @param id       the id of the position
     * @param username the name of the user
     */
    public void removePosition(long id, String username) {
        UserData data = userDataRepository.findByUsername(username);
        logger.info("removing Position with ID: " + id);
        if (!data.removePositionById(id)) throw new EntityNotFoundException();
        userDataRepository.flush();
    }

    public boolean deleteUserData(String username) {
        if (!UserDataExistsWithUsername(username)) return false;
        else userDataRepository.deleteById(username);
        return true;
    }

    /**
     * is called when a new user signs up and creates the userDataObject for this user
     *
     * @param userDetails the user to create the Data object for
     */
    public void createUserData(UserDetails userDetails) throws AlreadyExistsException {
        if (UserDataExistsWithUsername(userDetails.getUsername()))
            throw new UsernameAlreadyExistsException("UserData with username exists already");
        userDataRepository.saveAndFlush(new UserData(userDetails.getUsername()));
    }

    public boolean UserDataExistsWithUsername(String username) {
        return userDataRepository.findByUsername(username) != null;
    }

    public boolean UserDataExistsWithEmail(String email) {
        return userDataRepository.findByEmail(email) != null;
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

//    public UserData enrichUserData(UserData userData) throws FileNotFoundException {
//        List<Position> positions = userData.getPositions();
//
//        for (Position position : positions) {
//            Stock stock = stocksRepository.findByName(position.getStockRef());
//            if (stock == null) throw new FileNotFoundException();
//            position.setBuyValue(calculateBuyValue(position, stock));
//            position.setCurrentValue(calculateCurrentValue(position, stock));
//        }
//        return userData;
//    }

    public UserStockData getUserStockData(UserData userData) throws FileNotFoundException {
        int amountOfDays = 28;
        List<Date> dates = initDates(amountOfDays);
        return new UserStockData(dates, addInformationToPositions(userData.getPositions(), dates));

    }

    public List<Position> addInformationToPositions(List<Position> list, List<Date> dates) throws FileNotFoundException {

        for (Position position : list) {

            Stock stock = checkStockExists(position);

            MonthHistory hist = addDataPointsToHistory(dates, position, stock);

            position.setBuyValue(calculateBuyValue(position, stock));
            position.setCurrentValue(calculateCurrentValue(position, stock));
            position.setHistory(hist);
        }
        return list;
    }

    private Stock checkStockExists(Position position) throws FileNotFoundException {
        Stock stock = stocksRepository.findByName(position.getStockRef());
        if (stock == null) throw new FileNotFoundException();
        return stock;
    }

    private MonthHistory addDataPointsToHistory(List<Date> dates, Position position, Stock stock) {
        MonthHistory hist = new MonthHistory(position.getStockRef());
        for (Date date : dates) {
            DataPoint dp = getClosestDataPoint(date, stock.getHistory());
            if (dp == null) throw new RuntimeException("Couldn't get a DataPoint that closest matches the date");
            hist.addDataPoint(dp, position.getQuantity());
        }
        return hist;
    }


    private static List<Date> initDates(int days) {
        List<Date> list = new ArrayList<>();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        while (list.size() < days) {

            if (!(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
                list.add(cal.getTime());
            }
            cal.add(Calendar.DATE, -1);
        }
        return list;
    }

    public String getUsernameFromEmail(String email) {
        logger.info("Getting Username from UserData repository for mail: " + email);
        UserData data = userDataRepository.findByEmail(email);
        Assert.notNull(data, "Couldn't find UserData of email: " + email);
        return data.getUsername();
    }

    public void setVerifiedEmail(String username, String email) {
        UserData user = userDataRepository.findByUsername(username);
        user.setEmail(email);
        userDataRepository.saveAndFlush(user);
        logger.info("Added Email " + email + " to UserData of User: " + username);
    }
}
