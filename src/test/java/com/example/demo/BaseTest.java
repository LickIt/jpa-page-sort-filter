package com.example.demo;

import java.io.IOException;
import java.net.*;
import java.nio.file.*;

import javax.persistence.*;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public abstract class BaseTest {
    
    @PersistenceContext
    EntityManager em;
    
    @BeforeEach
    void setUp() throws IOException, URISyntaxException {
        URI testDataUri = getClass().getClassLoader().getResource( "test-data.sql" ).toURI();
        String sqlScript = new String( Files.readAllBytes( Paths.get( testDataUri ) ) );
        
        em.createNativeQuery( sqlScript ).executeUpdate();
    }
}
