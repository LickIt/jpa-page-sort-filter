package com.example.demo.repositories;

import org.springframework.stereotype.Repository;

import com.example.demo.entities.User;

@Repository
public interface UserRepository extends BaseRepository<User, Integer> {
}
