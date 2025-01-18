package com.ringodev.stocks.data.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * contains a history of dates with some extra info
 */
public class DateObject {

    Date date;
    String fullWeekday;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFullWeekday() {
        return fullWeekday;
    }

    public void setFullWeekday(String fullWeekday) {
        this.fullWeekday = fullWeekday;
    }

    public String getShortWeekday() {
        return shortWeekday;
    }

    public void setShortWeekday(String shortWeekday) {
        this.shortWeekday = shortWeekday;
    }

    String shortWeekday;



    public DateObject(Date date){
        this.date = date;
        this.fullWeekday = new SimpleDateFormat("EEEE").format(date);
        this.shortWeekday = new SimpleDateFormat("E").format(date);
    }

}
