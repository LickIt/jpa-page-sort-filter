package com.example.demo.projections;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize( as = UserSummary.class )
public interface UserSummary {
    
    String getUsername();
    
    @JsonIgnore
    String getFirstName();
    
    @JsonIgnore
    String getLastName();
    
    default String getName() {
        return getFirstName() + " " + getLastName();
    }
    
    String getCityName();
}
