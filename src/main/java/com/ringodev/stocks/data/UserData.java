package com.ringodev.stocks.data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UserData {


    @Id
    String username;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Position> positions;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public void addPosition(Position position) {
        if (positions == null) {
            positions = new ArrayList<>();
        }
        positions.add(position);
    }

    public boolean removePositionById(long id){
        //check if such position exists
        if(this.positions.stream().noneMatch(p -> p.getId() == id))return false;
        this.positions.removeIf(p -> p.getId() == id);
        return true;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "username='" + username + '\'' +
                ", positions=" + positions +
                '}';
    }
}
