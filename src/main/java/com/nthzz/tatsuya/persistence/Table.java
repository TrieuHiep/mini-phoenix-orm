package com.nthzz.tatsuya.persistence;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author tatsuya
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface Table {
    /**
     * (Optional) The name of the table.
     * <p/>
     * Defaults to the entity name.
     */
    String name() default "";

    /**
     * (Optional) The schema of the table.
     * <p/>
     * Defaults to the default schema for user.
     */
    String schema() default "";

}