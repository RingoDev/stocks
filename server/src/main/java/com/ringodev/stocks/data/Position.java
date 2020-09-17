package com.ringodev.stocks.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

import static javax.persistence.GenerationType.AUTO;

@Entity
public class Position {

    @Id
    @GeneratedValue(strategy = AUTO)
    Long id;

    @JsonProperty("stock")
    String stockRef;

    @JsonProperty("date")
    Date date;
    double price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double prize) {
        this.price = prize;
    }
    public String getStockRef() {
        return stockRef;
    }

    public void setStockRef(String stockRef) {
        this.stockRef = stockRef;
    }

    @Override
    public String toString() {
        return "Position{" +
                "id=" + id +
                ", stockRef='" + stockRef + '\'' +
                ", date=" + date +
                ", price=" + price +
                '}';
    }
}
