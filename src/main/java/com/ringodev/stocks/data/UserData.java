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

    @Transient
    List<CombinedPosition> combinedPositions;

    public List<CombinedPosition> getCombinedPositions() {
        return combinedPositions;
    }

    public void setCombinedPositions(List<CombinedPosition> combinedPositions) {
        this.combinedPositions = combinedPositions;
    }

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

    @Override
    public String toString() {
        return "UserData{" +
                "username='" + username + '\'' +
                ", positions=" + positions +
                '}';
    }
}