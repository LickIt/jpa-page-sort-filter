package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.City;
import com.example.demo.projections.CitySummary;

@Repository
public interface CityRepository extends PagingAndSortingRepository<City, Integer>, JpaSpecificationExecutor<City> {
    
    Page<City> findAllByNameContainsOrderByNameDesc( Pageable pageable, String name );
    
    @Query( "select c from City c left join User u on u.city = c group by c having count(u.id) >= ?1" )
    List<City> findAllWithAtLeastUsers( long usersCount );
    
    CitySummary getSummaryById( int cityId );
}
