package com.example.demo.repositories;

import java.util.*;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.*;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends PagingAndSortingRepository<T, ID>, JpaSpecificationExecutor<T> {
    
    <P> Optional<P> findOne( Class<P> projectionType );
    
    <P> Optional<P> findOne( Class<P> projectionType, Specification<T> spec );
    
    <P> List<P> findAll( Class<P> projectionType );
    
    <P> List<P> findAll( Class<P> projectionType, Sort sort );
    
    <P> Page<P> findAll( Class<P> projectionType, Pageable pageable );
    
    <P> List<P> findAll( Class<P> projectionType, Specification<T> spec );
    
    <P> List<P> findAll( Class<P> projectionType, Specification<T> spec, Sort sort );
    
    <P> Page<P> findAll( Class<P> projectionType, Specification<T> spec, Pageable pageable );
    
}

