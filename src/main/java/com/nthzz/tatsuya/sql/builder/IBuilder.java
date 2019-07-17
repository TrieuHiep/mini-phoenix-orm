package com.nthzz.tatsuya.sql.builder;


import com.nthzz.tatsuya.sql.DynamicQuery;

/**
 * Created by vietnb on 9/25/17.
 */
public interface IBuilder<T> {
	DynamicQuery build();
}
