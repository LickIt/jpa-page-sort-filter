package com.example.demo.entities;

import java.util.List;

import javax.persistence.*;

@Entity
public class City {
    
    private int id;
    private String name;
    private String postCode;
    private List<User> users;
    
    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }
    
    public void setId( int id ) {
        this.id = id;
    }
    
    @Column
    public String getName() {
        return name;
    }
    
    public void setName( String name ) {
        this.name = name;
    }
    
    @OneToMany( mappedBy = "city" )
    public List<User> getUsers() {
        return users;
    }
    
    public void setUsers( List<User> users ) {
        this.users = users;
    }
    
    @Column( length = 8 )
    public String getPostCode() {
        return postCode;
    }
    
    public void setPostCode( String postCode ) {
        this.postCode = postCode;
    }
}
