package com.example.demo.repositories;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;

import com.example.demo.BaseTest;
import com.example.demo.entities.City;
import com.example.demo.projections.CitySummary;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CityRepositoryTest extends BaseTest {
    
    @Autowired CityRepository cityRepository;
    
    @Test
    void testQueryMethod() {
        Page<City> citiesPage = cityRepository.findAllByNameContainsOrderByNameDesc( PageRequest.of( 0, 1 ), "o" );
        
        assertEquals( 2, citiesPage.getTotalElements() );
        assertEquals( 2, citiesPage.getTotalPages() );
        assertEquals( 1, citiesPage.getNumberOfElements() );
        assertEquals( "Veliko Tarnovo", citiesPage.getContent().get( 0 ).getName() );
    }
    
    @Test
    void testQueryMethodWithJPQL() {
        List<City> bigCities = cityRepository.findAllWithAtLeastUsers( 2 );
        assertEquals( 1, bigCities.size() );
    }
    
    @Test
    void testProjectionMethod() {
        CitySummary citySummary = cityRepository.getSummaryById( 1 );
        assertEquals( 3, citySummary.getPopulation() );
        assertEquals( 3, citySummary.getPopulationFromBean() );
        assertEquals( "SOF", citySummary.getInitials() );
    }
}