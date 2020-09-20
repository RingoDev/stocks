package com.ringodev.stocks.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
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

    @JsonProperty("quantity")
    int quantity;

    @Transient
    double buyValue;

    @Transient
    double currentValue;

    public double getBuyValue() {
        return buyValue;
    }

    public void setBuyValue(double buyValue) {
        this.buyValue = buyValue;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }



    public Position(String stockRef, Date date, int quantity) {
        this.stockRef = stockRef;
        this.date = date;
        this.quantity = quantity;
    }

    public Position(){

    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

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
                ", quantity=" + quantity +
                ", buyValue=" + buyValue +
                ", currentValue=" + currentValue +
                '}';
    }
}
