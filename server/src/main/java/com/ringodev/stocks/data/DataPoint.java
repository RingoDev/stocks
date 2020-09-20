package com.ringodev.stocks.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.AUTO;

@Entity
public class DataPoint implements Comparable<DataPoint> {

    @Transient
    static SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd yyyy");

    @Id
    @GeneratedValue(strategy = AUTO)
    Long id;

    Date date;
    double open;
    double high;
    double low;
    double close;
    double adj_close;
    long volume;

    public DataPoint() {
    }

    public DataPoint(List<String> list) throws ParseException {

        // convert to european timezone
//        Calendar cal = Calendar.getInstance();
//
//        cal.setTime(dateFormatter.parse(list.get(0)));
//        cal.add(Calendar.DATE, 1);
//        this.date = cal.getTime();
        this.date = dateFormatter.parse(list.get(0));
        this.open = Double.parseDouble(list.get(1));
        this.high = Double.parseDouble(list.get(2));
        this.low = Double.parseDouble(list.get(3));
        this.close = Double.parseDouble(list.get(4));
        this.adj_close = Double.parseDouble(list.get(5));
        this.volume = Integer.parseInt(list.get(6));
    }

    @Override
    public String toString() {
        return "DataPoint{" +
                "date=" + date +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", adj_close=" + adj_close +
                ", volume=" + volume +
                '}';
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getAdj_close() {
        return adj_close;
    }

    public void setAdj_close(double adj_close) {
        this.adj_close = adj_close;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int compareTo(DataPoint o) {
        return -1 * this.date.compareTo(o.date);
    }
}
