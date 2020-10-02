package com.ringodev.stocks.data;

import com.ringodev.stocks.data.util.DateObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserStockData {

    List<DateObject> validDates;
    List<Position> positions;

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }


    public UserStockData(){

    }

    public UserStockData(List<Date> dates,List<Position> positions){
        this.positions = positions;
        this.validDates = new ArrayList<>();
        for(Date date : dates){
            this.validDates.add(new DateObject(date));
        }
    }

    public List<DateObject> getValidDates() {
        return validDates;
    }

    public void setValidDates(List<DateObject> validDates) {
        this.validDates = validDates;
    }

}
