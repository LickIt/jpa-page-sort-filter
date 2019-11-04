package com.example.demo.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.example.demo.repositories.BaseRepositoryImpl;

@Configuration
@EnableJpaRepositories( basePackages = "com.example", repositoryBaseClass = BaseRepositoryImpl.class )
public class JpaConfiguration {
}
