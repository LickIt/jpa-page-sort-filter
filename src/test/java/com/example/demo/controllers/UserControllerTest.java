package com.example.demo.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.BaseTest;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class UserControllerTest extends BaseTest {
    
    @Autowired MockMvc mockMvc;
    
    @Test
    void getUsers() throws Exception {
        mockMvc.perform( get( "/api/users" )
                             .param( "page", "0" )
                             .param( "size", "2" )
                             .param( "sort", "firstName,desc" )
                             .param( "filter", "city.name,like,sof" ) )
               .andExpect( status().isOk() )
               .andDo( print() )
               .andExpect( jsonPath( "$.totalElements", equalTo( 3 ) ) )
               .andExpect( jsonPath( "$.content.length()", equalTo( 2 ) ) )
               .andExpect( jsonPath( "$.content[0].name", equalTo( "Kylo Ren" ) ) )
               .andExpect( jsonPath( "$.content[0].cityName", equalTo( "Sofia" ) ) )
               .andExpect( jsonPath( "$.content[1].name", equalTo( "Keanu Reeves" ) ) )
               .andExpect( jsonPath( "$.content[1].cityName", equalTo( "Sofia" ) ) );
    }
}