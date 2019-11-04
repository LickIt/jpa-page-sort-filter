package com.example.demo.services;

import org.springframework.stereotype.Service;

import com.example.demo.entities.City;

@Service
public class CityServiceImpl implements CityService {
    
    @Override public int getPopulation( City city ) {
        return city.getUsers().size();
    }
}
