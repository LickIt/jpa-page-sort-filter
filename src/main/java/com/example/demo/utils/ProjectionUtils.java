package com.example.demo.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import org.springframework.beans.BeanUtils;
import org.springframework.data.projection.*;

public abstract class ProjectionUtils {
    
    private static ProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();
    
    public static <P> P project( Class<P> targetType, Object source ) throws InvocationTargetException, IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors( source.getClass() );
        for( PropertyDescriptor propertyDescriptor : propertyDescriptors ) {
            Object value = propertyDescriptor.getReadMethod().invoke( source );
            map.put( propertyDescriptor.getName(), value );
        }
        
        return project( targetType, map );
    }
    
    public static <P> P project( Class<P> targetType, Map<String, Object> source ) {
        return projectionFactory.createProjection( targetType, source );
    }
    
}

