package com.example.demo.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class User {
    
    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private City city;
    
    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }
    
    public void setId( int id ) {
        this.id = id;
    }
    
    @Column( nullable = false )
    @NotNull
    public String getUsername() {
        return username;
    }
    
    public void setUsername( String username ) {
        this.username = username;
    }
    
    @Column( nullable = false )
    @NotNull
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName( String firstName ) {
        this.firstName = firstName;
    }
    
    @Column
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName( String lastName ) {
        this.lastName = lastName;
    }
    
    @ManyToOne
    @JoinColumn
    @NotNull
    public City getCity() {
        return city;
    }
    
    public void setCity( City city ) {
        this.city = city;
    }
}
