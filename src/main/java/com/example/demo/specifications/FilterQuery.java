package com.example.demo.specifications;

import java.util.*;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.util.UriUtils;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FilterQuery {
    
    private final List<Filter> filters = new ArrayList<>();
    
    public FilterQuery() {
    }
    
    FilterQuery( String[] filters ) {
        for( String filter : filters ) {
            String[] parts = filter.split( "," );
            and( parts[0], FilterOperator.valueOf( parts[1].toUpperCase() ), UriUtils.decode( parts[2], UTF_8 ) );
        }
    }
    
    public FilterQuery and( String name, FilterOperator operator, String value ) {
        filters.add( new Filter( name, operator, value ) );
        return this;
    }
    
    public <T> Specification<T> toSpecification() {
        if( filters.isEmpty() ) {
            return null;
        }
        
        Specification<T> specification = Specification.where( new FilterSpecification<>( filters.get( 0 ) ) );
        for( int i = 1; i < filters.size(); i++ ) {
            specification.and( new FilterSpecification<>( filters.get( i ) ) );
        }
        
        return specification;
    }
    
}
