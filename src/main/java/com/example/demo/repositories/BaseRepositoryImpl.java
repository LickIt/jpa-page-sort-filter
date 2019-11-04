package com.example.demo.repositories;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

import javax.persistence.*;
import javax.persistence.criteria.*;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.*;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

import com.example.demo.utils.ProjectionUtils;

import static java.util.Collections.singletonList;
import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

@NoRepositoryBean
public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
    implements BaseRepository<T, ID> {
    
    private final EntityManager em;
    private final Method toExpressionRecursively = ReflectionUtils.findMethod( QueryUtils.class,
                                                                               "toExpressionRecursively",
                                                                               From.class,
                                                                               PropertyPath.class,
                                                                               boolean.class );
    
    public BaseRepositoryImpl( JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager ) {
        super( entityInformation, entityManager );
        this.em = entityManager;
    }
    
    public BaseRepositoryImpl( Class<T> domainClass, EntityManager em ) {
        super( domainClass, em );
        this.em = em;
    }
    
    @Override
    public <P> Optional<P> findOne( Class<P> projectionType ) {
        return findOne( projectionType, null );
    }
    
    @Override
    public <P> Optional<P> findOne( Class<P> projectionType, Specification<T> spec ) {
        TypedQuery<Tuple> query = getQuery( projectionType, spec, null, null );
        
        try {
            Tuple result = query.getSingleResult();
            return Optional.of( projectResults( projectionType, singletonList( result ) ).get( 0 ) );
        } catch( NoResultException e ) {
            return Optional.empty();
        }
    }
    
    @Override
    public <P> List<P> findAll( Class<P> projectionType ) {
        return findAll( projectionType, Sort.unsorted() );
    }
    
    @Override
    public <P> List<P> findAll( Class<P> projectionType, Sort sort ) {
        return findAll( projectionType, null, sort );
    }
    
    @Override
    public <P> Page<P> findAll( Class<P> projectionType, Pageable pageable ) {
        return findAll( projectionType, null, pageable );
    }
    
    @Override
    public <P> List<P> findAll( Class<P> projectionType, Specification<T> spec ) {
        return findAll( projectionType, spec, Sort.unsorted() );
    }
    
    @Override
    public <P> List<P> findAll( Class<P> projectionType, Specification<T> spec, Sort sort ) {
        TypedQuery<Tuple> query = getQuery( projectionType, spec, sort, null );
        
        List<Tuple> results = query.getResultList();
        return projectResults( projectionType, results );
    }
    
    @Override
    public <P> Page<P> findAll( Class<P> projectionType, Specification<T> spec, Pageable pageable ) {
        
        TypedQuery<Tuple> query = getQuery( projectionType, spec, null, pageable );
        List<Tuple> results = query.getResultList();
        List<P> projectResults = projectResults( projectionType, results );
        
        if( pageable.isPaged() ) {
            return PageableExecutionUtils.getPage( projectResults,
                                                   pageable,
                                                   () -> executeCountQuery( getCountQuery( spec, getDomainClass() ) ) );
        }
        
        return new PageImpl<>( projectResults );
    }
    
    private static long executeCountQuery( TypedQuery<Long> query ) {
        List<Long> totals = query.getResultList();
        long total = 0L;
        
        for( Long element : totals ) {
            total += element == null ? 0 : element;
        }
        
        return total;
    }
    
    private <P> TypedQuery<Tuple> getQuery( Class<P> projectionType,
                                            @Nullable Specification<T> spec,
                                            @Nullable Sort sort,
                                            @Nullable Pageable pageable ) {
        
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createQuery( Tuple.class );
        Root<T> root = query.from( getDomainClass() );
        
        if( spec != null ) {
            applySpecification( spec, root, builder, query );
        }
        
        applyProjection( projectionType, root, query );
        
        if( sort != null ) {
            applySort( sort, root, builder, query );
        }
        
        if( pageable != null ) {
            applySort( pageable.getSortOr( Sort.unsorted() ), root, builder, query );
            return applyPagination( pageable, query );
        }
        
        return em.createQuery( query );
    }
    
    private <P> List<P> projectResults( Class<P> projectionType, List<Tuple> results ) {
        List<P> projectedResults = new ArrayList<>();
        
        for( Tuple tuple : results ) {
            Map<String, Object> map = new HashMap<>();
            
            for( TupleElement<?> element : tuple.getElements() ) {
                map.put( element.getAlias(), tuple.get( element ) );
            }
            
            projectedResults.add( ProjectionUtils.project( projectionType, map ) );
        }
        
        return projectedResults;
    }
    
    private void applySpecification( Specification<T> spec, Root<T> root, CriteriaBuilder builder, CriteriaQuery<?> query ) {
        Predicate predicate = spec.toPredicate( root, query, builder );
        if( predicate != null ) {
            query.where( predicate );
        }
    }
    
    private <P> void applyProjection( Class<P> projectionType, Root<T> root, CriteriaQuery<?> query ) {
        List<Selection<?>> selections = new ArrayList<>();
        for( String property : getInterfaceGetterMethods( projectionType ) ) {
            
            PropertyPath path = PropertyPath.from( property, getDomainClass() );
            
            if( !toExpressionRecursively.isAccessible() ) {
                toExpressionRecursively.setAccessible( true );
            }
            
            Selection<?> selection = ( Selection<?> ) ReflectionUtils.invokeMethod( toExpressionRecursively,
                                                                                    null,
                                                                                    root,
                                                                                    path,
                                                                                    true );
            selections.add( selection.alias( property ) );
        }
        
        query.multiselect( selections );
    }
    
    private void applySort( Sort sort, Root<T> root, CriteriaBuilder builder, CriteriaQuery<?> query ) {
        if( sort.isSorted() ) {
            query.orderBy( toOrders( sort, root, builder ) );
        }
    }
    
    private <R> TypedQuery<R> applyPagination( Pageable pageable, CriteriaQuery<R> query ) {
        TypedQuery<R> typedQuery = em.createQuery( query );
        
        if( pageable.isPaged() ) {
            typedQuery.setFirstResult( ( int ) pageable.getOffset() );
            typedQuery.setMaxResults( pageable.getPageSize() );
        }
        
        return typedQuery;
    }
    
    private List<String> getInterfaceGetterMethods( Class<?> type ) {
        List<String> properties = new ArrayList<>();
        
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors( type );
        for( PropertyDescriptor propertyDescriptor : propertyDescriptors ) {
            if( !propertyDescriptor.getReadMethod().isDefault() ) {
                properties.add( propertyDescriptor.getName() );
            }
        }
        
        return properties;
    }
    
}
