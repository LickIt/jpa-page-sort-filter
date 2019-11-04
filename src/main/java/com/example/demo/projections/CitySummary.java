package com.example.demo.projections;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize( as = CitySummary.class )
public interface CitySummary {
    
    String getName();
    
    @Value( "#{target.users.size()}" )
    int getPopulation();
    
    @Value( "#{@cityServiceImpl.getPopulation(target)}" )
    int getPopulationFromBean();
    
    default String getInitials() {
        return getName().substring( 0, 3 ).toUpperCase();
    }
    
}
