package com.ringodev.stocks.data;


import java.text.SimpleDateFormat;
import java.util.Date;

public class CombinedPosition {
    Date date;
    double value;
    String weekday;

    public CombinedPosition(Date date) {
        this.date = date;
        this.value = 0;
        this.weekday = new SimpleDateFormat("E").format(date);
    }


    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public void addValue(double value){
        this.value += value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
