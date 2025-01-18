package com.ringodev.stocks.service.userdata;

import com.ringodev.stocks.data.Position;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UserData {


    @Column()
    private String firstname;
    @Column()
    private String lastname;
    @Column(unique = true)
    private String email;
    @Id
    String username;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Position> positions;


    public UserData() {
    }

    UserData(String username){
        this.username = username;
        this.positions = new ArrayList<>();
    }


    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public boolean removePositionById(long id){
        //check if such position exists
        if(this.positions.stream().noneMatch(p -> p.getId() == id))return false;
        this.positions.removeIf(p -> p.getId() == id);
        return true;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", positions=" + positions +
                '}';
    }
}
