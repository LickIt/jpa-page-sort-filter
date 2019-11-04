package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import com.example.demo.projections.UserSummary;
import com.example.demo.repositories.UserRepository;
import com.example.demo.specifications.FilterQuery;

import static org.springframework.data.domain.Sort.Direction.ASC;

@RestController
@RequestMapping( "/api/users" )
public class UserController {
    
    private UserRepository userRepository;
    
    @Autowired
    public UserController( UserRepository userRepository ) {
        this.userRepository = userRepository;
    }
    
    @GetMapping
    public Page<UserSummary> getUsers( @PageableDefault( sort = "username", direction = ASC ) Pageable pageable,
                                       FilterQuery filter ) {
        return userRepository.findAll( UserSummary.class, filter.toSpecification(), pageable );
    }
    
}
