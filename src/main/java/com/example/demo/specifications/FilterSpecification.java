package com.example.demo.specifications;

import javax.persistence.criteria.*;

import org.springframework.data.jpa.domain.Specification;

class FilterSpecification<T> implements Specification<T> {
    
    private final Filter filter;
    
    public FilterSpecification( Filter filter ) {
        this.filter = filter;
    }
    
    @Override public Predicate toPredicate( Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder ) {
        Path<String> path = getRootPath( root );
        switch( filter.operator ) {
            case LIKE:
                return criteriaBuilder.like( criteriaBuilder.lower( path ), "%" + filter.value.toLowerCase() + "%" );
            case EQ:
            default:
                return criteriaBuilder.equal( path, filter.value );
        }
    }
    
    private Path<String> getRootPath( Root<T> root ) {
        String[] properties = filter.name.split( "\\." );
        
        Path<String> path = root.get( properties[0] );
        for( int i = 1; i < properties.length; i++ ) {
            path = path.get( properties[i] );
        }
        
        return path;
    }
}
