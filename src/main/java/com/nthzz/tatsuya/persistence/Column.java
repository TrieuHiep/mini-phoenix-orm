package com.nthzz.tatsuya.persistence;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author tatsuya
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Column {

    /**
     * (Optional) The name of the column. Defaults to
     * the property or field name.
     */
    String name() default "";

    /**
     * (Optional) The name of the table that contains the column.
     * If absent the column is assumed to be in the primary table.
     */
    String table() default "";

    /**
     * (Optional) The column length. (Applies only if a
     * string-valued column is used.)
     */
    int length() default 255;
}