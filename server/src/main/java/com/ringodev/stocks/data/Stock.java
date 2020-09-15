package com.ringodev.stocks.data;

import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import java.util.Set;
import java.util.TreeSet;

import static javax.persistence.GenerationType.AUTO;

@Entity
public class Stock {

    // id for internal calling of other Services
    @Id
    @GeneratedValue(strategy = AUTO)
    Long id;

    String name;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @SortNatural
    Set<DataPoint> history  = new TreeSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<DataPoint> getHistory() {
        return history;
    }

    public void setHistory(Set<DataPoint> history) {
        this.history = history;
    }

    public void addDataPoint(DataPoint dp){
        this.history.add(dp);
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", history='" + history+ '\'' +
                '}';
    }
}
