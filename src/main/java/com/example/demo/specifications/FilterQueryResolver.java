package com.example.demo.specifications;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.*;

public class FilterQueryResolver implements HandlerMethodArgumentResolver {
    
    @Override public boolean supportsParameter( MethodParameter parameter ) {
        return FilterQuery.class.isAssignableFrom( parameter.getParameterType() );
    }
    
    @Override
    public Object resolveArgument( MethodParameter parameter,
                                   ModelAndViewContainer mavContainer,
                                   NativeWebRequest webRequest,
                                   WebDataBinderFactory binderFactory ) throws Exception {
        
        String[] filters = webRequest.getParameterValues( "filter" );
        if( filters == null ) {
            filters = new String[0];
        }
        
        return new FilterQuery( filters );
    }
}
