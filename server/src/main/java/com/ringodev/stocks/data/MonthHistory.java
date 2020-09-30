package com.ringodev.stocks.data;

import java.util.ArrayList;
import java.util.List;

public class MonthHistory {

    public List<Double> getValues() {
        return values;
    }

    public void setValues(List<Double> values) {
        this.values = values;
    }

    public String getStockSign() {
        return stockSign;
    }

    public void setStockSign(String stockSign) {
        this.stockSign = stockSign;
    }

    public MonthHistory(){

    }

    List<Double> values;
    String stockSign;




    public MonthHistory(String stockSign){
        this.stockSign = stockSign;
        values = new ArrayList<>();

    }


    public void addDataPoint(DataPoint dp,int quantity){
        values.add(dp.close*quantity);
    }
}
