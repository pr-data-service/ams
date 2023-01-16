package com.drps.ams.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface FKEntityFieldMapping {	  
    
    String LEFT_JOIN = "LEFT JOIN";
    String RIGHT_JOIN = "RIGHT JOIN";
    String INNER_JOIN = "INNER JOIN";
    
    Class< ?> entity();
    String join();
}
