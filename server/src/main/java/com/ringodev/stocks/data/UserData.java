package com.ringodev.stocks.data;

import javax.persistence.*;
import java.util.List;

@Entity
public class UserData {

    @Id
    String username;

    @OneToMany(cascade = CascadeType.ALL)
    List<Position> positions;
}
