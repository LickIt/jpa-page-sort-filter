package com.example.demo.specifications;

class Filter {
    
    String name;
    FilterOperator operator;
    String value;
    
    Filter( String name, FilterOperator operator, String value ) {
        this.name = name;
        this.operator = operator;
        this.value = value;
    }
}
